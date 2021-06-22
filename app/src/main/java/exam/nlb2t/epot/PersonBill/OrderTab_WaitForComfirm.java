package exam.nlb2t.epot.PersonBill;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.OrderTab;
import exam.nlb2t.epot.singleton.Authenticator;

public class OrderTab_WaitForComfirm extends OrderTab {
    @Override
    public List<BillAdapterItemInfo> loadDataFromDB() {
        List<BillAdapterItemInfo> rs = null;
        DBControllerBill db = new DBControllerBill();
        rs = db.getBillCustomer_ByStatus(Authenticator.getCurrentUser().id, BillBaseDB.BillStatus.WAIT_CONFIRM, lastIndex, step);
        db.closeConnection();

        return rs;
    }
}
