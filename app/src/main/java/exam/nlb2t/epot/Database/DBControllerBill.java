package exam.nlb2t.epot.Database;

import android.util.Log;

import androidx.core.util.Pair;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import exam.nlb2t.epot.Database.DatabaseController;

public class DBControllerBill extends DatabaseController {
    public boolean addBill(int customerID, int priceShip, String address, Map<Integer,List<Pair<Integer, Integer>>> buyMap) {
        boolean rs = true;
        for (Map.Entry<Integer, List<Pair<Integer, Integer>>> entry : buyMap.entrySet()) {
            rs = rs && addBill(customerID, entry.getKey(), priceShip, address, entry.getValue());
        }
        if (rs) {
            commit();
        } else {
            rollback();
        }
        return rs;
    }
    private boolean addBill(int customerID, int salerID, int priceShip, String address, List<Pair<Integer, Integer>> list)
    {
        boolean rs = false;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("DECLARE @para as [dbo].[BILL_INFOList];");

            for(Pair<Integer, Integer> p: list)
            {
                builder.append("INSERT INTO @para VALUES(?,?);");
            }
            builder.append("EXEC dbo.createBill ?, ?, ?, ?,@para;");

            PreparedStatement statement = connection.prepareStatement(builder.toString());

            int i = 1;
            for(Pair<Integer, Integer> p: list)
            {
                statement.setInt(i, p.first);
                i++;
                statement.setInt(i, p.second);
                i++;
            }
            statement.setInt(i, customerID);
            i++;
            statement.setInt(i, salerID);
            i++;
            statement.setInt(i, priceShip);
            i++;
            statement.setString(i, address);

            int r = statement.executeUpdate();

            Log.d("MY_TAG", r+"");
            statement.close();
            rs = true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            ErrorMsg = e.getMessage();
        }
        return rs;
    }
}
