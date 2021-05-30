package exam.nlb2t.epot.Database;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import exam.nlb2t.epot.SQL.DataController;
import exam.nlb2t.epot.singleton.Helper;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseController {

    private DataController dataController = new DataController();
    private Connection connection;

    public DatabaseController() {
        try{
            connection = dataController.ConnnectionData();
            connection.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    void rollback()
    {
        try {
            connection.rollback();
        }
        catch (SQLException e)
        {
            Log.e("MY_TAG", "ERROR: Failed to rollback connection");
            e.printStackTrace();
        }
    }

    public void closeConnection()
    {
        try {
            connection.close();
        } catch (SQLException e) {
            Log.e("MY_TAG", "ERROR: Failed to close connection");
            e.printStackTrace();
        }
    }

    public boolean InsertUser(String phone, String email, String password, String fullname,
                              int gender, String joinday, String birthday, String address,
                              String shopname,  int follower, String info)
    {

        return true;
    }

    public boolean CheckUserExist(String username){

        return false;
    }

    public boolean CheckphoneExist(String phone){

        return false;
    }

    public boolean CheckEmailExist(String email){

        return false;
    }

    public boolean CheckPassword(String username, String password){

        return true;
    }

    final int MAX_BYTE_IMAGE = 10000;
    public boolean insertProduct(int salerID, int categoryID, String name, int price, int amount, Bitmap imagePrimary, String description, List<Bitmap> images)
    {
        if(images== null || name == null || description == null){
            Log.e("MY_TAG", "ERROR: insert product with null data");
            return false;
        }

        byte[] mainImage = Helper.toByteArray(imagePrimary);
        if(mainImage.length > MAX_BYTE_IMAGE)
        {
            Log.e("MY_TAG", "ERROR: Image is too big");
            return false;
        }

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("{EXEC createProduct ? ? ? ? ? ? ?}");
            salerID = 0;
            preparedStatement.setInt(1, salerID);
            categoryID = 0;
            preparedStatement.setInt(2, categoryID);
            preparedStatement.setString(3, name);
            preparedStatement.setInt(4, price);
            preparedStatement.setInt(5, amount);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(mainImage);
            preparedStatement.setBinaryStream(6, inputStream);
            preparedStatement.setString(7, description);
            preparedStatement.addBatch();

            int[] rs = preparedStatement.executeBatch();
            preparedStatement.close();
            inputStream.close();
            Log.d("MY_TAG", rs[0]+"");
            if(rs[0] == 2)
            {
                if(images.size() == 0 || insertProduct_Images(images)) {
                    connection.commit();
                    return true;
                }
            }
            rollback();
            return false;
        }
        catch (SQLException | IOException e)
        {
            e.printStackTrace();
            rollback();
            return false;
        }
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
        try
        {
            PreparedStatement statement = connection.prepareStatement("EXEC addImageForProduct ? ?");
            statement.setInt(1, productID);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Helper.toByteArray(bitmap));
            statement.setBinaryStream(2, inputStream);
            statement.addBatch();

            int[] rs = statement.executeBatch();

            inputStream.close();
            statement.close();
            return rs.length == 1 && rs[0] == 1;
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
            String sql = "{select [DATA] from [PRODUCT_IMAGE] where [ID] = ?}";
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

    public List<ProductBaseDB> getProducts_All()
    {
        List<ProductBaseDB> rs = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from [PRODUCT]"))
        {
            rs = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                ProductBaseDB pd = new ProductBaseDB();
                pd.name = resultSet.getString(3);
                pd.price = resultSet.getInt(4);
                pd.amount = resultSet.getInt(5);
                InputStream inputStream = resultSet.getBinaryStream(6);
                pd.description = resultSet.getString(7);
                pd.images = new ArrayList<>();
                pd.images.add(BitmapFactory.decodeStream(inputStream));
                rs.add(pd);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }
}
