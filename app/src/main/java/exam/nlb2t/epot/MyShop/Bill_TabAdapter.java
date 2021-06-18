package exam.nlb2t.epot.MyShop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import exam.nlb2t.epot.BillRecyclerViewAdapter;
import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.singleton.Authenticator;

public class Bill_TabAdapter extends BillRecyclerViewAdapter{
    BillBaseDB.BillStatus status;
    private OnStatusTableChangedListener notifyStatusChangedListener;

    public Bill_TabAdapter(BillBaseDB.BillStatus status) {
        super();
        this.status = status;

        DBControllerBill db = new DBControllerBill();
        billList = db.getBillsOverviewbyStatus(Authenticator.getCurrentUser().id, status);
        db.closeConnection();

        DBControllerUser db2 = new DBControllerUser();
        for (BillBaseDB bill : billList) {
            shops.add(db2.getUserInfo(bill.userID));
        }
        db.closeConnection();
    }

    public void setNotifyStatusChangedListener(OnStatusTableChangedListener notifyStatusChangedLintener) {
        this.notifyStatusChangedListener = notifyStatusChangedLintener;
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
        super.onBindViewHolder(holder, position);

//        holder.getShopImage().setImageBitmap(Authenticator.getCurrentUser().getAvatar(Authenticator.getCurrentUser().avatarID));
//        holder.getTv_shopName().setText(Authenticator.getCurrentUser().username);
//        holder.getTv_IDBill().setText(billList.get(position).keyBill);
//        holder.getTv_DateCreate().setText(billList.get(position).createdDate.toString());
//        holder.getTv_Status().setText(billList.get(position).status.toString());
//        holder.getTv_total().setText("");
//        holder.getTv_Amount().setText("");

        if (holder instanceof EditProductViewHolder) {
            ((EditProductViewHolder) holder).getBtnConfirm().setOnClickListener(v -> onBtnConfirmClick(holder));
            ((EditProductViewHolder) holder).getBtnCancel().setOnClickListener(v -> onBtnCancelClick(holder));
        }
    }

    private void onBtnCancelClick(RecyclerView.ViewHolder holder) {
        //TODO: Cancel the bill
        changeStatus(holder.getAdapterPosition(), BillBaseDB.BillStatus.DEFAULT);
    }

    private void onBtnConfirmClick(RecyclerView.ViewHolder holder) {
        //TODO: Confirm the bill
        changeStatus(holder.getAdapterPosition(), BillBaseDB.BillStatus.IN_SHIPPING);
    }

    private void changeStatus(int position, BillBaseDB.BillStatus newstatus) {
        billList.get(position).status = newstatus;

        DBControllerBill db = new DBControllerBill();
        db.setStatusBill(billList.get(position).id, newstatus);
        db.closeConnection();

        notifyStatusChangedListener.notifyChanged(Bill_TabAdapter.this.status, newstatus);
        billList.remove(position);
        //notifyItemRemoved(position);
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

    public interface OnStatusTableChangedListener {
        void notifyChanged(BillBaseDB.BillStatus from, BillBaseDB.BillStatus to);
    }
}
