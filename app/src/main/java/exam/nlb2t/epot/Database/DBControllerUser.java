package exam.nlb2t.epot.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
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
                if(inputStream != null) {
                    rs = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
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

    public int insertUser(String username, String password,String phone, String email, String fullname,
                          int gender, int birthdayYear, int birthdayMonth, int birthdayDay)
    {
        int newUserID = -1;
        try
        {
            Authenticator authenticator = new Authenticator();
            byte[] passEncypted = authenticator.encyptPassword(username, password);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(passEncypted);

            Date birthday = Helper.getDateFromLocalToUTC(birthdayYear, birthdayMonth, birthdayDay, 6,0);

            PreparedStatement statement = connection.prepareStatement("EXEC createUser ?,?,?,?,?,?,?;");
            statement.setString(1, username);
            statement.setBinaryStream(2, inputStream, passEncypted.length);
            statement.setString(3, phone);
            statement.setString(4, email);
            statement.setString(5, fullname);
            statement.setInt(6, gender);
            statement.setDate(7, birthday);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                newUserID = resultSet.getInt(1);
            }

            statement.close();
            inputStream.close();
            resultSet.close();
            commit();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            ErrorMsg = "FAILED: Cannot execute statement";
            rollback();
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
            statement.close();
            inputStream.close();
            commit();

        } catch (SQLException | IOException e) {
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


    public int findUserID(String username, String pass)
    {
        int rs = -1;
        try
        {
            Authenticator authenticator = new Authenticator();
            byte[] passEncypted = authenticator.encyptPassword(username, pass);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(passEncypted);
            PreparedStatement statement = connection.prepareStatement("SELECT [ID] FROM [USER] WHERE [USERNAME]= ? AND [PASSWORD] = ?");
            statement.setString(1, username);
            statement.setBinaryStream(2, inputStream, passEncypted.length);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                rs = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "FAILED: Cannot execute statement";
        }
        return rs;
    }

    public boolean updateUser(UserBaseDB userBaseDB)
    {
        boolean rs = false;
        try
        {
            PreparedStatement statement = connection.prepareStatement("UPDATE [USER] SET [ADDRESS] = ? WHERE [ID] = ?;");
            statement.setString(1, userBaseDB.address);
            statement.setInt(2, userBaseDB.id);

            rs = statement.executeUpdate() > 0;
            statement.close();
            commit();

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể ghi thông tin vào máy chủ";
        }
        return rs;
    }

    public boolean updateAddress(int userID, String receiverName, String receiverPhone, String address_detail, String province)
    {
        boolean rs = false;
        try
        {
            UserBaseDB userBaseDB = new UserBaseDB();
            userBaseDB.setAddress(receiverName, receiverPhone, address_detail, province);

            PreparedStatement statement = connection.prepareStatement("UPDATE [USER] SET [ADDRESS] = ? WHERE [ID] = ?;");
            statement.setString(1, userBaseDB.address);
            statement.setInt(2, userID);

            rs = statement.executeUpdate() > 0;
            statement.close();
            commit();

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể ghi thông tin vào máy chủ";
        }
        return rs;
    }

    public boolean updateUser(int userID, String fullName, int gender, int birthDay_year, int birthDay_month, int birthDay_day)
    {
        boolean rs = false;
        try
        {
            PreparedStatement statement = connection.prepareStatement("UPDATE [USER] SET [FULL_NAME]=?,[GENDER]=?,[BIRTHDAY]=? WHERE [ID] = ?;");
            statement.setString(1, fullName);
            statement.setInt(2, gender);
            statement.setDate(3, Helper.getDateFromLocalToUTC(birthDay_year, birthDay_month, birthDay_day, 6, 0));
            statement.setInt(4, userID);

            rs = statement.executeUpdate() > 0;
            statement.close();
            commit();

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể ghi vào máy chủ";
        }
        return rs;
    }

    public boolean updateUser_Avatar(int userID, int avatarID, Bitmap avatar)
    {
        boolean rs = false;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Helper.toByteArray(avatar, MEDIUM_SIZE_IMAGES_IN_PIXEL, MEDIUM_SIZE_IMAGES_IN_PIXEL));

            CallableStatement statement = connection.prepareCall("{call updateUserAvatar(?,?,?)}");
            statement.setInt(1, userID);
            statement.setInt(2, avatarID);
            statement.setBinaryStream(3, inputStream);

            rs = statement.executeUpdate() > 0;
            if(rs) {
                commit();
            }
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể ghi thông tin vào máy chủ";
        }
        return rs;
    }

    public int findUserID_ByEmail(String email)
    {
        int rs = -1;
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT [ID] FROM [USER] WHERE [EMAIL]= ?;");
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                rs = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể đọc thông tin từ máy chủ";
        }
        return rs;
    }
    public int CheckExitsemail(String email)
    {
        int rs = -1;
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT [ID] FROM [USER] WHERE [EMAIL]=?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                rs = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMsg = "FAILED: Cannot execute statement";
        }
        return rs;
    }
}
