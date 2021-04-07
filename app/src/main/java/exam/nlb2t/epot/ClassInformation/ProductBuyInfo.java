package exam.nlb2t.epot.ClassInformation;

import java.util.Random;

public class ProductBuyInfo {
    public Product product;
    public int Amount;

    public static ProductBuyInfo createRandom(int seek)
    {
        ProductBuyInfo productBuyInfo = new ProductBuyInfo();
        productBuyInfo.product = Product.createRandom(seek);
        Random random = new Random(seek);
        productBuyInfo.Amount = random.nextInt(100);
        return productBuyInfo;
    }
}
