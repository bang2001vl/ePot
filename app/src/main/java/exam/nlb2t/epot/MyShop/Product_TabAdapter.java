package exam.nlb2t.epot.MyShop;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.MyShopProductTabBinding;
import exam.nlb2t.epot.databinding.MyShopProductViewBinding;
import exam.nlb2t.epot.singleton.Helper;

public class Product_TabAdapter extends RecyclerView.Adapter<Product_TabAdapter.ViewHolder> {
    List<ProductBaseDB> products;
    Context context;

    public Product_TabAdapter(int userID) {
        DBControllerProduct db = new DBControllerProduct();
        products = db.getProducts(userID);
        db.closeConnection();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_shop_product_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getNameView().setText(products.get(position).name);
        holder.getPriceView().setText(Helper.getMoneyString(products.get(position).price));
        holder.getImageView().setImageBitmap(products.get(position).getImagePrimary());
        holder.getLikeView().setText("Yêu thích: " + products.get(position).getNumberLike());
        holder.getWarehouseView().setText("Kho hàng: " + (products.get(position).amount - products.get(position).amountSold));
        holder.getSellView().setText("Đã bán: " + products.get(position).amountSold);

        setEventHandler(holder);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        private final TextView nameView;
        private final TextView priceView;
        private final TextView warehouseView;
        private final TextView sellView;
        private final TextView likeView;

        private final Button btnChange;
        private final Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.my_shop_imageProduct);

            nameView = (TextView) itemView.findViewById(R.id.my_shop_nameProduct);
            priceView = (TextView) itemView.findViewById(R.id.my_shop_priceProduct);
            warehouseView = (TextView) itemView.findViewById(R.id.my_shop_txt_numberWarehouse);
            sellView = (TextView) itemView.findViewById(R.id.my_shop_txt_numberSale);
            likeView = (TextView) itemView.findViewById(R.id.my_shop_txt_numberLike);

            btnChange = (Button) itemView.findViewById(R.id.my_shop_btn_change);
            btnDelete = (Button) itemView.findViewById(R.id.my_shop_btn_delete);
        }

        public ImageView getImageView() {
            return imageView;
        }
        public TextView getNameView() {
            return nameView;
        }
        public TextView getPriceView() {
            return priceView;
        }
        public TextView getWarehouseView() {
            return warehouseView;
        }
        public TextView getLikeView() {
            return likeView;
        }
        public TextView getSellView() {
            return sellView;
        }
        public Button getBtnChange() {
            return btnChange;
        }
        public Button getBtnDelete() {
            return btnDelete;
        }
    }
    private void setEventHandler(ViewHolder holder) {
        holder.getBtnChange().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Open dialog view update_product.xml
                AddProductFragment dialog = new AddProductFragment(products.get(holder.getAdapterPosition()));
                dialog.show(((AppCompatActivity)context).getSupportFragmentManager(),AddProductFragment.NAMEDIALOG);
            }
        });

        holder.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Delete product from My Shop
                products.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }
}
