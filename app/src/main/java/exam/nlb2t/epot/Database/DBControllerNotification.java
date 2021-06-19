package exam.nlb2t.epot.Database;

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
                noti.hasRead = resultSet.getBoolean(i);i++;
                noti.createdDate = Helper.getDateLocalFromUTC(resultSet.getDate(i));i++;
                noti.billID = resultSet.getInt(i);i++;

                NotifycationInfo info = new NotifycationInfo();
                info.notification = noti;
                info.keyBill = resultSet.getString(i);

                rs.add(info);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "FAILED: Cannot read from server";
        }
        return rs;
    }
}
