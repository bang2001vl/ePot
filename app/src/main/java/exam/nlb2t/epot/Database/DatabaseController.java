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

    public boolean CheckUserExist(String username) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select * from UserInfo where username = '" + username + "'";
        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                connection.close();
                return true;
            }
        }
        catch (SQLException e) {
            Log.d("MY_DEBUG", e.getMessage());
        }
        connection.close();
        return false;
    }

    public boolean CheckPassword(String username, String password) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select * from UserInfo where username = '" + username + "' and password = '"+password+"'";
        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                connection.close();
                return true;
            }
        }
        catch (SQLException e) {
            Log.d("MY_DEBUG", e.getMessage());
        }
        connection.close();
        return false;
    }
}
