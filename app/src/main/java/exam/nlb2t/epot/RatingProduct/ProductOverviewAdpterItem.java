package exam.nlb2t.epot.RatingProduct;

import android.graphics.Bitmap;

public class ProductOverviewAdpterItem {
    public int productID;
    public String productName;
    public int productAvatarID;
    public Bitmap productAvatar;
    public int salerID;
    public String salerName;

    public ProductOverviewAdpterItem(){

    }

    public ProductOverviewAdpterItem(int productID, String productName, int productAvatarID, int salerID, String salerName) {
        this.productID = productID;
        this.productName = productName;
        this.productAvatarID = productAvatarID;
        this.salerID = salerID;
        this.salerName = salerName;
    }
}
