package exam.nlb2t.epot.Database;

import android.util.Log;
import exam.nlb2t.epot.SQL.DataController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

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

    public void closeConnection()
    {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean InsertUser(String phone, String email, String password, String fullname,
                              int gender, String joinday, String birthday, String address,
                              String shopname,  int follower, String info)
    {
        Statement statement = getStatement();
        String sql = "insert into userinfo(phone, email, password, fullname, gender, " +
                "joinday, birthday, address, shopname, follower, info) values" +
                "('%s', '%s', '%s', '%s', %d, '%s', '%s', '%s', '%s', %d, '%s')";
        sql = String.format(Locale.getDefault(), sql, phone, email, password, fullname,
                gender, joinday, birthday, address, shopname, follower, info);
        try{
            statement.execute(sql);
            return true;
        } catch (SQLException throwables) {
            Log.e("MY_ERROR_DATABASE", throwables.getMessage());
            return false;
        }
    }

    public boolean CheckUserExist(String username){
        Statement statement = getStatement();
        String sql = "select * from UserInfo where phone='%s' or email='%s'";
        sql = String.format(Locale.getDefault(), sql, username, username);
        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException throwables) {
            Log.e("MY_ERROR_DATABASE", throwables.getMessage());
        }
        return false;
    }

    public boolean CheckphoneExist(String phone){
        Statement statement = getStatement();
        String sql = "select * from UserInfo where phone='%s';";
        sql = String.format(Locale.getDefault(), sql, phone);
        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException throwables) {
            Log.e("MY_ERROR_DATABASE", throwables.getMessage());
        }
        return false;
    }

    public boolean CheckEmailExist(String email){
        Statement statement = getStatement();
        String sql = "select * from UserInfo where email='%s'";
        sql = String.format(Locale.getDefault(), sql, email);
        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException throwables) {
            Log.e("MY_ERROR_DATABASE", throwables.getMessage());
        }
        return false;
    }

    public boolean CheckPassword(String username, String password){
        Statement statement = getStatement();
        String sql = "select * from UserInfo where (phone='%s' or email='%s') and password = '%s'";
        sql = String.format(Locale.getDefault(), sql, username, username, password);
        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException throwables) {
            Log.e("MY_ERROR_DATABASE", throwables.getMessage());
        }
        return false;
    }
}
