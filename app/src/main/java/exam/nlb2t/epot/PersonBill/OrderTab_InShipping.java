package exam.nlb2t.epot.PersonBill;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.OrderTab;
import exam.nlb2t.epot.ProductDetail.ChooseItemDetailBottomSheet;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class OrderTab_InShipping extends OrderTab {
    Helper.OnSuccessListener onSubmitVertifyBillListener;
    public void setOnSubmitVertifyBillListener(Helper.OnSuccessListener listener)
    {
        this.onSubmitVertifyBillListener = listener;
    }
    @Override
    public List<BillAdapterItemInfo> loadDataFromDB() {
        List<BillAdapterItemInfo> rs = null;
        DBControllerBill db = new DBControllerBill();
        rs = db.getBillCustomer_ByStatus(Authenticator.getCurrentUser().id, BillBaseDB.BillStatus.IN_SHIPPING, lastIndex, step);
        db.closeConnection();

        return rs;
    }

    public OrderTab_InShipping()
    {
        super();
        buttonText = "Đã nhận hàng";
        buttonClickListner = (bill, posi) -> new AlertDialog.Builder(getContext()).setMessage("Bạn xác nhận rằng đã nhận được đơn hàng thành công?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBControllerBill db = new DBControllerBill();
                        db.vertifyReceived(bill.id);
                        db.closeConnection();
                        if(db.hasError() && getView() != null)
                        {
                            Snackbar.make(getView(), db.getErrorMsg(), BaseTransientBottomBar.LENGTH_LONG).show();;
                        }
                        else {
                            bills.remove(posi);
                            recyclerViewAdapter.notifyItemRemoved(posi);
                            if(onSubmitVertifyBillListener != null){onSubmitVertifyBillListener.OnSuccess(null);
                        }
                        dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
