package exam.nlb2t.epot.Database.Tables;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerBill;

public class BillBaseDB {
    public enum BillStatus {
        DEFAULT(0), //MEANS: Default
        WAIT_CONFIRM(1), //MEANS: Success payment but not confirm from saler
        IN_SHIPPING(2), //MEANS: Success confirm but buyer not give product
        SUCCESS(3); //MEANS: Success Deal

        private final int value;

        private BillStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }


        @NonNull
        @Override
        public String toString() {
            String rs = "";
            switch (this) {
                case SUCCESS:
                    rs = "Thành công";
                    break;
                case DEFAULT:
                    rs = "Hủy bỏ";
                    break;
                case IN_SHIPPING:
                    rs = "Đang giao";
                    break;
                case WAIT_CONFIRM:
                    rs = "Chờ xác nhận";
                    break;
            }
            return rs;
        }
    }

    public int id; //[BILL].[ID]
    public int userID; //[BILL].[USER_ID]
    public Date createdDate; //[BILL].[CREATED_DATE]
    public String keyBill; //[BILL].[KEYBILL]
    public String address; //[BILL].[ADDRESS]
    public BillStatus status; //[BILL].[STATUS]
    public long total; //[BILL].[TOTAL]
    public int salerID; //[BILL].[SALER_ID]
    public List<ProductBaseDB> productinBill;

    public BillBaseDB() {

    }

    public static int getNumberBill(int userID, BillStatus status) {
        DBControllerBill db = new DBControllerBill();
        int number = db.getNumberBillbyStatus(userID, status);
        db.closeConnection();

        return number;
    }
}
