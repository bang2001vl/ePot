package exam.nlb2t.epot.ClassInformation;

public class ProductInfo extends exam.nlb2t.epot.ClassData.ProductInfo{
    public static ProductInfo random()
    {
        ProductInfo rs = new ProductInfo();
        rs.setNameProduct("RANDOM PRODUCT");
        rs.setValue(1000L);
        rs.setTotal(1000L);
        rs.setAmountSold(200L);

        return rs;
    }
}
