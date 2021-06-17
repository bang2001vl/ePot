package exam.nlb2t.epot.Database;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exam.nlb2t.epot.Database.Tables.BillBaseDB;

public class DBControllerBill extends  DatabaseController{
    public boolean addBill(int userID, String address, List<Pair<Integer, Integer>> list)
    {
        boolean rs = false;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("DECLARE @para as [dbo].[BILL_INFOList];");

            for(Pair<Integer, Integer> p: list)
            {
                builder.append("INSERT INTO @para VALUES(?,?);");
            }
            builder.append("EXEC dbo.createBill ?, ?,@para;");

            PreparedStatement statement = connection.prepareStatement(builder.toString());

            int i = 1;
            for(Pair<Integer, Integer> p: list)
            {
                statement.setInt(i, p.first);
                i++;
                statement.setInt(i, p.second);
                i++;
            }
            statement.setInt(i, userID);
            i++;
            statement.setString(i, address);
            statement.addBatch();

            int[] r = statement.executeBatch();

            Log.d("MY_TAG", Arrays.toString(r));
            rs = true;
            commit();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            rollback();
            ErrorMsg = e.getMessage();
        }
        return rs;
    }
    public int getNumberBillbyStatus(int userID, BillBaseDB.BillStatus statusBill) {
        int number = 0;
        try {
            String sql = "SELECT COUNT(*) FROM [BILL] WHERE [USER_ID] = ? and [STATUS] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userID);
            statement.setInt(2, statusBill.getValue());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                number = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return number;
    }

    /**
     * Get Bills overview without detailBill by user id and status of the bill
     * @param userID The id of the user want to get
     * @param statusBill The status of bills want to get, it is nullable variable. The null is determine when want get all bill of user
     * @return List bill overview
     */
    public List<BillBaseDB> getBillsOverviewbyStatus(int userID, @Nullable BillBaseDB.BillStatus statusBill) {
        List<BillBaseDB> rs = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC selectBillsByStatus ?,?");
            statement.setInt(1, userID);
            if (statusBill != null) {
                statement.setInt(2, statusBill.getValue());
            }
            else {
                statement.setNull(2, Types.INTEGER);
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                BillBaseDB bill = new BillBaseDB();

                bill.userID = userID;
                bill.id = resultSet.getInt("ID");
                bill.address = resultSet.getString("ADDRESS");
                bill.createdDate = resultSet.getDate("CREATED_DATE");
                bill.keyBill = resultSet.getString("KEYBILL");

                // if not null, return current status, else return status in the table
                if (statusBill != null) bill.status = statusBill;
                else bill.status = BillBaseDB.BillStatus.values()[resultSet.getInt("STATUS")];

                rs.add(bill);
            }

            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return rs;
    }
}
