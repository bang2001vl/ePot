package exam.nlb2t.epot.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.singleton.Helper;

public class DBControllerUser extends DatabaseController{
    public Bitmap getAvatar(int avatarID)
    {
        Bitmap rs = null;
        if(avatarID == -1) return null;
        try
        {
            String sql = "select [DATA] from [AVATAR] where [ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, avatarID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                InputStream inputStream = resultSet.getBinaryStream(1);
                rs = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException | IOException e)
        {
            ErrorMsg = "LỖI: Câu lệnh SQL không đúng";
            e.printStackTrace();
        }
        return rs;
    }

    public UserBaseDB getUserOverview(int userID)
    {
        UserBaseDB rs = null;
        try
        {
            String sql = "select [USERNAME],[FULL_NAME],[AVATAR_ID] from [USER] where [ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                rs = new UserBaseDB();
                rs.id = userID;
                rs.username = resultSet.getString(1);
                rs.fullName = resultSet.getString(2);
                rs.avatarID = resultSet.getInt(3);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            ErrorMsg = "LỖI: Câu lệnh SQL không đúng";
            e.printStackTrace();
        }
        return rs;
    }

    // Get information of user WITHOUT password
    public UserBaseDB getUserInfo(int userID)
    {
        UserBaseDB rs = null;
        try
        {
            String sql = "select [USERNAME],[FULL_NAME],[AVATAR_ID], [ADDRESS], [GENDER], [BIRTHDAY] from [USER] where [ID] = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                rs = new UserBaseDB();
                rs.id = userID;
                rs.username = resultSet.getString(1);
                rs.fullName = resultSet.getString(2);
                rs.avatarID = resultSet.getInt(3);
                rs.address = resultSet.getString(4);
                rs.gender = resultSet.getInt(5);
                rs.birthday = Helper.getDateLocalFromUTC(resultSet.getDate(6));
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e)
        {
            ErrorMsg = "LỖI: Câu lệnh SQL không đúng";
            e.printStackTrace();
        }
        return rs;
    }
}
