package exam.nlb2t.epot.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.Tables.ImageProductBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductInBill;
import exam.nlb2t.epot.Database.Tables.ProductMyShop;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class DBControllerProduct extends DatabaseController{

    public boolean insertProduct(int salerID, int categoryID, String name, int price, int amount, Bitmap imagePrimary, String description, List<Bitmap> images){
        if(images== null || name == null || description == null){
            Log.e("MY_TAG", "ERROR: insert product with null data");
            return false;
        }

        boolean isOK = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("EXEC createProduct ?,?,?,?,?,?,?");
            preparedStatement.setInt(1, salerID);
            preparedStatement.setInt(2, categoryID);
            preparedStatement.setString(3, name);
            preparedStatement.setInt(4, price);
            preparedStatement.setInt(5, amount);

            // Set image primary
            if(imagePrimary != null)
            {
                byte[] mainImage = Helper.toByteArray(imagePrimary, MEDIUM_SIZE_IMAGES_IN_PIXEL, MEDIUM_SIZE_IMAGES_IN_PIXEL);
                if(mainImage != null && mainImage.length < MAX_BYTE_IMAGE)
                {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(mainImage);
                    preparedStatement.setBinaryStream(6, inputStream, mainImage.length);
                }
                else {preparedStatement.setNull(6, Types.VARBINARY);}
            }
            else {preparedStatement.setNull(6, Types.VARBINARY);}

            preparedStatement.setString(7, description);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                // Get new product's id
                int productID = resultSet.getInt(1);
                for (Bitmap bitmap : images) {
                    if (!insertProduct_Image(productID, bitmap)) {
                        break;
                    }
                }
                isOK = true;
            }

            if(isOK) {
                commit();
            }
            else {rollback();}

            preparedStatement.close();
            resultSet.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            rollback();
            ErrorMsg = e.getMessage();
        }
        return isOK;
    }

    public boolean updateProduct(ProductMyShop product, List<Bitmap> newimages, int[] deletedImagesID) {
        if(newimages == null || deletedImagesID == null){
            Log.e("MY_TAG", "ERROR: update product with null data");
            return false;
        }
        boolean isOK = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("EXEC updateProduct ?,?,?,?,?,?,?,?");
            preparedStatement.setInt(1, product.id);
            preparedStatement.setInt(2, product.categoryID);
            preparedStatement.setString(3, product.name);
            preparedStatement.setInt(4, product.price);
            preparedStatement.setInt(5, product.priceOrigin);
            preparedStatement.setInt(6, product.amount);
            //Set null for primary image, it cannot change
            preparedStatement.setNull(7, Types.VARBINARY);
            preparedStatement.setString(8, product.description);

            int result = preparedStatement.executeUpdate();

            // TODO: Add Images to db
            // TODO: Something wrongs here
            if(result != 0) {
                for (Bitmap bitmap : newimages) {
                    if (!insertProduct_Image(product.id, bitmap)) {
                        throw new SQLException("Không thể thêm ảnh mới vào DB");
                    }
                }
                for (int i = 0; i< deletedImagesID.length; i++)
                if (!removeImageProductbyID(deletedImagesID[i]))
                        throw new SQLException("Không thể xóa ảnh có sẵn trong DB, ID: " + deletedImagesID[i]);

                isOK = true;
            }

            if(isOK) {
                commit();
            }
            else {rollback();}

            preparedStatement.close();
        }
        catch (SQLException e)
        {
            ErrorMsg = "Lỗi khi cập nhật món hàng vào DB";
            e.printStackTrace();
            rollback();
        }
        return isOK;
    }

    private boolean removeImageProductbyID(int id) {
        boolean isOk = false;
        try {
            String sql = "DELETE FROM PRODUCT_IMAGE WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            if (statement.executeUpdate() == 1) isOk = true;

            if (isOk) {
                commit();
            }
            else rollback();

            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            rollback();
        }
        return isOk;
    }

    public boolean insertProduct_Image(int productID, Bitmap bitmap)
    {
        byte[] mainImage = Helper.toByteArray(bitmap, BIG_SIZE_PRODUCT_IMAGES_IN_PIXEL, BIG_SIZE_PRODUCT_IMAGES_IN_PIXEL);
        if(mainImage == null) {return false;}

        if(mainImage.length > MAX_BYTE_IMAGE)
        {
            Log.e("MY_TAG", "ERROR: Image is too big");
            return false;
        }
        try
        {
            PreparedStatement statement = connection.prepareStatement("EXEC addImageForProduct ?,?");
            statement.setInt(1, productID);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(mainImage);
            statement.setBinaryStream(2, inputStream, mainImage.length);

            int rs = statement.executeUpdate();

            inputStream.close();
            statement.close();
            return rs == 1;
        }
        catch (SQLException | IOException e)
        {
            e.printStackTrace();
            return  false;
        }
    }

    public Bitmap getAvatar_Product(int avatarID)
    {
        Bitmap rs = null;
        try
        {
            String sql = "select [DATA] from [AVATAR] where [ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, avatarID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                rs = BitmapFactory.decodeStream(resultSet.getBinaryStream(1));
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }

    public Bitmap getAvatar_Product(int avatarID, int targetWidth, int targetHeight)
    {
        Bitmap rs = null;
        try
        {
            String sql = "select [DATA] from [AVATAR] where [ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, avatarID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                InputStream is = resultSet.getBinaryStream(1);
                rs = Helper.getScaleImage(is,targetWidth,targetHeight);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }

    public List<Bitmap> getImages(int productID)
    {
        List<Bitmap> rs = new ArrayList<>();
        try
        {
            String sql = "select [DATA] from [PRODUCT_IMAGE] where [PRODUCT_ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                rs.add(BitmapFactory.decodeStream(resultSet.getBinaryStream(1)));
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }

    public List<ImageProductBaseDB> getOverviewImages(int productID)
    {
        List<ImageProductBaseDB> rs = new ArrayList<>();
        try
        {
            String sql = "select [ID], [DATA] from [PRODUCT_IMAGE] where [PRODUCT_ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                ImageProductBaseDB image = new ImageProductBaseDB();
                image.id = resultSet.getInt("ID");
                image.value = BitmapFactory.decodeStream(resultSet.getBinaryStream("DATA"));
                rs.add(image);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }

    public ProductBaseDB getProduct(int productID)
    {
        ProductBaseDB rs = null;
        try
        {
            String sql = "select * from [PRODUCT] where [ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
            {
                rs = new ProductBaseDB();
                int i = 1;
                rs.id = resultSet.getInt(i);i++;
                rs.salerID = resultSet.getInt(i);i++;
                rs.categoryID = resultSet.getInt(i);i++;
                rs.name = resultSet.getString(i);i++;
                rs.price = resultSet.getInt(i);i++;
                rs.priceOrigin = resultSet.getInt(i);i++;
                rs.amount = resultSet.getInt(i);i++;
                rs.amountSold = resultSet.getInt(i);i++;
                rs.imagePrimaryID = resultSet.getInt(i);i++;
                rs.description = resultSet.getString(i);i++;
                rs.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp(i).getTime());i++;
                rs.deleted = resultSet.getInt(i);i++;
                rs.starAverage = resultSet.getFloat(i);i++;
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }

    public boolean deleteProduct(int productID) {
        boolean rs = false;
        try {
            String sql = "DELETE FROM [LIKE] WHERE [PRODUCT_ID] = ? ;" +
                    " DELETE FROM [PRODUCT_IMAGE] WHERE [PRODUCT_ID] = ? ; " +
                    "DELETE FROM [PRODUCT] WHERE [ID] = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productID);
            statement.setInt(2, productID);
            statement.setInt(3, productID);
            rs = statement.executeUpdate() == 1;

            commit();
            statement.close();
        }
        catch (SQLException e)
        {
            ErrorMsg = "Cannot write to server";
            e.printStackTrace();
        }
        return rs;
    }

    public List<ProductBaseDB> getProducts(int userID)
    {
        List<ProductBaseDB> rs = new ArrayList<>();
        try
        {
            String sql = "select * from [PRODUCT] where [SALER_ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                int i = 1;
                ProductBaseDB item = new ProductBaseDB();

                item.id = resultSet.getInt(i);i++;
                item.salerID = resultSet.getInt(i);i++;
                item.categoryID = resultSet.getInt(i);i++;
                item.name = resultSet.getString(i);i++;
                item.price = resultSet.getInt(i);i++;
                item.priceOrigin = resultSet.getInt(i);i++;
                item.amount = resultSet.getInt(i);i++;
                item.amountSold = resultSet.getInt(i);i++;
                item.imagePrimaryID = resultSet.getInt(i);i++;
                item.description = resultSet.getString(i);i++;
                item.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp(i).getTime());i++;
                item.deleted = resultSet.getInt(i);i++;

                rs.add(item);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rs;
    }
    public List<ProductBaseDB> getLIMITProduct(int salerID, int offset, int number)
    {
        List<ProductBaseDB> rs = new ArrayList<>();
        try
        {
            PreparedStatement statement = connection.prepareStatement("EXEC getLIMITProduct ?,?,?");
            statement.setInt(1, salerID);
            statement.setInt(2, offset);
            statement.setInt(3, number);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                ProductBaseDB item = new ProductBaseDB();
                item.salerID = salerID;

                item.id = resultSet.getInt("ID");
                item.categoryID = resultSet.getInt("CATEGORY_ID");
                item.name = resultSet.getString("NAME");
                item.price = resultSet.getInt("PRICE");
                item.priceOrigin = resultSet.getInt("PRICE_ORIGIN");
                item.amount = resultSet.getInt("AMOUNT");
                item.amountSold = resultSet.getInt("AMOUNT_SOLD");
                item.imagePrimaryID = resultSet.getInt("PRIMARY_IMAGE_ID");
                item.description = resultSet.getString("DETAIL");
                item.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp("CREATED_DATE").getTime());

//                InputStream is = resultSet.getBinaryStream("PRIMARY_IMAGE");
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeStream(is,null,options);
//
//                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, R.dimen.btn_size, )
//                options.inSampleSize = Helper.calculateInSampleSize(options, 120,120);
//
//                options.inJustDecodeBounds = false;
//                item.imageProduct = BitmapFactory.decodeStream(resultSet.getBinaryStream("PRIMARY_IMAGE"), null, options);

                rs.add(item);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rs;
    }

    public List<ProductMyShop> getProductMyShop(int offset, int number) {
        List<ProductMyShop> rs = new ArrayList<>();
        try
        {
            PreparedStatement statement = connection.prepareStatement("EXEC getLIMITProduct ?,?,?");
            statement.setInt(1, Authenticator.getCurrentUser().id);
            statement.setInt(2, offset);
            statement.setInt(3, number);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                ProductMyShop item = new ProductMyShop();

                item.id = resultSet.getInt("ID");
                item.categoryID = resultSet.getInt("CATEGORY_ID");
                item.name = resultSet.getString("NAME");
                item.price = resultSet.getInt("PRICE");
                item.priceOrigin = resultSet.getInt("PRICE_ORIGIN");
                item.amount = resultSet.getInt("AMOUNT");
                item.amountSold = resultSet.getInt("AMOUNT_SOLD");
                item.imagePrimaryID = resultSet.getInt("PRIMARY_IMAGE_ID");
                item.description = resultSet.getString("DETAIL");
                item.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp("CREATED_DATE").getTime());

                item.numberLike =  getNumberLikeProduct(item.id);

                rs.add(item);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rs;
    }
    public List<ProductMyShop> getProductMyShop2(int userID, int offset, int number) {
        List<ProductMyShop> rs = new ArrayList<>();
        try
        {
            PreparedStatement statement = connection.prepareStatement("EXEC getLIMITProduct2 ?,?,?");
            statement.setInt(1, userID);
            statement.setInt(2, offset);
            statement.setInt(3, number);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                ProductMyShop item = new ProductMyShop();

                item.id = resultSet.getInt("ID");
                item.categoryID = resultSet.getInt("CATEGORY_ID");
                item.name = resultSet.getString("NAME");
                item.price = resultSet.getInt("PRICE");
                item.priceOrigin = resultSet.getInt("PRICE_ORIGIN");
                item.amount = resultSet.getInt("AMOUNT");
                item.amountSold = resultSet.getInt("AMOUNT_SOLD");
                item.imagePrimaryID = resultSet.getInt("PRIMARY_IMAGE_ID");
                item.description = resultSet.getString("DETAIL");
                item.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp("CREATED_DATE").getTime());

                item.numberLike =  getNumberLikeProduct(item.id);
                rs.add(item);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rs;
    }

    public List<ProductBaseDB> getNewProductList(String sql)
    {
        List<ProductBaseDB> rs = new ArrayList<>();
        try
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                int i = 1;
                ProductBaseDB item = new ProductBaseDB();

                item.id = resultSet.getInt(i);i++;
                item.salerID = resultSet.getInt(i);i++;
                item.categoryID = resultSet.getInt(i);i++;
                item.name = resultSet.getString(i);i++;
                item.price = resultSet.getInt(i);i++;
                item.priceOrigin = resultSet.getInt(i);i++;
                item.amount = resultSet.getInt(i);i++;
                item.amountSold = resultSet.getInt(i);i++;
                item.imagePrimaryID = resultSet.getInt(i);i++;
                item.description = resultSet.getString(i);i++;
                item.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp(i).getTime());i++;
                item.deleted = resultSet.getInt(i);i++;
                item.starAverage = resultSet.getFloat(i);i++;

                rs.add(item);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rs;
    }

    public boolean likeProduct(int productID, int userID)
    {
        boolean rs = false;
        try
        {
            String sql = "INSERT INTO [LIKE]([USER_ID],[PRODUCT_ID]) VALUES(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userID);
            statement.setInt(2, productID);
            rs = statement.executeUpdate() == 1;

            commit();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            rollback();
        }
        return rs;
    }

    public boolean unlikeProduct(int productID, int userID)
    {
        boolean rs = false;
        try
        {
            String sql = "DELETE FROM [LIKE] WHERE [USER_ID]=? AND [PRODUCT_ID]=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userID);
            statement.setInt(2, productID);
            rs = statement.executeUpdate() > 0;

            commit();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            rollback();
        }
        return rs;
    }

    public boolean checkLikeProduct(int productID, int userID)
    {
        boolean rs = false;
        try
        {
            String sql = "SELECT [USER_ID] FROM [LIKE] WHERE [USER_ID]=? AND [PRODUCT_ID]=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userID);
            statement.setInt(2, productID);

            ResultSet resultSet = statement.executeQuery();
            rs = resultSet.next();

            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }

    public int getNumberLikeProduct(int productID)
    {
        int rs = 0;
        try {
            String sql = "SELECT COUNT(*) FROM [LIKE] WHERE [PRODUCT_ID]=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productID);
            ResultSet rsSet = statement.executeQuery();
            if (rsSet.next()) {
                rs = rsSet.getInt(1);
            }
            rsSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public int getCountRating(int productID)
    {
        int rs = 0;
        try {
            String sql = "SELECT COUNT(*) FROM [RATING] WHERE [PRODUCT_ID]=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productID);
            ResultSet rsSet = statement.executeQuery();
            if (rsSet.next()) {
                rs = rsSet.getInt(1);
            }
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public List<ProductBaseDB> getProductsBaseName( String name, int offset, int rows)
    {
        List<ProductBaseDB> rs = new ArrayList<>();
        try
        {
            String sql =
                            "(Select  * from (" +
                            " select Pro.ID, Pro.SALER_ID, Pro.CATEGORY_ID, Pro.NAME as Name, Pro.PRICE, Pro.PRICE_ORIGIN, Pro.AMOUNT, Pro.AMOUNT_SOLD, Pro.PRIMARY_IMAGE_ID, " +
                            " Pro.DETAIL, Pro.CREATED_DATE as CREATED_DATE, Pro.DELETED, D.DATA" +
                            " from [PRODUCT] AS Pro INNER join  [AVATAR] as D on Pro.PRIMARY_IMAGE_ID = D.ID " +
                            " where (Pro.NAME LIKE '" + name+"%' COLLATE Vietnamese_CI_AI or Pro.NAME LIKE '" + name+"%' COLLATE Vietnamese_CI_AS or Pro.NAME LIKE N'" + name+"%') and pro.DELETED = 0 ) p1" +
                           /* " ORDER BY  CREATED_DATE DESC  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY )" + */
                            " union " +
                            " Select * from (" +
                            " select Pro.ID, Pro.SALER_ID, Pro.CATEGORY_ID, Pro.NAME, Pro.PRICE, Pro.PRICE_ORIGIN, Pro.AMOUNT, Pro.AMOUNT_SOLD, Pro.PRIMARY_IMAGE_ID, " +
                            " Pro.DETAIL, Pro.CREATED_DATE, Pro.DELETED, D.DATA " +
                            " from [PRODUCT] AS Pro INNER join  [AVATAR] as D on Pro.PRIMARY_IMAGE_ID = D.ID " +
                            " where (Pro.NAME LIKE '%" + name+"%'  COLLATE Vietnamese_CI_AI or Pro.NAME LIKE '%" + name+"%' COLLATE Vietnamese_CI_AS  or Pro.NAME LIKE N'%" + name+"%' )and pro.DELETED = 0 ) p2)" +
                           /* " ORDER BY  CREATED_DATE  DESC  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ) " +*/
                            " ORDER BY  Name  OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, offset);
            statement.setInt(2,  rows);
            /*statement.setInt(3, offset);
            statement.setInt(4, rows + offset);
            statement.setInt(5, offset);
            statement.setInt(6, rows);*/
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                int i = 1;
                ProductBaseDB item = new ProductBaseDB();

                item.id = resultSet.getInt(i);i++;
                item.salerID = resultSet.getInt(i);i++;
                item.categoryID = resultSet.getInt(i);i++;
                item.name = resultSet.getString(i);i++;
                item.price = resultSet.getInt(i);i++;
                item.priceOrigin = resultSet.getInt(i);i++;
                item.amount = resultSet.getInt(i);i++;
                item.amountSold = resultSet.getInt(i);i++;
                item.imagePrimaryID = resultSet.getInt(i);i++;
                item.description = resultSet.getString(i);i++;
                item.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp(i).getTime());i++;
                item.deleted = resultSet.getInt(i);i++;
                item.imageProduct = BitmapFactory.decodeStream(resultSet.getBinaryStream(i));

                rs.add(item);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
           e.printStackTrace();
        }

        return rs;
    }
    public List<ProductBaseDB> getProductsBaseCategory( String name, int offset, int rows)
    {
        List<ProductBaseDB> rs = new ArrayList<>();
        try
        {
            String sql = "select P.ID, P.SALER_ID, P.CATEGORY_ID, P.NAME, P.PRICE, P.PRICE_ORIGIN, P.AMOUNT, P.AMOUNT_SOLD, P.PRIMARY_IMAGE_ID," +
                    " P.DETAIL, P.CREATED_DATE, P.DELETED, A.DATA" +
                    " from [PRODUCT] AS P INNER join  [AVATAR] as A on P.PRIMARY_IMAGE_ID = A.ID " +
                    "  INNER JOIN [CATEGORY]  AS C on P.CATEGORY_ID = C.ID  "+
                    " where C.NAME LIKE N'%" + name + "%' and P.DELETED = 0" +
                    " ORDER BY CREATED_DATE  DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;"; ; /*LIMIT 2 OFFSET 0";*/
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, offset);
            statement.setInt(2, rows);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                int i = 1;
                ProductBaseDB item = new ProductBaseDB();

                item.id = resultSet.getInt(i);i++;
                item.salerID = resultSet.getInt(i);i++;
                item.categoryID = resultSet.getInt(i);i++;
                item.name = resultSet.getString(i);i++;
                item.price = resultSet.getInt(i);i++;
                item.priceOrigin = resultSet.getInt(i);i++;
                item.amount = resultSet.getInt(i);i++;
                item.amountSold = resultSet.getInt(i);i++;
                item.imagePrimaryID = resultSet.getInt(i);i++;
                item.description = resultSet.getString(i);i++;
                item.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp(i).getTime());i++;
                item.deleted = resultSet.getInt(i);i++;
                item.imageProduct = BitmapFactory.decodeStream(resultSet.getBinaryStream(i));
                i++;
                rs.add(item);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rs;
    }

    public List<ProductBaseDB> getProductsBaseSaler( String name, int offset, int rows)
    {
        List<ProductBaseDB> rs = new ArrayList<>();
        try
        {
            String sql =  "select P.ID, P.SALER_ID, P.CATEGORY_ID, P.NAME, P.PRICE, P.PRICE_ORIGIN, P.AMOUNT, P.AMOUNT_SOLD, P.PRIMARY_IMAGE_ID," +
                " P.DETAIL, P.CREATED_DATE, P.DELETED, A.DATA" +
                " from [PRODUCT] AS P INNER join  [AVATAR] as A on P.PRIMARY_IMAGE_ID = A.ID " +
                " INNER JOIN [USER]  AS U on P.SALER_ID = U.ID "+
                " where U.USERNAME LIKE N'%" + name + "%'  and P.DELETED = 0" +
                " ORDER BY CREATED_DATE  DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;"; /*LIMIT 2 OFFSET 0";*/            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, offset);
            statement.setInt(2, rows);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                int i = 1;
                ProductBaseDB item = new ProductBaseDB();

                item.id = resultSet.getInt(i);i++;
                item.salerID = resultSet.getInt(i);i++;
                item.categoryID = resultSet.getInt(i);i++;
                item.name = resultSet.getString(i);i++;
                item.price = resultSet.getInt(i);i++;
                item.priceOrigin = resultSet.getInt(i);i++;
                item.amount = resultSet.getInt(i);i++;
                item.amountSold = resultSet.getInt(i);i++;
                item.imagePrimaryID = resultSet.getInt(i);i++;
                item.description = resultSet.getString(i);i++;
                item.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp(i).getTime());i++;
                item.deleted = resultSet.getInt(i);++i;
                item.imageProduct = BitmapFactory.decodeStream(resultSet.getBinaryStream(i));
                i++;
                rs.add(item);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rs;
    }

  public int getNumberProducts(int saler) {
        int rs = 0;
        try {
            String sql = "SELECT COUNT(*) FROM [PRODUCT] WHERE [SALER_ID]=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,saler);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                rs = resultSet.getInt(1);
            }

            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {

            e.printStackTrace();
        }

        return rs;
    }

    public void setSalePriceProduct(int productID, int newSalePrice) {
        try {
            String sql = "UPDATE [PRODUCT] SET [PRICE] = ? WHERE [ID]=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,newSalePrice);
            statement.setInt(2, productID);
            int rs = statement.executeUpdate();

            if (rs != 1) {
                rollback();
                Log.e("UPDATE DATABASE", "Cannot update price for Product");
            }
            else {
                commit();
            }

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProductBaseDB> getLikedProduct(int userID, int start, int end)
    {
        List<ProductBaseDB> rs = null;
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC getProductLiked ?,?,?;");
            statement.setInt(1, userID);
            statement.setInt(2, start);
            statement.setInt(3, end);

            rs = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                int i = 1;
                ProductBaseDB item = new ProductBaseDB();

                item.id = resultSet.getInt(i);i++;
                item.salerID = resultSet.getInt(i);i++;
                item.categoryID = resultSet.getInt(i);i++;
                item.name = resultSet.getString(i);i++;
                item.price = resultSet.getInt(i);i++;
                item.priceOrigin = resultSet.getInt(i);i++;
                item.amount = resultSet.getInt(i);i++;
                item.amountSold = resultSet.getInt(i);i++;
                item.imagePrimaryID = resultSet.getInt(i);i++;
                item.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp(i).getTime());i++;
                item.deleted = resultSet.getInt(i);i++;
                item.starAverage = resultSet.getFloat(i);i++;
                rs.add(item);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "FAILED: Cannot get data from server";
        }
        return rs;
    }

    public List<ProductInBill> getProductInBill(int billID) {
        List<ProductInBill> rs = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT [PRODUCT].[ID], [PRODUCT].[NAME], " +
                    "[BILL_DETAIL].[PRICE], [PRODUCT].[PRIMARY_IMAGE_ID], [BILL_DETAIL].[AMOUNT] " +
                    "FROM [PRODUCT] JOIN [BILL_DETAIL] ON [PRODUCT].ID = [BILL_DETAIL].[PRODUCT_ID] " +
                    "WHERE [BILL_DETAIL].[BILL_ID] = ?;");
            statement.setInt(1, billID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int imagePrimaryID = resultSet.getInt("PRIMARY_IMAGE_ID");
                int price = resultSet.getInt("PRICE");
                int amount = resultSet.getInt("AMOUNT");

                rs.add(new ProductInBill(id, imagePrimaryID, name, price, amount));
            }

            if(rs.size() == 0) throw new SQLException();

            resultSet.close();
            statement.close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "FAILED: Cannot find product with billID: " + billID;
        }
        return rs;
    }

    public int getNumberProductOutofStock(int salerID) {
        final int LIMIT_NUMBER_OUTOFSTOCK = 5;
        int rs = 0;
        try {
            String sql = "SELECT COUNT(*) FROM [PRODUCT] WHERE [SALER_ID] = ? AND (AMOUNT - AMOUNT_SOLD) <= ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, salerID);
            statement.setInt(2, LIMIT_NUMBER_OUTOFSTOCK);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                rs = resultSet.getInt(1);
            }

            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public boolean checkProductIsSold(int productID) {
        boolean isExists = false;
        try {
            String sql = "SELECT TOP(1) * FROM [BILL_DETAIL] where [PRODUCT_ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                isExists = true;
            }

            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            ErrorMsg = "Error when check Product is sold";
            e.printStackTrace();
        }
        return isExists;
    }

    public void StopProvide(int productID) {
        try {
            String sql = "UPDATE PRODUCT SET DELETED = 1 where ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productID);

            if(statement.executeUpdate() == 0) {
                throw new SQLException("Không thể tìm thấy món ăn để dừng cung cấp");
            }

            commit();
            statement.close();
        }
        catch (SQLException e) {
            ErrorMsg = "Error when update Product deleted";
            e.printStackTrace();
        }
    }

    public void updateQuantityProduct(int productID, int quantity) {
        try {
            String sql = "UPDATE [PRODUCT] SET AMOUNT_SOLD = AMOUNT_SOLD - ? WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,quantity);
            statement.setInt(2,productID);

            if(statement.executeUpdate() == 1) {
                commit();
            }
            else {
                rollback();
                throw new SQLException("Không tìm thấy sản phẩm để cập nhật");
            }

            statement.close();

        } catch (SQLException throwables) {
            ErrorMsg = "Không thể cập nhật lại số lượng đã bán của món hàng có mã số: " + productID;
            throwables.printStackTrace();
        }
    }
}
