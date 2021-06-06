package exam.nlb2t.epot.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import exam.nlb2t.epot.singleton.Helper;

public class DBControllerProduct extends DatabaseController{

    public boolean insertProduct(int salerID, int categoryID, String name, int price, int amount, Bitmap imagePrimary, String description, List<Bitmap> images){
        if(images== null || name == null || description == null){
            Log.e("MY_TAG", "ERROR: insert product with null data");
            return false;
        }

        byte[] mainImage = Helper.toByteArray(imagePrimary, BIG_SIZE_PRODUCT_IMAGES_IN_PIXEL, BIG_SIZE_PRODUCT_IMAGES_IN_PIXEL);
        if(mainImage.length > MAX_BYTE_IMAGE)
        {
            Log.e("MY_TAG", "ERROR: Image is too big");
            return false;
            //throw new SQLException(String.format("LỖI: Ảnh có kích thước quá lớn (>%dkB)", (MAX_BYTE_IMAGE/1000)));
        }
        boolean isOK = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("EXEC createProduct ?,?,?,?,?,?,?");
            preparedStatement.setInt(1, salerID);
            preparedStatement.setInt(2, categoryID);
            preparedStatement.setString(3, name);
            preparedStatement.setInt(4, price);
            preparedStatement.setInt(5, amount);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(mainImage);
            preparedStatement.setBinaryStream(6, inputStream, mainImage.length);
            preparedStatement.setString(7, description);

            int rs = preparedStatement.executeUpdate();
            preparedStatement.close();
            Log.d("MY_TAG", rs+"");
            if(rs != 0)
            {
                if(images.size() == 0 || insertProduct_Images(images)) {
                    connection.commit();
                    isOK = true;
                }
            }
            else {rollback();}
            inputStream.close();
        }
        catch (SQLException | IOException e)
        {
            e.printStackTrace();
            rollback();
        }
        return isOK;
    }

    // Insert images to latest product
    public boolean insertProduct_Images(List<Bitmap> bitmaps)
    {
        int latestID = -1;
        try (PreparedStatement statement = connection.prepareStatement("select max([ID]) from [PRODUCT]");
             ResultSet resultSet = statement.executeQuery();)
        {
            if(resultSet.next())
            {
                latestID = resultSet.getInt(1);
                statement.close();
                resultSet.close();
                for(Bitmap bitmap: bitmaps) {
                    if(!insertProduct_Image(latestID, bitmap))
                    {
                        return false;
                    }
                }
                return true;
            }
            else {return false;}
        }
        catch (SQLException e)
        {
            e.printStackTrace(); return false;
        }

    }

    public boolean insertProduct_Image(int productID, Bitmap bitmap)
    {
        byte[] mainImage = Helper.toByteArray(bitmap);
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

    public Bitmap getImage_Product(int id)
    {
        Bitmap rs = null;
        try
        {
            String sql = "select [DATA] from [PRODUCT_IMAGE] where [ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                rs = BitmapFactory.decodeStream(resultSet.getBinaryStream(1));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }
}
