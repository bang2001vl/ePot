package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    public Bitmap getImagePrimary()
    {
        return null;
    }

    public ProductBaseDB()
    {

    }
}
