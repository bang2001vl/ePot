package exam.nlb2t.epot;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.DialogFragment.DetailBillFragment;

public class BillRecyclerViewAdapter extends RecyclerView.Adapter<BillRecyclerViewAdapter.ViewHolder>{
    protected List<BillBaseDB> billList;
    protected Context context;
    private List<UserBaseDB> shops;
    protected OnStatusTableChangedListener notifyStatusChangedListener;

    public void setNotifyStatusChangedListener(OnStatusTableChangedListener notifyStatusChangedLintener) {
        this.notifyStatusChangedListener = notifyStatusChangedLintener;
    }

    protected BillRecyclerViewAdapter() {
        //MEANS: It use for Bill_TabAdapter.java, should not write anymore here
    }

    public BillRecyclerViewAdapter (List<BillBaseDB> bills, Context mcontext)
    {
        this.billList = bills;
        this.context = mcontext;
        this.shops = new ArrayList<>();

        DBControllerUser db = new DBControllerUser();
        for (BillBaseDB bill : billList) {
            shops.add(db.getUserInfo(bill.salerID));
        }
        db.closeConnection();
    }

    @NonNull
    @Override
    public BillRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_order_bill_view, parent, false);

        BillRecyclerViewAdapter.ViewHolder viewHolder;
        viewHolder = new BillRecyclerViewAdapter.ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull BillRecyclerViewAdapter.ViewHolder holder, int position) {
        BillBaseDB currentbill= billList.get(position);
        UserBaseDB currentshop = shops.get(position);

        setBillInfor(holder,currentbill);
        setShopInfor(holder, currentshop);

        setEventHandler(holder);
    }

    protected void setBillInfor(@NonNull BillRecyclerViewAdapter.ViewHolder holder, BillBaseDB currentbill) {
        holder.getTv_IDBill().setText(currentbill.keyBill);
        holder.getTv_DateCreate().setText(currentbill.createdDate.toString());
        holder.getTv_Status().setText(currentbill.status.toString());
        holder.getTv_total().setText(String.valueOf(currentbill.total));
        holder.getTv_Amount().setText(String.valueOf(currentbill.getAmountProduct()));
    }

    protected void setShopInfor(@NonNull BillRecyclerViewAdapter.ViewHolder holder, UserBaseDB currentshop) {
        holder.getShopImage().setImageBitmap(currentshop.getAvatar());
        holder.getTv_shopName().setText(currentshop.username);
    }

    protected void setEventHandler(ViewHolder holder) {
        holder.getBtn_Detail().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    //TODO: open detail Bill
                    DetailBillFragment dialog = new DetailBillFragment(billList.get(holder.getAdapterPosition()).id, context);
                    dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),DetailBillFragment.NAMEDIALOG);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView shopImage;
        public TextView tv_shopName;
        public TextView tv_Amount;
        public TextView tv_IDBill;
        public TextView tv_total;
        public TextView tv_DateCreate;
        public TextView tv_Status;
        LinearLayout parent_layout;
        public Button btn_Detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopImage = itemView.findViewById(R.id.shop_image);
            tv_shopName = itemView.findViewById(R.id.name_shop);
            tv_Amount = itemView.findViewById(R.id.tv_amount);
            tv_IDBill= itemView.findViewById(R.id.tv_ID_Order);
            tv_DateCreate = itemView.findViewById(R.id.date_order);
            tv_total = itemView.findViewById(R.id.tv_total);
            tv_Status = itemView.findViewById(R.id.tv_Status);
            parent_layout = itemView.findViewById(R.id.bill_view);
            btn_Detail = itemView.findViewById(R.id.btn_detail_bill);
        }
        public ImageView getShopImage() {
            return shopImage;
        }
        public TextView getTv_shopName() {
            return tv_shopName;
        }
        public TextView getTv_Amount() {
            return tv_Amount;
        }
        public TextView getTv_IDBill() {
            return tv_IDBill;
        }
        public TextView getTv_DateCreate() {
            return tv_DateCreate;
        }
        public TextView getTv_total() {
            return tv_total;
        }
        public TextView getTv_Status() {
            return tv_Status;
        }
        public Button getBtn_Detail() {
            return btn_Detail;
        }
    }

    public interface OnStatusTableChangedListener {
        void notifyChanged(BillBaseDB.BillStatus from, BillBaseDB.BillStatus to, BillBaseDB bill);
    }

    public void addNewItem() {

    }
}
