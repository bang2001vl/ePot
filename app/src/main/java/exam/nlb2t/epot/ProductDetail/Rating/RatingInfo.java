package exam.nlb2t.epot.ProductDetail.Rating;

import android.graphics.Bitmap;

import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;

public class RatingInfo {
    public RatingBaseDB rating;
    public UserBaseDB userOverview;
    public Bitmap userAvatar;

    public RatingInfo()
    {

    }

    public RatingInfo(RatingBaseDB rating, UserBaseDB userOverview) {
        this.rating = rating;
        this.userOverview = userOverview;
    }
}
