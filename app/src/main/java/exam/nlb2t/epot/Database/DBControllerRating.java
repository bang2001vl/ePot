package exam.nlb2t.epot.Database;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.ProductDetail.Rating.RatingInfo;
import exam.nlb2t.epot.RatingProduct.ProductOverviewAdpterItem;
import exam.nlb2t.epot.singleton.Helper;

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
            if(rs)
            commit();
            else rollback();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể viết dữ liệu vào server";
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
            ErrorMsg = "THẤT BẠI: Không thể viết dữ liệu vào server";
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
            ErrorMsg = "THẤT BẠI: Không thể viết dữ liệu vào server";
            rollback();
        }
        return rs;
    }

    public List<RatingBaseDB> getRating_ByProduct(int productID, int startIndex, int endIndex)
    {
        List<RatingBaseDB> rs = null;
        try
        {
            PreparedStatement statement = connection.prepareStatement("EXEC [dbo].[getRating_ByProduct] ?,?,?;");
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
                        Helper.getDateLocalFromUTC(resultSet.getDate(5))
                );
                rs.add(rating);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể lấy dữ liệu từ server";
        }
        return rs;
    }

    public List<RatingInfo> getRatingInfo_ByProduct(int productID, int startIndex, int endIndex) {
        List<RatingInfo> rs = null;

        List<RatingBaseDB> ratings = getRating_ByProduct(productID, startIndex, endIndex);
        if(ratings != null) {
            rs = new ArrayList<>(ratings.size());
            DBControllerUser db = new DBControllerUser();
            for (RatingBaseDB rating : ratings) {
                rs.add(new RatingInfo(rating, db.getUserOverview(rating.userId)));
            }
            db.closeConnection();
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
                        Helper.getDateLocalFromUTC(resultSet.getDate(5))
                );
                rs.add(rating);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể lấy dữ liệu từ server";
        }
        return rs;
    }

    public RatingBaseDB getRating(int userID, int productID)
    {
        RatingBaseDB rs = null;
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT [ID],[STAR],[COMMENT],[CREATED_TIME] FROM [RATING] WHERE [USER_ID]=? AND [PRODUCT_ID]=?;");
            statement.setInt(1, userID);
            statement.setInt(2, productID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
            {
                rs = new RatingBaseDB(
                        resultSet.getInt(1),
                        productID,
                        userID,
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        Helper.getDateLocalFromUTC(resultSet.getDate(4))
                );
            }

            resultSet.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể lấy dữ liệu từ server";
        }
        return rs;
    }

    public int[] getRatingStar(int productID)
    {
        int[] rs = null;
        try {
            PreparedStatement statement = connection.prepareStatement("EXEC getRatingOverview ?;");
            statement.setInt(1, productID);

            ResultSet resultSet = statement.executeQuery();

            rs = new int[]{0,0,0,0,0};
            if(resultSet.next())
            {
                rs[0] = resultSet.getInt(1);
                rs[1] = resultSet.getInt(2);
                rs[2] = resultSet.getInt(3);
                rs[3] = resultSet.getInt(4);
                rs[4] = resultSet.getInt(5);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể lấy dữ liệu từ server";
        }
        return rs;
    }

    public List<ProductOverviewAdpterItem> getUserNotRatingPD(int userID, int star, int end)
    {
        List<ProductOverviewAdpterItem> rs = null;
        try
        {
            CallableStatement statement = connection.prepareCall("{call getProductNotRating(?,?,?)}");
            statement.setInt(1, userID);
            statement.setInt(2, star);
            statement.setInt(3, end);

            ResultSet resultSet = statement.executeQuery();
            rs = new ArrayList<>();
            while (resultSet.next())
            {
                rs.add(new ProductOverviewAdpterItem(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getString(5)
                ));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể lấy dữ liệu từ server";
        }
        return rs;
    }

    public List<ProductOverviewAdpterItem> getUserAlreadyRatingPD(int userID, int star, int end)
    {
        List<ProductOverviewAdpterItem> rs = null;
        try
        {
            CallableStatement statement = connection.prepareCall("{call getProductAlreadyRating(?,?,?)}");
            statement.setInt(1, userID);
            statement.setInt(2, star);
            statement.setInt(3, end);

            ResultSet resultSet = statement.executeQuery();
            rs = new ArrayList<>();
            while (resultSet.next())
            {
                rs.add(new ProductOverviewAdpterItem(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getString(5)
                ));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ErrorMsg = "THẤT BẠI: Không thể lấy dữ liệu từ server";
        }
        return rs;
    }
}
