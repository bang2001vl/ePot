package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.DatabaseController;

public class ProductBaseDB implements Comparable<ProductBaseDB>{
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
    public float starAverage; //[PRODUCT].[STAR_AVG]

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
        rs = db.getAvatar_Product(imagePrimaryID);
        db.closeConnection();
        return rs;
    }

    public ProductBaseDB()
    {

    }

    public int getDiscount()
    {
        return (price*100/priceOrigin);
    }

    public int getNumberLike() {
        int rs;
        DBControllerProduct db = new DBControllerProduct();
        rs = db.getNumberLikeProduct(this.id);
        return rs;
    }
    public void setData(ProductBaseDB product) {
    }

    public int getTotalCmt()
    {
        int rs = 0;
        DBControllerProduct db = new DBControllerProduct();
        rs = db.getCountRating(this.id);
        return rs;
    }

    public void testData()
    {
        id = 1;
        salerID = 1;
        categoryID = 0;
        name = "Product";
        price = 10000;
        priceOrigin = 15000;
        amount = 100;
        amountSold = 1;
        description = "description";
        imagePrimaryID = 3;
        createdDate = new Date(System.currentTimeMillis());
        deleted = 0;
    }

    public static Comparator<ProductBaseDB> sortNameAtoZ = new Comparator<ProductBaseDB>() {
        @Override
        public int compare(ProductBaseDB o1, ProductBaseDB o2) {
            return o1.name.compareTo(o2.name);
        }
    };


    @Override
    public int compareTo(ProductBaseDB o) {
        return this.createdDate.compareTo(o.createdDate);
    }

    public static Comparator<ProductBaseDB> sortNameZtoA = new Comparator<ProductBaseDB>() {
        @Override
        public int compare(ProductBaseDB o1, ProductBaseDB o2) {
            return o2.name.compareTo(o1.name);
        }
    };public static Comparator<ProductBaseDB> sortPriceMin = new Comparator<ProductBaseDB>() {
        @Override
        public int compare(ProductBaseDB o1, ProductBaseDB o2) {
            return o1.name.compareTo(o2.name);
        }
    };public static Comparator<ProductBaseDB> sortPriceMax = new Comparator<ProductBaseDB>() {
        @Override
        public int compare(ProductBaseDB o1, ProductBaseDB o2) {
            return o2.name.compareTo(o1.name);
        }
    };
}
