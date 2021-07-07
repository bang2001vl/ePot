package exam.nlb2t.epot.Database;

import android.app.AlertDialog;
import android.app.Application;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.SQLException;

import exam.nlb2t.epot.SQL.DataController;

public class DatabaseController {

    public boolean noConnection;

    protected String ErrorMsg = null;
    public String getErrorMsg(){return ErrorMsg;}
    public boolean hasError(){return ErrorMsg != null;}
    protected DataController dataController = new DataController();
    protected Connection connection;

    public DatabaseController(){
        try{
            connection = dataController.ConnnectionData();
            if(connection == null){
                ErrorMsg = "No connection";
            }
            else {
                connection.setAutoCommit(false);
            }
        }
        catch (SQLException e)
        {
            ErrorMsg = "LỖI: Không thể kết nối đến server";
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
            ErrorMsg = "LỖI: Commit SQLStatement thất bại";
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
            ErrorMsg = "LỖI: Rollback SQLStatement thất bại";
            e.printStackTrace();
        }
    }

    public void closeConnection()
    {
        try {
            connection.close();
        } catch (SQLException e) {
            Log.e("MY_TAG", "ERROR: Failed to close connection");
            ErrorMsg = "LỖI: Đóng kết nối thất bại";
            e.printStackTrace();
        }
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

    public static final int MAX_BYTE_IMAGE = 10000000;
    public static final int BIG_SIZE_PRODUCT_IMAGES_IN_PIXEL = 700;
    public static final int MEDIUM_SIZE_IMAGES_IN_PIXEL = 200;
}
