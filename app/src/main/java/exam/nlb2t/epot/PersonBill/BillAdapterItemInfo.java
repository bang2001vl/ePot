package exam.nlb2t.epot.PersonBill;

import android.graphics.Bitmap;

import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;

public class BillAdapterItemInfo {
    public BillBaseDB billOverview;
    public Bitmap salerAvatar;
    public UserBaseDB salerOverview;
    public int amountProduct;

    public BillAdapterItemInfo(BillBaseDB billOverview, int amountProduct, UserBaseDB salerOverview) {
        this.billOverview = billOverview;
        this.amountProduct = amountProduct;
        this.salerOverview = salerOverview;
    }
}
