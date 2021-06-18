package exam.nlb2t.epot.Database.Tables;

import java.util.Date;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerBill;

public class BillBaseDB {
    public enum BillStatus {
        DEFAULT(1), //MEANS: Default
        WAIT_CONFIRM(2), //MEANS: Success payment but not confirm from saler
        IN_SHIPPING(3), //MEANS: Success confirm but buyer not give product
        SUCCESS(4); //MEANS: Success Deal

        private final int value;

        private BillStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public int id; //[BILL].[ID]
    public int userID; //[BILL].[USER_ID]
    public Date createdDate; //[BILL].[CREATED_DATE]
    public String keyBill; //[BILL].[KEYBILL]
    public String address; //[BILL].[ADDRESS]
    public BillStatus status; //[BILL].[STATUS]
    public long total; //[BILL].[TOTAL]
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
