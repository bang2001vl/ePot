package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.DBControllerUser;

public class ProductBaseDB {
    public int id; //[PRODUCT].[ID]
    public int salerID; //[PRODUCT].[SALER_ID]
    public int categoryID; //[PRODUCT].[CATEGORY_ID]
    public String name; //[PRODUCT].[NAME]
    public int price; //[PRODUCT].[PRICE]
    public int priceOrigin; //[PRODUCT].[PRICE_ORIGIN]
    public int amount; //[PRODUCT].[AMOUNT]
    public int amountSold; //[PRODUCT].[AMOUNT_SOLD]
    public String description; //[PRODUCT].[DETAIL]
    public int imagePrimaryID; //[PRODUCT].[PRIMARY_IMAGE_ID]
    public Date createdDate; //[PRODUCT].[CREATED_DATE]
    public int deleted; //[PRODUCT].[DELETED]

    public UserBaseDB getSaler()
    {
        return null;
    }

    public UserBaseDB getSalerOverview()
    {
        UserBaseDB salerOverview;
        DBControllerUser db = new DBControllerUser();
        salerOverview = db.getUserOverview(salerID);
        db.closeConnection();
        return  salerOverview;
    }

    public Bitmap getImagePrimary()
    {
        Bitmap rs;
        DBControllerProduct db = new DBControllerProduct();
        rs = db.getImage_Product(imagePrimaryID);
        db.closeConnection();
        return rs;
    }

    public ProductBaseDB()
    {

    }
}
