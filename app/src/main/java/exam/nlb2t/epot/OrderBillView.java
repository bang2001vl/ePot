package exam.nlb2t.epot;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class OrderBillView  {
    public Bitmap shopImage;
    public String tv_shopName;
    public String  tv_Amount;
    public String tv_IDBill;
    public String tv_total;
    public String tv_DateCreate;
    public String tv_Status;

    public OrderBillView() {
    }

    public OrderBillView(Bitmap shopImage, String tv_shopName, String tv_Amount, String tv_IDBill, String tv_total, String tv_DateCreate, String tv_Status) {
        this.shopImage = shopImage;
        this.tv_shopName = tv_shopName;
        this.tv_Amount = tv_Amount;
        this.tv_IDBill = tv_IDBill;
        this.tv_total = tv_total;
        this.tv_DateCreate = tv_DateCreate;
        this.tv_Status = tv_Status;
    }
    public Bitmap getShopImage() {
        return shopImage;
    }

    public String getTv_shopName() {
        return tv_shopName;
    }

    public String getTv_Amount() {
        return tv_Amount;
    }

    public String getTv_IDBill() {
        return tv_IDBill;
    }

    public String getTv_total() {
        return tv_total;
    }

    public String getTv_DateCreate() {
        return tv_DateCreate;
    }

    public String getTv_Status() {
        return tv_Status;
    }


}