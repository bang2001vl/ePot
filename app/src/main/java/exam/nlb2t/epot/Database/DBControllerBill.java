package exam.nlb2t.epot.Database;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import java.security.InvalidParameterException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.PersonBill.BillAdapterItemInfo;
import exam.nlb2t.epot.singleton.Helper;

public class DBControllerBill extends DatabaseController {
    public boolean addBill(int customerID, int priceShip, String address, Map<Integer, List<Pair<Integer, Integer>>> buyMap) {
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

    private boolean addBill(int customerID, int salerID, int priceShip, String address, List<Pair<Integer, Integer>> list) {
        boolean rs = false;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("DECLARE @para as [dbo].[BILL_INFOList];");

            for (Pair<Integer, Integer> p : list) {
                builder.append("INSERT INTO @para VALUES(?,?);");
            }
            builder.append("EXEC dbo.createBill ?, ?, ?, ?,@para;");

            PreparedStatement statement = connection.prepareStatement(builder.toString());

            int i = 1;
            for (Pair<Integer, Integer> p : list) {
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

            Log.d("MY_TAG", r + "");
            statement.close();
            rs = true;
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = e.getMessage();
        }
        return rs;
    }

    public int getNumberBillbyStatus(int salerID, @Nullable BillBaseDB.BillStatus statusBill) {
        int number = 0;
        try {
            String sql;
            if (statusBill != null) {
                sql = "SELECT COUNT(*) FROM [BILL] WHERE [SALER_ID] = ? and [STATUS] = ?";
            } else {
                sql = "SELECT COUNT(*) FROM [BILL] WHERE [SALER_ID] = ?";
            }
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, salerID);
            if (statusBill != null) {
                statement.setInt(2, statusBill.getValue());
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                number = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return number;
    }

    public int[] getAllNumberBill(int salerID) {
        int[] list = new int[4];
        try {
            String sql =
                    "SELECT COUNT(case [STATUS] when 0 then 1 else null end) as [DEFAULT], " +
                            "COUNT(case [STATUS] when 1 then 1 else null end) as [WAIT_CONFIRM], " +
                            "COUNT(case [STATUS] when 2 then 1 else null end) as [IN_SHIPPING], " +
                            "COUNT(case [STATUS] when 3 then 1 else null end) as [SUCCESS]" +
                            "FROM [BILL] WHERE [SALER_ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, salerID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                for (int i = 0; i < 4; i++) {
                    list[i] = resultSet.getInt(i + 1);
                }
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * Get Bills overview without detailBill by saler id and status of the bill
     *
     * @param salerID    The id of the saler want to get
     * @param statusBill The status of bills want to get, it is nullable variable. The null is determine when want get all bill of saler
     * @param offset    The off set is offset of record, equal the start index, it must be positive number, if not, the error wil show
     * @param number    The number record will be get, if it euqual -1, it will take all rest data in database, start from offset
     * @return List bill overview
     */
    public List<BillBaseDB> getBillsOverviewbyStatus(int salerID, @Nullable BillBaseDB.BillStatus statusBill, int offset, int number) {
        if (offset < 0) {
            throw new InvalidParameterException("Giá trị đầu vào của Offset phải lớn hơn 0, hiện tại: " + offset);
        }

        List<BillBaseDB> rs = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC selectBillByStatus ?,?,?,?");
            String getNumberSQL;
            statement.setInt(1, salerID);
            if (statusBill != null) {
                statement.setInt(2, statusBill.getValue());
                getNumberSQL = "SELECT BILL_ID, COUNT(*) as NUMBER FROM BILL_DETAIL WHERE BILL_ID IN (SELECT ID FROM BILL WHERE SALER_ID = ? AND STATUS = ?) GROUP BY BILL_ID";
            } else {
                statement.setNull(2, Types.INTEGER);
                getNumberSQL = "SELECT BILL_ID, COUNT(*) as NUMBER FROM BILL_DETAIL WHERE BILL_ID IN (SELECT ID FROM BILL WHERE SALER_ID = ?) GROUP BY BILL_ID";
            }
            statement.setInt(3, offset);
            statement.setInt(4, number);

            PreparedStatement statement2 = connection.prepareStatement(getNumberSQL);
            statement2.setInt(1, salerID);
            if (statusBill != null) {
                statement2.setInt(2, statusBill.getValue());
            }

            ResultSet resultSet = statement.executeQuery();
            ResultSet resultSet2 = statement2.executeQuery();

            while (resultSet.next() && resultSet2.next()) {
                BillBaseDB bill = new BillBaseDB();

                bill.salerID = salerID;

                bill.id = resultSet.getInt("ID");
                bill.address = resultSet.getString("ADDRESS");
                bill.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp("CREATED_DATE").getTime());
                bill.keyBill = resultSet.getString("KEYBILL");
                bill.total = resultSet.getInt("TOTAL");
                bill.userID = resultSet.getInt("USER_ID");
                bill.setAmountProduct(resultSet2.getInt("NUMBER"));

                // if not null, return current status, else return status in the table
                if (statusBill != null) bill.status = statusBill;
                else bill.status = BillBaseDB.BillStatus.values()[resultSet.getInt("STATUS")];

                rs.add(bill);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return rs;
    }
    public List<BillBaseDB> getBillsOverviewbyStatus(int userID, @Nullable BillBaseDB.BillStatus statusBill) {
        return getBillsOverviewbyStatus(userID, statusBill, 0, -1);
    }

    /**
     * Get Bills overview without detailBill by user id and status of the bill
     *
     * @param userID    The id of the user want to get
     * @param statusBill    The status of bills want to get, it is nullable variable. The null is determine when want get all bill of user
     * @param offset    The off set is offset of record, equal the start index, it must be positive number, if not, the error wil show
     * @param number    The number record will be get, if it euqual -1, it will take all rest data in database, start from offset
     * @return List bill overview
     */
    public List<BillBaseDB> getUserBillsOverviewbyStatus(int userID, @Nullable BillBaseDB.BillStatus statusBill, int offset, int number) {
        if (offset < 0) {
            throw new InvalidParameterException("Giá trị đầu vào của Offset phải lớn hơn 0, hiện tại: " + offset);
        }

        List<BillBaseDB> rs = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC selectUserBillByStatus ?,?,?,?");
            statement.setInt(1, userID);
            if (statusBill != null) {
                statement.setInt(2, statusBill.getValue());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            statement.setInt(3, offset);
            statement.setInt(4, number);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                BillBaseDB bill = new BillBaseDB();

                bill.userID = userID;

                bill.id = resultSet.getInt("ID");
                bill.address = resultSet.getString("ADDRESS");
                bill.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp("CREATED_DATE").getTime());
                bill.keyBill = resultSet.getString("KEYBILL");
                bill.total = resultSet.getInt("TOTAL");
                bill.salerID = resultSet.getInt("SALER_ID");

                // if STATUS not null, return current status, else return status in the table
                if (statusBill != null) bill.status = statusBill;
                else bill.status = BillBaseDB.BillStatus.values()[resultSet.getInt("STATUS")];

                rs.add(bill);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return rs;
    }
    public List<BillBaseDB> getUserBillsOverviewbyStatus(int userID, @Nullable BillBaseDB.BillStatus statusBill) {
        return getUserBillsOverviewbyStatus(userID, statusBill, 0, -1);
    }
    public int getNumberofUserBill(int UserID, BillBaseDB.BillStatus status)
    {
        List<BillBaseDB> bills=getUserBillsOverviewbyStatus(UserID,status);
        return bills.size();
    }

    // Get from start to end. start and end is included
    public List<BillBaseDB> getBillbySaler_Overview(int salerID, int startIndex, int endIndex) {
        List<BillBaseDB> rs = null;
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC getBILL_bySALERID ?, ?, ?;");
            statement.setInt(1, salerID);
            statement.setInt(2, startIndex);
            statement.setInt(3, endIndex);

            ResultSet resultSet = statement.executeQuery();

            rs = new ArrayList<>();
            while (resultSet.next()) {
                BillBaseDB bill = new BillBaseDB();
                bill.id = resultSet.getInt("ID");
                bill.salerID = salerID;
                bill.keyBill = resultSet.getString("KEYBILL");
                bill.total = resultSet.getLong("TOTAL");
                bill.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp("CREATED_DATE").getTime());
                bill.status = BillBaseDB.BillStatus.values()[resultSet.getInt("STATUS")];
                bill.userID = resultSet.getInt("USER_ID");
                bill.address = resultSet.getString("ADDRESS");
                rs.add(bill);
            }

            statement.close();
            resultSet.close();
        } catch (SQLException throwables) {
            ErrorMsg = "FAILED: Cannot get data from server";
            throwables.printStackTrace();
        }
        return rs;
    }

    public void setStatusBill(int billID, BillBaseDB.BillStatus newStatus) {
        try {
            String sql = "UPDATE [BILL] SET STATUS = ? WHERE [ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, newStatus.getValue());
            statement.setInt(2, billID);
            if (statement.executeUpdate() != 1) {
                ErrorMsg = "FAILED: Set status of bill is wrong";
                return;
            }

            commit();
            statement.close();
        } catch (SQLException throwables) {
            ErrorMsg = "FAILED: Cannot tranfer status of bill from server";
            throwables.printStackTrace();
        }
    }

    public int getAmountProductInBill(int billID) {
        int number = 0;
        try {
            String sql = "SELECT COUNT(*) FROM BILL_DETAIL WHERE BILL_ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, billID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                number = resultSet.getInt(1);
            }

            statement.close();
        } catch (SQLException throwables) {
            ErrorMsg = "FAILED: Cannot tranfer status of bill from server";
            throwables.printStackTrace();
        }

        return number;
    }

