package exam.nlb2t.epot.Database.Tables;

import android.graphics.Bitmap;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.ProductAdapterItemInfo;
import exam.nlb2t.epot.singleton.Helper;

public class ProductBaseDB implements Comparable<ProductAdapterItemInfo>{
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
    public Timestamp createdDate; //[PRODUCT].[CREATED_DATE]
    public int deleted; //[PRODUCT].[DELETED]
    public float starAverage; //[PRODUCT].[STAR_AVG]
    public Bitmap imageProduct;

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

    public Bitmap getImagePrimary(int targetWidth, int targetHeight)
    {
        Bitmap rs;
        DBControllerProduct db = new DBControllerProduct();
        rs = db.getAvatar_Product(imagePrimaryID, targetWidth, targetHeight);
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
        createdDate = new Timestamp(System.currentTimeMillis());
        deleted = 0;
    }

    public static Comparator<ProductAdapterItemInfo> sortNameAtoZ = new Comparator<ProductAdapterItemInfo>() {
        @Override
        public int compare(ProductAdapterItemInfo o1, ProductAdapterItemInfo o2) {
            String name1 = Helper.covertToEngString(o1.productBaseDB.name);
            String name2 = Helper.covertToEngString(o2.productBaseDB.name);
            return name1.compareTo(name2);
        }
    };
    public static Comparator<ProductAdapterItemInfo> sortNameZtoA = new Comparator<ProductAdapterItemInfo>() {
        @Override
        public int compare(ProductAdapterItemInfo o1, ProductAdapterItemInfo o2) {
            String name1 = Helper.covertToEngString(o1.productBaseDB.name);
            String name2 = Helper.covertToEngString(o2.productBaseDB.name);
            return name2.compareTo(name1);
        }
    };
    public static Comparator<ProductAdapterItemInfo> sortPriceMin = new Comparator<ProductAdapterItemInfo>() {
        @Override
        public int compare(ProductAdapterItemInfo o1, ProductAdapterItemInfo o2) {
            return Integer.compare(o1.productBaseDB.price, o2.productBaseDB.price);
        }
    };
    public static Comparator<ProductAdapterItemInfo> sortPriceMax = new Comparator<ProductAdapterItemInfo>() {
        @Override
        public int compare(ProductAdapterItemInfo o1, ProductAdapterItemInfo o2) {
            return Integer.compare(o2.productBaseDB.price, o1.productBaseDB.price);
        }
    };
    public static Comparator<ProductAdapterItemInfo> TimeNew = new Comparator<ProductAdapterItemInfo>() {
        @Override
        public int compare(ProductAdapterItemInfo o1, ProductAdapterItemInfo o2) {
            return o2.productBaseDB.createdDate.compareTo( o1.productBaseDB.createdDate);
        }
    };
    @Override
    public int compareTo(ProductAdapterItemInfo o) {
        return 0;
    }
}
