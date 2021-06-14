package exam.nlb2t.epot.ClassInformation;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

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

    public ProductBuyInfo(int productID, int amount)
    {
        this.Amount = amount;
        DBControllerProduct db1 = new DBControllerProduct();
        product = db1.getProduct(productID);
        imagePrimary = db1.getImage_Product(product.imagePrimaryID);
        db1.closeConnection();

        salerOverview = product.getSalerOverview();
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
        return productBuyInfo;
    }
}