    public BillBaseDB getBillbyID(int billID) {
        BillBaseDB bill = new BillBaseDB();
        try {
            String sql = "SELECT TOP(1) * FROM BILL WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,billID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                bill.id = resultSet.getInt("ID");
                bill.keyBill = resultSet.getString("KEYBILL");
                bill.status = BillBaseDB.BillStatus.values()[resultSet.getInt("STATUS")];
                bill.address = resultSet.getString("ADDRESS");
                bill.total = resultSet.getInt("TOTAL");
                bill.createdDate = Helper.getDateLocalFromUTC(resultSet.getTimestamp("CREATED_DATE").getTime());
                bill.salerID = resultSet.getInt("SALER_ID");
                bill.userID = resultSet.getByte("USER_ID");
            }
            else {
                throw new SQLException();
            }
        }
        catch (SQLException throwables) {
            ErrorMsg = "FAILED: Cannot find bill by this ID: " + billID;
            throwables.printStackTrace();
            return null;
        }
        return bill;
    }

    public boolean vertifyReceived(int billID)
    {
        boolean rs = false;
        try{
            CallableStatement statement = connection.prepareCall("{call vertifySuccessBill(?)}");
            statement.setInt(1, billID);

            rs = statement.executeUpdate() > 0;
            if(rs){
                commit();
            }
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể lấy dữ liệu từ máy chủ";
        }
        return rs;
    }

    public List<BillAdapterItemInfo> getBillCustomer_ByStatus(int userID, @Nullable BillBaseDB.BillStatus statusBill, int offset, int number)
    {
        List<BillBaseDB> list = getUserBillsOverviewbyStatus(userID, statusBill, offset, number);
        if(list == null){return null;}

        DBControllerUser db = new DBControllerUser();
        List<BillAdapterItemInfo> rs = new ArrayList<>(list.size());
        for(BillBaseDB b: list)
        {
            int amountPD = getAmountProductInBill(b.id);
            UserBaseDB saler = db.getUserOverview(b.salerID);
            rs.add(new BillAdapterItemInfo(b,amountPD, saler));
        }
        db.closeConnection();

        return rs;
    }
}
