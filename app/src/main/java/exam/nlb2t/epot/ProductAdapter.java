package exam.nlb2t.epot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
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

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductAdapterItemInfo> products;
    private Context context;
    private OnItemClickListener onItemClickListener;
    ProductBaseDB  product;

    public ProductAdapter (List<ProductAdapterItemInfo> products, Context mcontext, OnItemClickListener onItemClickListener)
    {
        this.products = products;
        this.context = mcontext;
        this.onItemClickListener = onItemClickListener;
    }

    public ProductAdapter(List<ProductAdapterItemInfo> productList, Context context) {
        this.products = productList;
        this.context = context;
    }

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public List<ProductAdapterItemInfo> getProductList() {
        return products;
    }

    public void setProductList(List<ProductAdapterItemInfo> productList) {
        this.products = productList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(List<ProductAdapterItemInfo> list){
        products = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductAdapterItemInfo info = products.get(position);
        this.product =  info.productBaseDB;
        holder.id = product.id;
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
        holder.tv_Amountpro.setText("Đã bán " + product.amountSold);

        holder.rt_Rating.setRating(product.starAverage);
        holder.btn_favorites.setBackgroundResource( info.isLiked ? R.drawable.red_favorite_24 :R.drawable.ic_baseline_favorite_24 );

        if(info.productAvatar != null) {
            holder.imagePro.setImageBitmap(info.productAvatar);
        }
        else {holder.imagePro.setImageResource(R.color.white);}
        if(info.productAvatar == null) {
            new Thread(new LoadImageRunable(new Handler(), position), "LoadImageAt=" + position).start();
        }
    }

    public class LoadImageRunable implements Runnable {
        int position;
        android.os.Handler mainHandler;
        public LoadImageRunable(Handler mainHandler, int item_position)
        {
            this.mainHandler = mainHandler;
            position = item_position;
        }
        @Override
        public void run() {
            ProductAdapterItemInfo info = products.get(position);
            int imageID = info.productBaseDB.imagePrimaryID;

            DBControllerProduct db = new DBControllerProduct();
            info.productAvatar = db.getAvatar_Product(imageID);
            db.closeConnection();

            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(position);
                }
            }, 100);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
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
        public int id;

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(),"id: " + id,Toast.LENGTH_LONG).show();
                    onItemClickListener.onItemClickProduct(id);
                }
            });
            itemView.setPadding(2, 2 , 2, 2);
        }
    }

    public void addproduct(List<ProductAdapterItemInfo> subpro) {
        products.addAll(subpro);
        this.notifyItemRangeInserted(products.size() - subpro.size(), subpro.size());
    }

}
