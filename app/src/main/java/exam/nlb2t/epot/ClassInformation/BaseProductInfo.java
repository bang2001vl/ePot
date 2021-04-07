package exam.nlb2t.epot.ClassInformation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class BaseProductInfo {
    /// Base info
    public String ProductName;
    public int OriginPrice;
    public int CurrentPrice;
    public float AverageRating;
    public int AvaiableAmount;
    public Bitmap MainImage;

    public static BaseProductInfo createRandom(int seek)
    {
        BaseProductInfo baseProductInfo = new BaseProductInfo();
        baseProductInfo.ProductName = "Random Name " + seek;
        baseProductInfo.OriginPrice = 1000*seek;
        baseProductInfo.CurrentPrice = baseProductInfo.OriginPrice;
        baseProductInfo.AverageRating = (seek % 5)/5.0f;
        baseProductInfo.AvaiableAmount = 100;
        return baseProductInfo;
    }
}
