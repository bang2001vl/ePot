package exam.nlb2t.epot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductInBill;
import exam.nlb2t.epot.databinding.FragmentProductInBillBinding;
import exam.nlb2t.epot.singleton.Helper;

public class Product_InBill_Adapter extends RecyclerView.Adapter<Product_InBill_Adapter.ViewHolder> {
    private final List<ProductInBill> listProduct;
    Context context;

    public Product_InBill_Adapter(List<ProductInBill> listProduct) {
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.fragment_product_in_bill,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setUI(holder,position);
        setEventHandler();
    }

    private void setUI(ViewHolder holder, int position) {
        holder.getImageProduct().setImageBitmap(listProduct.get(position).getImagePrimary());
        holder.getTxtName().setText(listProduct.get(position).getName());
        holder.getTxtPrice().setText(Helper.getMoneyString(listProduct.get(position).getPriceSell()));
        holder.getTxtQuantity().setText(String.format("x %d", listProduct.get(position).getQuantity()));
        holder.getTxtSTT().setText(String.format("#%d", position+1));
    }

    private void setEventHandler() {
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageProduct;
        private final TextView txtName;
        private final TextView txtPrice;
        private final TextView txtQuantity;
        private final TextView txtSTT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = (ImageView)itemView.findViewById(R.id.img_Product);
            txtName = (TextView)itemView.findViewById(R.id.titlename_Product);
            txtPrice = (TextView)itemView.findViewById(R.id.price_Product);
            txtQuantity = (TextView) itemView.findViewById(R.id.quantity_Product);
            txtSTT = (TextView) itemView.findViewById(R.id.stt_product);
        }

        public ImageView getImageProduct() {
            return imageProduct;
        }
        public TextView getTxtName() {
            return txtName;
        }
        public TextView getTxtPrice() {
            return txtPrice;
        }
        public TextView getTxtQuantity() {
            return txtQuantity;
        }
        public TextView getTxtSTT() {
            return txtSTT;
        }
    }
}
