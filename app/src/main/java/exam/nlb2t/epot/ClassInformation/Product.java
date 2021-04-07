package exam.nlb2t.epot.ClassInformation;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import java.util.Random;

public class Product extends BaseProductInfo{

    public String Description;
    public Bitmap[] Images;
    public Saler Saler;

    @SuppressLint("DefaultLocale")
    public static Product createRandom(int seek)
    {
        BaseProductInfo base = BaseProductInfo.createRandom(seek);
        Product product = new Product();
        product.ProductName = base.ProductName;
        product.OriginPrice = base.OriginPrice;
        product.CurrentPrice = base.CurrentPrice;
        product.AverageRating = base.AverageRating;
        product.AvaiableAmount = base.AvaiableAmount;
        StringBuilder builder = new StringBuilder();
        Random random = new Random(seek);
        for(int i = random.nextInt(10); i>=0; i--)
        {
            builder.append(String.format("Default text linr number %1$d", i));
        }
        product.Saler = exam.nlb2t.epot.ClassInformation.Saler.createRandom(seek);
        return product;
    }
}
