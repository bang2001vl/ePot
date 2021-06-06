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

    protected DataController dataController = new DataController();
    protected Connection connection;

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

    public void commit()
    {
        try {
            connection.commit();
        }
        catch (SQLException e)
        {
            Log.e("MY_TAG", "ERROR: Failed to commit connection");
            e.printStackTrace();
        }
    }

    public void rollback()
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

    public static final int MAX_BYTE_IMAGE = 2000000;
    public static final int BIG_SIZE_PRODUCT_IMAGES_IN_PIXEL = 700;
}
