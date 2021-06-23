package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;

import java.util.Comparator;
import java.util.Date;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.ProductAdapterItemInfo;
import exam.nlb2t.epot.singleton.Helper;

public class ProductMyShop{
    public int id; //[PRODUCT].[ID]
    public int categoryID; //[PRODUCT].[CATEGORY_ID]
    public String name; //[PRODUCT].[NAME]
    public int price; //[PRODUCT].[PRICE]
    public int priceOrigin; //[PRODUCT].[PRICE_ORIGIN]
    public int amount; //[PRODUCT].[AMOUNT]
    public int amountSold; //[PRODUCT].[AMOUNT_SOLD]
    public String description; //[PRODUCT].[DETAIL]
    public int imagePrimaryID; //[PRODUCT].[PRIMARY_IMAGE_ID]
    public Date createdDate; //[PRODUCT].[CREATED_DATE]
    public float starAverage; //[PRODUCT].[STAR_AVG]
    public Bitmap imageProduct;

    public int numberLike;

    public Bitmap loadImagePrimary(int targetWidth, int targetHeight)
    {
        Bitmap rs;
        DBControllerProduct db = new DBControllerProduct();
        rs = db.getAvatar_Product(imagePrimaryID, targetWidth, targetHeight);
        db.closeConnection();
        return rs;
    }

    public ProductMyShop()
    {

    }

    public void setData(ProductBaseDB product) {
    }
}
