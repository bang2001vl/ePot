package exam.nlb2t.epot.MyShop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import exam.nlb2t.epot.BillRecyclerViewAdapter;
import exam.nlb2t.epot.ClassInformation.User;
import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.singleton.Authenticator;

public class Bill_TabAdapter extends BillRecyclerViewAdapter{
    BillBaseDB.BillStatus status;

    public Bill_TabAdapter(List<BillBaseDB> listBill, BillBaseDB.BillStatus status) {
        super();
        this.status = status;
        this.billList = listBill;
    }

    @NonNull
    @Override
    public BillRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        BillRecyclerViewAdapter.ViewHolder holder;

        if (status != BillBaseDB.BillStatus.WAIT_CONFIRM) {
            holder = new BillRecyclerViewAdapter.ViewHolder(inflater.inflate(R.layout.sample_order_bill_view, parent, false));
        } else {
            holder = new EditProductViewHolder(inflater.inflate(R.layout.sample_order_bill_confirm_view, parent, false));
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BillRecyclerViewAdapter.ViewHolder holder, int position) {
        BillBaseDB bill = billList.get(position);
        setBillInfor(holder, bill);
        setShopInfor(holder, Authenticator.getCurrentUser());
        setEventHandler(holder);

        if (holder instanceof EditProductViewHolder) {
            ((EditProductViewHolder) holder).getBtnConfirm().setOnClickListener(v -> onBtnConfirmClick(holder));
            ((EditProductViewHolder) holder).getBtnCancel().setOnClickListener(v -> onBtnCancelClick(holder));
        }
    }

    private void onBtnCancelClick(RecyclerView.ViewHolder holder) {
        //TODO: Cancel the bill
        changeStatus(holder.getAbsoluteAdapterPosition(), BillBaseDB.BillStatus.DEFAULT);
    }

    private void onBtnConfirmClick(RecyclerView.ViewHolder holder) {
        //TODO: Confirm the bill
        changeStatus(holder.getAbsoluteAdapterPosition(), BillBaseDB.BillStatus.IN_SHIPPING);
    }

    private void changeStatus(int position, BillBaseDB.BillStatus newstatus) {
        BillBaseDB bill = billList.get(position);
        bill.status = newstatus;

        new Thread(()-> {
            DBControllerBill db = new DBControllerBill();
            db.setStatusBill(bill.id, newstatus);
            db.closeConnection();
        }).start();

        billList.remove(position);

        notifyItemRemoved(position);
        notifyStatusChangedListener.notifyChanged(Bill_TabAdapter.this.status, newstatus, bill);

    }

    public class EditProductViewHolder extends BillRecyclerViewAdapter.ViewHolder {
        private final TextView btnConfirm;
        private final TextView btnCancel;

        public EditProductViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO: btn Confirm or Cancel
            btnConfirm = (TextView) itemView.findViewById(R.id.btnConfirm);
            btnCancel = (TextView) itemView.findViewById(R.id.btnCancel);
        }

        public TextView getBtnConfirm() {
            return btnConfirm;
        }

        public TextView getBtnCancel() {
            return btnCancel;
        }
    }
}
