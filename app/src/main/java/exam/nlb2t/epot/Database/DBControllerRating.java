package exam.nlb2t.epot.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.Tables.RatingBaseDB;

public class DBControllerRating extends DatabaseController{
    public boolean insertRating(int productID, int userID, int star, String comment)
    {
        boolean rs =false;
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC insertRating ?,?,?,?;");
            statement.setInt(1, productID);
            statement.setInt(2, userID);
            statement.setInt(3, star);
            statement.setString(4, comment);

            rs = statement.executeUpdate() > 0;
            commit();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "FAILED: Cannot write data to database";
            rollback();
        }
        return rs;
    }

    public boolean updateRating(int ratingID, int productID, int star, String comment)
    {
        boolean rs =false;
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC updateRating ?,?,?,?;");
            statement.setInt(1, ratingID);
            statement.setInt(2, productID);
            statement.setInt(3, star);
            statement.setString(4, comment);

            rs = statement.executeUpdate() > 0;
            commit();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "FAILED: Cannot write data to database";
            rollback();
        }
        return rs;
    }

    public boolean deleteRating(int productID)
    {
        boolean rs =false;
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC deleteRating ?;");
            statement.setInt(1, productID);

            rs = statement.executeUpdate() > 0;
            commit();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "FAILED: Cannot write data to database";
            rollback();
        }
        return rs;
    }

    public List<RatingBaseDB> getRating_ByProduct(int productID, int startIndex, int endIndex)
    {
        List<RatingBaseDB> rs = null;
        try
        {
            PreparedStatement statement = connection.prepareStatement("EXEC [dbo].[getRating_ByProduct] ?,?,?;;");
            statement.setInt(1, productID);
            statement.setInt(2, startIndex);
            statement.setInt(3, endIndex);

            ResultSet resultSet = statement.executeQuery();
            rs = new ArrayList<>();
            while (resultSet.next())
            {
                RatingBaseDB rating = new RatingBaseDB(
                        resultSet.getInt(1),
                        productID,
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        null
                );
                rs.add(rating);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "FAILED: Cannot get data from server";
        }
        return rs;
    }

    public List<RatingBaseDB> getRating_ByUser(int userID, int startIndex, int endIndex)
    {
        List<RatingBaseDB> rs = null;
        try
        {
            PreparedStatement statement = connection.prepareStatement("EXEC [dbo].[getRating_ByUser] ?,?,?;;");
            statement.setInt(1, userID);
            statement.setInt(2, startIndex);
            statement.setInt(3, endIndex);

            ResultSet resultSet = statement.executeQuery();
            rs = new ArrayList<>();
            while (resultSet.next())
            {
                RatingBaseDB rating = new RatingBaseDB(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        userID,
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        null
                );
                rs.add(rating);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "FAILED: Cannot get data from server";
        }
        return rs;
    }
}
