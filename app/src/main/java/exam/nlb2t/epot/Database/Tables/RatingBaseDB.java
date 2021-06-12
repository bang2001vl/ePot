package exam.nlb2t.epot.Database.Tables;

import java.text.DateFormat;
import java.util.Date;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.singleton.Helper;

public class RatingBaseDB {
    public int id;
    public int productId;
    public int userId;
    public int star;
    public String comment;
    public Date createdDate;

    public UserBaseDB getUserOverview()
    {
        UserBaseDB rs;
        DBControllerUser db = new DBControllerUser();
        rs = db.getUserOverview(userId);
        db.closeConnection();
        return rs;
    }

    public String getDateString()
    {
        DateFormat format = Helper.getDateFormat();
        return format.format(createdDate);
    }
}
