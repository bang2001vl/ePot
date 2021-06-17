package exam.nlb2t.epot.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.singleton.Authenticator;
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

    public boolean checkExistUsername(String username)
    {
        boolean rs = false;
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT [ID] FROM [USER] WHERE [USERNAME]=?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            rs = resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "FAILED: Cannot execute statement";
        }
        return rs;
    }

    public int insertUser(String username, String password,String phone, String fullname, int gender, int birthdayYear, int birthdayMonth, int birthdayDay)
    {
        int newUserID = -1;
        try
        {
            Authenticator authenticator = new Authenticator();
            byte[] passEncypted = authenticator.encyptPassword(username, password);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(passEncypted);

            Date birthday = Helper.getDateFromLocalToUTC(birthdayYear, birthdayMonth, birthdayDay, 6,0);

            PreparedStatement statement = connection.prepareStatement("EXEC createUser ?,?,?,?,?,?;");
            statement.setString(1, username);
            statement.setBinaryStream(2, inputStream, passEncypted.length);
            statement.setString(3, phone);
            statement.setString(4, fullname);
            statement.setInt(5, gender);
            statement.setDate(6, birthday);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                newUserID = resultSet.getInt(1);
            }

            statement.close();
            inputStream.close();
            resultSet.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            ErrorMsg = "FAILED: Cannot execute statement";
        }
        return newUserID;
    }

    public boolean checkExistPhone(String phone)
    {
        boolean rs = false;
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT [ID] FROM [USER] WHERE [PHONE]= ?");
            statement.setString(1, phone);
            ResultSet resultSet = statement.executeQuery();
            rs = resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "FAILED: Cannot execute statement";
        }
        return rs;
    }

    public boolean UpdatePassword(String phone, String pass)
    {
        try
        {
            Authenticator authenticator = new Authenticator();
            byte[] passEncypted = authenticator.encyptPassword(null, pass);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(passEncypted);

            PreparedStatement statement = connection.prepareStatement("UPDATE [USER] SET [PASSWORD] = ? WHERE [PHONE] = ?");
            statement.setBinaryStream(1, inputStream, passEncypted.length);
            statement.setString(2, phone);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "FAILED: Cannot execute statement";
            return false;
        }
        return true;
    }

    public boolean CheckUserLogin(String username, String pass)
    {
        boolean rs = false;
        try
        {
            Authenticator authenticator = new Authenticator();
            byte[] passEncypted = authenticator.encyptPassword(username, pass);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(passEncypted);

            PreparedStatement statement = connection.prepareStatement("SELECT [ID] FROM [USER] WHERE [USERNAME]= ? AND [PASSWORD] = ?");
            statement.setString(1, username);
            statement.setBinaryStream(2, inputStream, passEncypted.length);
            ResultSet resultSet = statement.executeQuery();
            rs = resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "FAILED: Cannot execute statement";
        }
        return rs;
    }


}
