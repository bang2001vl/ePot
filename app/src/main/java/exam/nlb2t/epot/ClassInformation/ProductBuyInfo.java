package exam.nlb2t.epot.ClassInformation;

import androidx.annotation.NonNull;

import java.util.Random;

public class ProductBuyInfo implements Cloneable{
    public BaseProductInfo product;
    public int Amount;

    public ProductBuyInfo()
    {

    }

    public ProductBuyInfo(ProductBuyInfo productBuyInfo) {
        this.product = productBuyInfo.product;
        this.Amount = productBuyInfo.Amount;
    }

    public static ProductBuyInfo createRandom(int seek)
    {
        ProductBuyInfo productBuyInfo = new ProductBuyInfo();
        productBuyInfo.product = Product.createRandom(seek);
        Random random = new Random(seek);
        productBuyInfo.Amount = 1 + random.nextInt(10 );
        return productBuyInfo;
    }

    @NonNull
    @Override
    protected ProductBuyInfo clone() throws CloneNotSupportedException {
        ProductBuyInfo productBuyInfo = (ProductBuyInfo) super.clone();

        productBuyInfo.product = (BaseProductInfo) this.product.clone();

        return productBuyInfo;
    }
}
