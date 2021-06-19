package exam.nlb2t.epot.MyShop;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
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
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class Product_TabAdapter extends RecyclerView.Adapter<Product_TabAdapter.ViewHolder>{
    public List<ProductBaseDB> products;
    Context context;
    boolean isfullProducts;
    int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Product_TabAdapter() {
        DBControllerProduct db = new DBControllerProduct();
        products = db.getLIMITProduct(Authenticator.getCurrentUser().id, 0, 10);
        db.closeConnection();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_shop_product_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getNameView().setText(products.get(position).name);
        holder.getPriceView().setText(Helper.getMoneyString(products.get(position).priceOrigin));
        holder.getImageView().setImageBitmap(products.get(position).getImagePrimary());
        holder.getLikeView().setText("Yêu thích: " + products.get(position).getNumberLike());
        holder.getWarehouseView().setText("Kho hàng: " + (products.get(position).amount - products.get(position).amountSold));
        holder.getSellView().setText("Đã bán: " + products.get(position).amountSold);

        setEventHandler(holder);
    }

    public void setTextItemPrice() {
        //TODO: Set PriceOrigin and Price of ProductItem here
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final ImageView btnShowContextMenu;

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
            btnShowContextMenu = (ImageView) itemView.findViewById(R.id.my_shop_context_menu_item);

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
        public ImageView getBtnShowContextMenu() {
            return btnShowContextMenu;
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
                DBControllerProduct db = new DBControllerProduct();
                if (!db.deleteProduct(products.get(holder.getAdapterPosition()).id)) {
                    Log.e("Lỗi", "Không có món cần tìm hoặc không thể xóa món");
                }
                db.closeConnection();

                products.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        holder.btnShowContextMenu.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(Menu.NONE, R.id.my_shop_context_menu_item, Menu.NONE, "Giảm giá");
            }
        });
        holder.btnShowContextMenu.setOnLongClickListener(v->{
            setPosition(holder.getAdapterPosition());
            return false;
        });
    }


    public void addItemToList(int number) {
        if (isfullProducts) return;
        DBControllerProduct db = new DBControllerProduct();
        int maxsize = db.getNumberProducts(Authenticator.getCurrentUser().id);
        List<ProductBaseDB> newlist;
        int offset = products.size();

        if (offset == maxsize) {
            isfullProducts = true;
            return;
        }
        if (number + offset > maxsize) {
            newlist = db.getLIMITProduct(Authenticator.getCurrentUser().id, offset, maxsize - offset);
        }
        else {
            newlist = db.getLIMITProduct(Authenticator.getCurrentUser().id, offset, number);
        };

        products.addAll(newlist);
        db.closeConnection();

        notifyItemRangeInserted(offset, products.size());
    }
}
