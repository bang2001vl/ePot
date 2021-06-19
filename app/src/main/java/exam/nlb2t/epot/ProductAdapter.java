package exam.nlb2t.epot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.Database.Tables.ProductBaseDB;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final List<ProductBaseDB> productList;
    private final Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();


    ProductAdapter (List<ProductBaseDB > products, Context mcontext)
    {
        this.productList = products;
        this.context = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);


        ViewHolder viewHolder;
        viewHolder = new ViewHolder(view);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ClipData.Item item = viewHolder.getPosition(viewHolder.getAdapterPosition());
                outsideClickListener.onItemClicked(item);*/
            }});

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductBaseDB  product = productList.get(position);

        String price = product.priceOrigin + " đ";
        SpannableString oldproprice = new SpannableString(price);
        oldproprice.setSpan(new StrikethroughSpan(), 0, (price).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (product.priceOrigin == product.price)
        {
            holder.tag_salepro.setVisibility(View.GONE);
            holder.tv_Oldproprice.setVisibility(View.GONE);
        }
        else
        {
            holder.tag_salepro.setText(" -" + (product.price*100 / product.priceOrigin) +"% ");
            holder.tv_Oldproprice.setText(oldproprice);
        }

        holder.tv_Pricepro.setText(product.price + " đ");
        holder.tv_Namepro.setText(" " + product.name + " ");
        holder.imagePro.setImageBitmap(product.getImagePrimary());
        holder.tv_Amountpro.setText("Đã bán " + product.amountSold);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imagePro;
        public TextView tv_Oldproprice;
        public TextView tv_Namepro;
        public TextView tv_Pricepro;
        public TextView tv_Amountpro;
        public TextView tag_salepro;
        public TextView tv_ShopName;
        public RatingBar rt_Rating;
        public TextView tv_Cmt;
        public Button btn_favorites;
        LinearLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePro = itemView.findViewById(R.id.Image_Product);
            tv_Amountpro = itemView.findViewById(R.id.textview_proSold);
            tv_Namepro = itemView.findViewById(R.id.textview_proName);
            tv_Oldproprice= itemView.findViewById(R.id.textview_OldproPrice);
            tv_Pricepro = itemView.findViewById(R.id.textview_proPrice);
            tv_ShopName = itemView.findViewById(R.id.StoreName);
            tv_Cmt = itemView.findViewById(R.id.TotalCmt);
            rt_Rating = itemView.findViewById(R.id.ratingbar);
            tag_salepro = itemView.findViewById(R.id.tv_tag_salepro);
            parent_layout = itemView.findViewById(R.id.product_item);
            btn_favorites = itemView.findViewById(R.id.Favorite);

            btn_favorites.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onClick(View v) {
                    {
                        //change status of favorites button
                         if (btn_favorites.getBackground() != v.getContext().getResources().getDrawable(R.drawable.red_favorite_24))
                         {
                             btn_favorites.setBackgroundResource(R.drawable.red_favorite_24);
                         }
                         else
                         {
                             btn_favorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);

                         }
                    }
                }
            });
            itemView.setPadding(2, 2 , 2, 2);
        }
    }
    public void addproduct(List<ProductBaseDB> subpro) {
        productList.addAll(subpro);
        this.notifyItemRangeInserted(productList.size(), subpro.size());
    }

}
