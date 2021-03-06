package exam.nlb2t.epot.MyShop;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
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
import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.DBControllerNotification;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class Bill_TabAdapter extends BillRecyclerViewAdapter{
    BillBaseDB.BillStatus status;
    public static Bitmap imageMyShop = null;

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

        loadImageShop();

        return holder;
    }

    public synchronized void loadImageShop() {
        //new Handler(Looper.getMainLooper()).post(()->{
        if (imageMyShop == null) {
            int size = (int)context.getResources().getDimension(R.dimen.image_shop_bill_size);
            imageMyShop = Authenticator.getCurrentUser().getAvatar(size, size);
        }
        //});
    }

    @Override
    public void onBindViewHolder(@NonNull BillRecyclerViewAdapter.ViewHolder holder, int position) {
        BillBaseDB bill = billList.get(position);
        this.setBillInfor(holder, bill);
        this.setShopInfor(holder);
        setEventHandler(holder);

        if (holder instanceof EditProductViewHolder) {
            ((EditProductViewHolder) holder).getBtnConfirm().setOnClickListener(v -> onBtnConfirmClick(holder));
            ((EditProductViewHolder) holder).getBtnCancel().setOnClickListener(v -> onBtnCancelClick(holder));
        }
    }

    protected void setShopInfor(@NonNull BillRecyclerViewAdapter.ViewHolder holder) {
        holder.getShopImage().setImageBitmap(imageMyShop);
        holder.getTv_shopName().setText(Authenticator.getCurrentUser().username);
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
        int old = bill.status.getValue();
        bill.status = newstatus;

        new Thread(()-> {
            DBControllerBill db = new DBControllerBill();
            db.setStatusBill(bill.id, newstatus);
            db.closeConnection();

            DBControllerNotification dbNoti = new DBControllerNotification();
            dbNoti.insertNotify_Bill(bill.userID, bill.id,  old, bill.status.getValue());
            dbNoti.closeConnection();
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
