package exam.nlb2t.epot;

import android.graphics.Bitmap;

import exam.nlb2t.epot.Database.Tables.ProductBaseDB;

public class ProductAdapterItemInfo {
    public ProductBaseDB productBaseDB;
    public Bitmap productAvatar;
    public boolean isLiked;
    public int ratingCount;

    public ProductAdapterItemInfo(){

    }

    public ProductAdapterItemInfo(ProductBaseDB productBaseDB, Bitmap productAvatar) {
        this.productBaseDB = productBaseDB;
        this.productAvatar = productAvatar;
    }
}
