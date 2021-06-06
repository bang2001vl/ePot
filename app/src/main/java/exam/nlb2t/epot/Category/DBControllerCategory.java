package exam.nlb2t.epot.Category;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.singleton.Helper;

public class DBControllerCategory extends DatabaseController {
    public boolean createCategory(String name, Bitmap image)
    {
        try{
            PreparedStatement statement = connection.prepareStatement("EXEC insertCategory ?, ?");
            byte[] arr = Helper.toByteArray(image, 300,300);
            Log.d("MY_TAG", "IMAGE size = " + arr.length);
            statement.setString(1, name);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(arr);
            statement.setBinaryStream(2, inputStream, arr.length);
            statement.addBatch();

            int[] rs = statement.executeBatch();
            Log.d("MY_TAG", ""+ Arrays.toString(rs));
            statement.close();
            inputStream.close();
            commit();
            return true;
        }
        catch (SQLException | IOException e)
        {
            rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<Pair<String, Bitmap>> getCategoryList()
    {
        List<Pair<String, Bitmap>> rs = new ArrayList<>();
        try{
            String sql = "SELECT [CATEGORY].[NAME], [AVATAR].[DATA] FROM [CATEGORY] LEFT JOIN [AVATAR] ON [CATEGORY].[AVATAR_ID]=[AVATAR].[ID];";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            InputStream inputStream;
            while (resultSet.next())
            {
                String name = resultSet.getString(1);
                inputStream = resultSet.getBinaryStream(2);
                rs.add(new Pair<>(name, BitmapFactory.decodeStream(inputStream)));
                inputStream.close();
            }
            Log.d("MY_TAG", String.format("SUCCESS: Retrive %d category", rs.size()));
            return rs;
        }
        catch (SQLException | IOException e)
        {
            return null;
        }
    }

    public List<String> getCategoryNameList()
    {
        List<String> rs = new ArrayList<>();
        try{
            String sql = "SELECT [CATEGORY].[NAME] FROM [CATEGORY];";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                String name = resultSet.getString(1);
                rs.add(name);
            }
            Log.d("MY_TAG", String.format("SUCCESS: Retrive %d category", rs.size()));
            return rs;
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public static List<String> getCategoryNames()
    {
        DBControllerCategory db = new DBControllerCategory();
        List<String> rs = db.getCategoryNameList();
        db.closeConnection();
        return rs;
    }

    public static List<Pair<String, Bitmap>> getCategories()
    {
        DBControllerCategory db = new DBControllerCategory();
        List<Pair<String, Bitmap>> rs = db.getCategoryList();
        db.closeConnection();
        return rs;
    }
}
