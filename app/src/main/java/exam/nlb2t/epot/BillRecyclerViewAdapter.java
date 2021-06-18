package exam.nlb2t.epot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;

public class BillRecyclerViewAdapter extends RecyclerView.Adapter<BillRecyclerViewAdapter.ViewHolder>{
    private List<BillBaseDB> billList;
    private Context context;
    private UserBaseDB shop;
    DBControllerUser dbControllerUser;


    public BillRecyclerViewAdapter (List<BillBaseDB> bill, Context mcontext)
    {
        this.billList = bill;
        this.context = mcontext;
    }

    @NonNull
    @Override
    public BillRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_order_bill_view, parent, false);

        BillRecyclerViewAdapter.ViewHolder viewHolder;
        viewHolder = new BillRecyclerViewAdapter.ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull BillRecyclerViewAdapter.ViewHolder holder, int position) {

        BillBaseDB currentbill= billList.get(position);
        shop=dbControllerUser.getUserInfo(currentbill.userID);
        holder.getShopImage().setImageBitmap(shop.getAvatar(currentbill.userID));
        holder.getTv_shopName().setText(shop.username);
        holder.getTv_IDBill().setText(currentbill.keyBill);
        holder.getTv_DateCreate().setText(currentbill.createdDate.toString());
        holder.getTv_Status().setText(currentbill.status.toString());
        holder.getTv_total().setText("");
        holder.getTv_Amount().setText("");
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
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
            tv_Status=itemView.findViewById(R.id.tv_Status);
            parent_layout = itemView.findViewById(R.id.bill_view);
            btn_Detail=itemView.findViewById(R.id.btn_detail_bill);

            btn_Detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    {

                    }

                }
            });
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
}
