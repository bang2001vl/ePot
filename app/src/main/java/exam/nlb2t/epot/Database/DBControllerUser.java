package exam.nlb2t.epot.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exam.nlb2t.epot.Database.Tables.UserBaseDB;

public class DBControllerUser extends DatabaseController{
    public Bitmap getAvatar(int avatarID)
    {
        Bitmap rs = null;
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
            e.printStackTrace();
        }
        return rs;
    }
}
