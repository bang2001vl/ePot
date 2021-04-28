package exam.nlb2t.epot.Database;

import android.util.Log;

import net.sourceforge.jtds.jdbc.DateTime;

import exam.nlb2t.epot.ClassInformation.User;
import exam.nlb2t.epot.SQL.DataController;

import java.sql.Connection;
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

    public boolean InsertUser(int idaccount, String name, String birthday, String address,
                              String email, String phone, String username, String password, int gender,
                              String shopname, String joinday, int follower, String info)
    {
        Statement statement = getStatement();
        String sql = "insert into userinfo(idaccount, name, birthday, address, email," +
                "phone, username, password, gender, shopname, joinday, follower, info) values" +
                "(%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d, '%s', '%s', %d, '%s')";
        sql = String.format(Locale.getDefault(), sql, idaccount, name, birthday, address, email, phone, username, password,
                gender, shopname, joinday, follower, info);
        try{
            statement.execute(sql);
            return true;
        } catch (SQLException throwables) {
            Log.e("MY_ERROR_DATABASE", throwables.getMessage());
            return false;
        }
    }

    public boolean InsertProduct(int idproduct, int idshop, String nameproduct, String producer, String dateofmanufacture,
                                 String expiredate, int total, int amountsold, int value, String genre, String placeofsale,
                                 String origin, String description)
    {
        Statement statement = getStatement();
        String sql = "insert into userinfo(idproduct,idshop,nameproduct,producer,dateofmanufacture," +
                "expiredate,total,amountsold,value,genre,placeofsale,origin,description) values" +
                "(%d, %d, '%s', '%s', '%s', '%s', %d, %d, %d, '%s', '%s', '%s', '%s')";
        sql = String.format(Locale.getDefault(), sql, idproduct, idshop, nameproduct, producer, dateofmanufacture,
            expiredate, total, amountsold, value, genre, placeofsale,
            origin, description);
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
