package exam.nlb2t.epot.ClassInformation;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import java.util.Random;

public class Product extends BaseProductInfo{

    public String Description;
    public Bitmap[] Images;

    public int numberSold;

    public Product()
    {

    }

    public Product(BaseProductInfo baseProductInfo)
    {
        super(baseProductInfo);
    }

    public Product(Product product)
    {
        super(product);

        this.Description = product.Description;
        this.numberSold = product.numberSold;

        this.saler = product.saler;
        this.Images = product.Images;
    }

    @SuppressLint("DefaultLocale")
    public static Product createRandom(int seek)
    {
        BaseProductInfo base = BaseProductInfo.createRandom(seek);
        Product product = new Product(base);

        StringBuilder builder = new StringBuilder();
        Random random = new Random(seek);
        for(int i = random.nextInt(10); i>=0; i--)
        {
            builder.append(String.format("Default text line number %d", i));
        }


        product.numberSold = random.nextInt(product.avaiableAmount);
        return product;
    }

}
