package exam.nlb2t.epot.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
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

    public boolean updateProduct(int productID, int categoryID, String name, int price, int newExsistAmount, Bitmap imagePrimary, String description, List<Bitmap> images) {
        if(productID < 0 || images == null || name == null || description == null){
            Log.e("MY_TAG", "ERROR: update product with null data");
            return false;
        }
        boolean isOK = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("EXEC updateProduct ?,?,?,?,?,?,?");
            preparedStatement.setInt(1, productID);
            preparedStatement.setInt(2, categoryID);
            preparedStatement.setString(3, name);
            preparedStatement.setInt(4, price);
            preparedStatement.setInt(5, newExsistAmount);

            // Set image primary
            if(imagePrimary != null)
            {
                byte[] mainImage = Helper.toByteArray(imagePrimary, MEDIUM_SIZE_IMAGES_IN_PIXEL, MEDIUM_SIZE_IMAGES_IN_PIXEL);
                if(mainImage != null && mainImage.length < MAX_BYTE_IMAGE)
                {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(mainImage);
                    preparedStatement.setBinaryStream(6, inputStream, mainImage.length);
                    inputStream.close();
                }
                else {preparedStatement.setNull(6, Types.VARBINARY);}
            }
            else {preparedStatement.setNull(6, Types.VARBINARY);}

            preparedStatement.setString(7, description);

            ResultSet resultSet = preparedStatement.executeQuery();

            // TODO: Add Images to db
            // TODO: Something wrongs here
            if(resultSet.next()) {
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
        catch (SQLException | IOException e)
        {
            e.printStackTrace();
            rollback();
            ErrorMsg = e.getMessage();
        }
        return isOK;
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
                rs.createdDate = Helper.getDateLocalFromUTC(resultSet.getDate(i));i++;
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
            String sql = "DELETE FROM [PRODUCT] WHERE [ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productID);
            rs = statement.executeUpdate() == 1;

            commit();
            statement.close();
        }
        catch (SQLException e)
        {
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
                item.createdDate = resultSet.getDate(i);i++;
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
    public List<ProductBaseDB> getLIMITProduct(int userID, int offset, int number)
    {
        List<ProductBaseDB> rs = new ArrayList<>();
        try
        {
            PreparedStatement statement = connection.prepareStatement("EXEC getLIMITProduct ?,?,?");
            statement.setInt(1, userID);
            statement.setInt(2, offset);
            statement.setInt(3, number);
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
                item.createdDate = resultSet.getDate(i);i++;
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
            rs = statement.executeUpdate() >0;

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
        name = name.toUpperCase();
        try
        {
            String sql = "select * from [PRODUCT] where UPPER(NAME) LIKE '%" + name + "%' ORDER BY CREATED_DATE  DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;"; /*LIMIT 2 OFFSET 0";*/
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
                item.createdDate = resultSet.getDate(i);i++;
                item.deleted = resultSet.getInt(i);
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
    public List<ProductBaseDB> getProductsBaseCategory( String name, int offset, int rows)
    {
        List<ProductBaseDB> rs = new ArrayList<>();
        name = name.toUpperCase();
        try
        {
            String sql = "select P.ID, P.SALER_ID, P.CATEGORY_ID, P.NAME, P.PRICE, P.PRICE_ORIGIN, P.AMOUNT, P.AMOUNT_SOLD, P.PRIMARY_IMAGE_ID, P.DETAIL, P.CREATED_DATE, P.DELETED from [PRODUCT] AS P INNER JOIN [CATEGORY]  AS C on P.CATEGORY_ID = C.ID AND UPPER(C.NAME) LIKE'%" + name + "%' ORDER BY P.CREATED_DATE  DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;"  ; /*LIMIT 2 OFFSET 0";*/
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
                item.createdDate = resultSet.getDate(i);i++;
                item.deleted = resultSet.getInt(i);
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
        name = name.toUpperCase();
        try
        {
            String sql = "select P.ID, P.SALER_ID, P.CATEGORY_ID, P.NAME, P.PRICE, P.PRICE_ORIGIN, P.AMOUNT, P.AMOUNT_SOLD, P.PRIMARY_IMAGE_ID, P.DETAIL, P.CREATED_DATE, P.DELETED from [PRODUCT] AS P INNER JOIN [USER]  AS U on P.SALER_ID = U.ID AND UPPER(U.USERNAME) LIKE'%" + name + "%' ORDER BY P.CREATED_DATE  DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;"  ; /*LIMIT 2 OFFSET 0";*/            PreparedStatement statement = connection.prepareStatement(sql);
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
                item.createdDate = resultSet.getDate(i);i++;
                item.deleted = resultSet.getInt(i);
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

  public int getNumberProductsbyUser(int userID) {
        int rs = 0;
        try {
            String sql = "SELECT COUNT(*) FROM [PRODUCT] WHERE [SALER_ID]=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                rs = resultSet.getInt(1);
            }
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
            ResultSet resultSet = statement.executeQuery();

            commit();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProductBaseDB> getLikedProduct(int userID)
    {
        List<ProductBaseDB> rs = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT [PRODUCT_ID] FROM [LIKE] WHERE [USER_ID]=?;");
            statement.setInt(1, userID);

            ResultSet resultSet = statement.executeQuery();
            List<Integer> productIDs = new ArrayList<>();
            while (resultSet.next())
            {
                productIDs.add(resultSet.getInt(1));
            }
            resultSet.close();
            statement.close();

            rs = new ArrayList<>();
            for(Integer id: productIDs)
            {
                rs.add(getProduct(id));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "FAILED: Cannot get data from server";
        }
        return rs;
    }
}
