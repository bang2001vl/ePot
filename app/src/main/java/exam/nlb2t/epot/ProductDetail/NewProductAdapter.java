package exam.nlb2t.epot.ProductDetail;

import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.ClassInformation.Product;
import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.R;

public class NewProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private List<ProductBaseDB> newProductBaseDBList;
    private boolean isLoading;

    public void setData(List<ProductBaseDB> list)
    {
        newProductBaseDBList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (newProductBaseDBList != null && position == newProductBaseDBList.size() - 1 && isLoading)
        {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false);
            return new NewProductViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM)
        {
            ProductBaseDB productBaseDB = newProductBaseDBList.get(position);
            NewProductViewHolder newProductViewHolder = (NewProductViewHolder) holder;

            newProductViewHolder.imageViewProduct.setImageBitmap(productBaseDB.getImagePrimary());
            newProductViewHolder.textViewNameProduct.setText(productBaseDB.name);
            newProductViewHolder.textViewNewPrice.setText(productBaseDB.price +"đ");
            newProductViewHolder.textViewOldPrice.setText(productBaseDB.priceOrigin + "đ");
            newProductViewHolder.textViewAmountSold.setText("Đã bán: " + productBaseDB.amountSold);
            newProductViewHolder.textViewSale.setText(productBaseDB.getDiscount() + "%");
            newProductViewHolder.textViewCmt.setText("(" + productBaseDB.getTotalCmt() + ")");
        }
    }

    @Override
    public int getItemCount() {
        if (newProductBaseDBList == null){
            return 0;
        }
        return newProductBaseDBList.size();
    }

    public class NewProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewProduct;
        private TextView textViewNameProduct;
        private TextView textViewNewPrice;
        private TextView textViewOldPrice;
        private TextView textViewAmountSold;
        private TextView textViewSale;
        private RatingBar ratingBar;
        private TextView textViewCmt;

        public NewProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.Image_Product);
            textViewNameProduct = itemView.findViewById(R.id.textview_proName);
            textViewNewPrice = itemView.findViewById(R.id.textview_proPrice);
            textViewOldPrice = itemView.findViewById(R.id.textview_OldproPrice);
            textViewAmountSold = itemView.findViewById(R.id.textview_proSold);
            textViewSale = itemView.findViewById(R.id.tv_tag_salepro);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            textViewCmt = itemView.findViewById(R.id.TotalCmt);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar_loading);
        }
    }

    public void removeFooterLoading(){
        isLoading = false;
        int position = newProductBaseDBList.size()-1;
        ProductBaseDB productBaseDB = newProductBaseDBList.get(position);

        if (productBaseDB != null)
        {
            newProductBaseDBList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addFooterLoading(){
        isLoading = true;
        newProductBaseDBList.add(new ProductBaseDB());
    }
}
