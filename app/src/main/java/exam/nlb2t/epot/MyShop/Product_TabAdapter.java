package exam.nlb2t.epot.MyShop;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductMyShop;
import exam.nlb2t.epot.DialogFragment.YesNoDialog;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class Product_TabAdapter extends RecyclerView.Adapter<Product_TabAdapter.ViewHolder> {
    public final List<ProductMyShop> products;
    Context context;
    boolean isfullProducts;
    int position;
    public Handler mHandler;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Product_TabAdapter(List<ProductMyShop> products) {
        this.products = products;
        lastIndex = products.size();
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).id;
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
        ProductMyShop product = products.get(position);

        holder.getNameView().setText(product.name);
        holder.getPriceView().setText(Helper.getMoneyString(product.priceOrigin));

        if (product.imageProduct != null) {
            holder.getImageView().setImageBitmap(product.imageProduct);
        } else {
            holder.getImageView().setBackgroundResource(R.color.gray);
            new Thread(() -> {
                int size = (int)context.getResources().getDimension(R.dimen.image_card_size);
                Bitmap image = product.loadImagePrimary(size,size);
                mHandler.postDelayed(() -> {
                    product.imageProduct = image;
                    notifyItemChanged(position);
                },100);
            }).start();
        }

        holder.getLikeView().setText("Yêu thích: " + product.numberLike);
        holder.getWarehouseView().setText("Kho hàng: " + (product.amount - product.amountSold));
        holder.getSellView().setText("Đã bán: " + product.amountSold);
    }


    public void setTextItemPrice() {
        //TODO: Set PriceOrigin and Price of ProductItem here
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageView btnShowContextMenu;

        private TextView nameView;
        private TextView priceView;
        private TextView warehouseView;
        private TextView sellView;
        private TextView likeView;

        private Button btnChange;
        private Button btnDelete;

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

            setEventHandler(ViewHolder.this);
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
                AddProductFragment dialog = new AddProductFragment(products.get(holder.getAbsoluteAdapterPosition()));
                dialog.setOnSubmitOKListener(l -> {
                    Toast.makeText(context, "Thay đổi thành công", Toast.LENGTH_LONG).show();
                });

                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), AddProductFragment.NAMEDIALOG);
            }
        });

        holder.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Delete product from My Shop
                DBControllerProduct db = new DBControllerProduct();
                if (db.checkProductIsSold(products.get(holder.getAbsoluteAdapterPosition()).id)) {
                    YesNoDialog dialog = new YesNoDialog(context, "Mặt hàng này đã được bán ra, chỉ có thể ngừng cung cấp. Bạn có chấp nhận không?");
                    dialog.setOnBtnYesClickListener(view -> {
                        stopProvide(holder.getAbsoluteAdapterPosition());
                        dialog.dismiss();
                    });
                    dialog.setOnBtnNoClickListener(view -> {
                        dialog.dismiss();
                    });

                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "YesNoDialog deleteProduct");
                    db.closeConnection();
                    return;
                }
                if (!db.deleteProduct(products.get(holder.getAbsoluteAdapterPosition()).id)) {
                    Log.e("Lỗi", "Không có món cần tìm hoặc không thể xóa món");
                }
                db.closeConnection();

                products.remove(holder.getAbsoluteAdapterPosition());
                notifyItemRemoved(holder.getAbsoluteAdapterPosition());
            }
        });

        holder.btnShowContextMenu.setOnClickListener(v -> {
            //TODO: Show popup menu for product
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.card_product_item_menu);

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_product_sale:
                        //MEANS: Open change product dialog
                        SaleDialog dialog = new SaleDialog(products.get(holder.getLayoutPosition()));
                        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "SaleDialog");
                        break;
                    case R.id.menu_product_stopProvide:
                        //MEANS: Open Stop Provide product dialog
                        stopProvide(products.get(holder.getAbsoluteAdapterPosition()).id);
                        break;
                }
                return true;
            });
            popupMenu.show();
        });
    }

    private void stopProvide(int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBControllerProduct db = new DBControllerProduct();
                db.StopProvide(products.get(position).id);
                db.closeConnection();
            }
        }).start();

        products.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(context, "Đã ngừng cung cấp", Toast.LENGTH_SHORT);
    }

    int step = 10;
    int lastIndex = 0;
    public void addItemToList(Handler mainHandler) {
        if (isfullProducts) return;

        // Avoid multiple thread
        isfullProducts = true;
        new Thread(() -> {
            Log.d("MY_TAG", "Load at index" + lastIndex);
            List<ProductMyShop> newlist;
            DBControllerProduct db = new DBControllerProduct();
            newlist = db.getProductMyShop2(Authenticator.getCurrentUser().id, lastIndex, step);

            db.closeConnection();

            isfullProducts = newlist.size() < step;
            lastIndex += newlist.size();

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    products.addAll(newlist);
                    notifyItemRangeInserted(products.size() - newlist.size(), newlist.size());
                }
            });
        }).start();
    }
}
