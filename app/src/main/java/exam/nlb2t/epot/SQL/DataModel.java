package exam.nlb2t.epot.SQL;


import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import exam.nlb2t.epot.SQL.DataObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressLint("NewApi")

public class DataModel {

    public Connection getConnectionOf() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection objConn = null;
        String ConnURL = null;
        DataObject objEntity = new DataObject(
                "thunder-server-4.southeastasia.cloudapp.azure.com",
                "sa",
                "Bangpro123",
                "FirstDB", "1433");
        try {
            Class.forName(objEntity.getsClass());
            ConnURL = "jdbc:jtds:sqlserver://"
                    + objEntity.getsServerName() + ":" + objEntity.getsPrort() + ";"
                    + "databaseName=" + objEntity.getsDatabase()
                    + ";user=" + objEntity.getsUserId()
                    + ";password=" + objEntity.getsPwd() + ";";
            objConn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return objConn;
    }
}
