package exam.nlb2t.epot.ProductDetail;

import android.graphics.Bitmap;

import java.util.Random;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;

public class ProductBuyInfo{
    public ProductBaseDB product;
    public int Amount;
    public Bitmap imagePrimary;
    public UserBaseDB salerOverview; // Only contains {username, fullname, avatarID}

    public ProductBuyInfo()
    {

    }

    public static ProductBuyInfo loadFromDB(int productID, int amount)
    {
        ProductBuyInfo info = new ProductBuyInfo();
        info.Amount = amount;

        DBControllerProduct db1 = new DBControllerProduct();
        info.product = db1.getProduct(productID);
        db1.closeConnection();

        DBControllerUser db2 = new DBControllerUser();
        info.salerOverview = db2.getUserOverview(info.product.salerID);
        db2.closeConnection();

        return info;
    }

    public ProductBuyInfo(ProductBuyInfo productBuyInfo) {
        this.product = productBuyInfo.product;
        this.Amount = productBuyInfo.Amount;
    }

    public static ProductBuyInfo createRandom(int seek)
    {
        ProductBuyInfo productBuyInfo = new ProductBuyInfo();

        ProductBaseDB product = new ProductBaseDB();
        productBuyInfo.product = product;
        product.id = seek;
        product.name = "Sản phẩm " + product.id;

        UserBaseDB salerOverview= new UserBaseDB();
        productBuyInfo.salerOverview = salerOverview;
        salerOverview.id = seek;
        salerOverview.fullName = "Người bán " + salerOverview.id;

        Random random = new Random(seek);
        productBuyInfo.Amount = 1 + random.nextInt(10 );
        product.price = 10000+1000*(random.nextInt(1000));
        product.amount = productBuyInfo.Amount + random.nextInt(10);
        product.amountSold = 0;
        return productBuyInfo;
    }
}
