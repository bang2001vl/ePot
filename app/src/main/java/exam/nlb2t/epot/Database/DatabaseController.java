package exam.nlb2t.epot.Database;

import android.util.Log;

import exam.nlb2t.epot.SQL.DataController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {

    private DataController dataController = new DataController();
    private Connection connection;

    public DatabaseController() {
        connection = dataController.ConnnectionData();
    }

    Statement getStatement()
    {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            Log.e("MY_ERROR_DATABASE", throwables.getMessage());
        }
        return statement;
    }

    void closeConnection()
    {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean CheckUserExist(String username){
        Statement statement = getStatement();
        String sql = "select * from UserInfo where username = '" + username + "'";
        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                connection.close();
                return true;
            }
        }
        catch (SQLException e) {
            Log.e("MY_ERROR_DATABASE", e.getMessage());
        }
        closeConnection();
        return false;
    }

    public boolean CheckPassword(String username, String password){
        Statement statement = getStatement();
        String sql = "select * from UserInfo where username = '" + username + "' and password = '"+password+"'";
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next())
                //if (rs.getString("password")==password)
                {
                    connection.close();
                    return true;
                }
        }
        catch (SQLException e) {
            Log.e("MY_ERROR_DATABASE", e.getMessage());
        }
        closeConnection();
        return false;
    }
}
