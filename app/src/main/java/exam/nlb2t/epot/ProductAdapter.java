package exam.nlb2t.epot;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.ClassInformation.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    ProductAdapter (List<Product> products, Context mcontext)
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

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = productList.get(position);

        String price = " " + product.originPrice + " đ";
        SpannableString oldproprice = new SpannableString(price);
        oldproprice.setSpan(new StrikethroughSpan(), 0, (price).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (product.originPrice == product.currentPrice)
        {
            holder.tag_salepro.setVisibility(View.GONE);
            holder.tv_Oldproprice.setVisibility(View.GONE);
        }
        else
        {
            holder.tag_salepro.setText("-" + (product.currentPrice*100 / product.originPrice) +"%");
            holder.tv_Oldproprice.setText(oldproprice);
        }

        holder.tv_Pricepro.setText(" " + product.currentPrice + " đ ");
        holder.tv_Namepro.setText(" " + product.productName + " ");
        holder.imagePro.setImageBitmap(product.mainImage);
        holder.tv_Amountpro.setText(" Đã bán " + product.numberSold);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imagePro;
        public TextView tv_Oldproprice;
        public TextView tv_Namepro;
        public TextView tv_Pricepro;
        public TextView tv_Amountpro;
        public TextView tag_salepro;
        LinearLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePro = itemView.findViewById(R.id.Image_Product);
            tv_Amountpro = itemView.findViewById(R.id.textview_proSold);
            tv_Namepro = itemView.findViewById(R.id.textview_proName);
            tv_Oldproprice= itemView.findViewById(R.id.textview_OldproPrice);
            tv_Pricepro = itemView.findViewById(R.id.textview_proPrice);
            tag_salepro = itemView.findViewById(R.id.tv_tag_salepro);
            parent_layout = itemView.findViewById(R.id.product_item);

            itemView.setPadding(10, 10 , 10, 10);


        }
    }

}
