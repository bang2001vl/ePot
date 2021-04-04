package exam.nlb2t.epot.Database;

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

    public boolean CheckUserExist(String username) throws SQLException {
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "select * from UserInfo where username = '" + username + "'";
        ResultSet rs = statement.executeQuery(sql);

        connection.close();

        if (rs.first()) return true;
        return false;
    }

    public boolean CheckPassword(String username, String password) throws SQLException {
        Statement statement = connection.createStatement();// Tạo đối tượng Statement.
        String sql = "select password from UserInfo where username = '" + username + "'";
        ResultSet rs = statement.executeQuery(sql);

        connection.close();

        if (rs.getString("password") == password) return true;
        else return false;
    }
}
