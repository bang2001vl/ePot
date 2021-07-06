package exam.nlb2t.epot.Database;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.Tables.NotificationBaseDB;
import exam.nlb2t.epot.NotificationWorkspace.NotifycationInfo;
import exam.nlb2t.epot.singleton.Helper;

public class DBControllerNotification extends DatabaseController{
    public List<NotifycationInfo> getNotidication(int userID, int startIndex, int endIndex)
    {
        List<NotifycationInfo> rs = null;
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC getNotification ?, ?, ?;");
            statement.setInt(1, userID);
            statement.setInt(2, startIndex);
            statement.setInt(3, endIndex);

            ResultSet resultSet = statement.executeQuery();

            rs = new ArrayList<>();
            while (resultSet.next())
            {
                NotificationBaseDB noti = new NotificationBaseDB();
                noti.receiverID = userID;

                int i = 1;
                noti.id = resultSet.getInt(i);i++;
                noti.oldStatus = resultSet.getInt(i);i++;
                noti.newStatus = resultSet.getInt(i);i++;
                noti.hasRead = (resultSet.getInt(i) != 0);i++;
                noti.createdDate = Helper.getDateLocalFromUTC(resultSet.getDate(i));i++;
                noti.billID = resultSet.getInt(i);i++;

                NotifycationInfo info = new NotifycationInfo();
                info.notification = noti;
                info.keyBill = resultSet.getString(i);i++;

                info.salerID = resultSet.getInt(i);i++;

                rs.add(info);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể lấy thông tin từ server";
        }
        return rs;
    }

    public boolean insertNotify_Bill(int receiverID, int billID, int oldStatus, int newStatus)
    {
        boolean rs = false;
        try{
            CallableStatement statement = connection.prepareCall("{call insertNotifyBill(?,?,?,?)}");
            statement.setInt(1, receiverID);
            statement.setInt(2, billID);
            statement.setInt(3, oldStatus);
            statement.setInt(4, newStatus);

            rs = statement.executeUpdate() > 0;
            commit();
            statement.close();
        } catch (SQLException throwables) {
            rollback();
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể đưa dữ liệu vào máy chủ";
        }
        return rs;
    }

    public boolean notifyHasReadNoti(int notificationID){
        boolean rs = false;
        try{
            CallableStatement statement = connection.prepareCall("{call updateHasReadNoti(?)}");
            statement.setInt(1, notificationID);

            rs = statement.executeUpdate() > 0;
            commit();
            statement.close();
        } catch (SQLException throwables) {
            rollback();
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể đưa dữ liệu vào máy chủ";
        }
        return rs;
    }

    public int countUnreadNoti(int userID){
        int rs = -1;
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC countUnreadNoti ?");
            statement.setInt(1, userID);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                rs = resultSet.getInt(1);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể lấy thông tin từ server";
        }
        return rs;
    }
}
