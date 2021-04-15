package exam.nlb2t.epot.ClassInformation;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.util.Random;

public class BaseProductInfo implements Cloneable{

    /// Base info

    public int id;

    public String productName;
    public int originPrice;
    public int currentPrice;
    public float averageRating;
    public int avaiableAmount;
    public Bitmap mainImage;

    public Saler saler;

    public BaseProductInfo()
    {

    }

    public BaseProductInfo(BaseProductInfo baseProductInfo) {
        this.productName = baseProductInfo.productName;
        this.originPrice = baseProductInfo.originPrice;
        this.currentPrice = baseProductInfo.currentPrice;
        this.averageRating = baseProductInfo.averageRating;
        this.avaiableAmount = baseProductInfo.avaiableAmount;
        this.id = baseProductInfo.id;
        this.mainImage = baseProductInfo.mainImage;
        this.saler = baseProductInfo.saler;
    }

    public static int RANDOM_ID = 0;
    public static BaseProductInfo createRandom(int seek)
    {
        Random random = new Random(seek);
        BaseProductInfo baseProductInfo = new BaseProductInfo();
        baseProductInfo.productName = "Random Name " + seek;

        baseProductInfo.originPrice = 1000*(1+random.nextInt(100));
        baseProductInfo.currentPrice = baseProductInfo.originPrice;
        if(random.nextInt(10)%2 == 0)
        {
            baseProductInfo.currentPrice *= random.nextFloat();
            if(baseProductInfo.currentPrice < 1000)
            {
                baseProductInfo.currentPrice = 1000;
            }
            else
            {
                baseProductInfo.currentPrice = (baseProductInfo.currentPrice/1000)*1000;
            }
        }
        baseProductInfo.averageRating = (seek % 5)/5.0f;
        baseProductInfo.avaiableAmount = 10+random.nextInt(100);
        baseProductInfo.saler = exam.nlb2t.epot.ClassInformation.Saler.createRandom(seek);
        baseProductInfo.id = RANDOM_ID;
        RANDOM_ID++;
        return baseProductInfo;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        BaseProductInfo base = (BaseProductInfo) super.clone();
        base.saler = (Saler) saler.clone();
        if(mainImage != null) {
            base.mainImage = Bitmap.createBitmap(mainImage);
        }
        return base;
    }
}
