package exam.nlb2t.epot.RatingProduct;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.PersonBill.BillAdapter;
import exam.nlb2t.epot.databinding.FragmentRatingTabNewItemBinding;

public class ProductOverviewAdapter extends RecyclerView.Adapter<ProductOverviewAdapter.ProductOverviewViewHolder> {
    public List<ProductOverviewAdpterItem> list;

    public ProductOverviewAdapter(List<ProductOverviewAdpterItem> list) {
        this.list = list;
    }

    protected BillAdapter.OnClickItemPositionListener onCLickItemListener;

    public void setOnCLickItemListener(BillAdapter.OnClickItemPositionListener onCLickItemListener) {
        this.onCLickItemListener = onCLickItemListener;
    }

    @NonNull
    @Override
    public ProductOverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentRatingTabNewItemBinding binding = FragmentRatingTabNewItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductOverviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOverviewViewHolder holder, int position) {
        ProductOverviewAdpterItem info = list.get(position);

        holder.binding.txtProductName.setText(info.productName);
        holder.binding.txtSalerName.setText(info.salerName);

        if(info.productAvatar != null)
        {
            holder.binding.imageAvatar.setImageBitmap(info.productAvatar);
        }
        else {
            Handler mainHandler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBControllerProduct db = new DBControllerProduct();
                    info.productAvatar = db.getAvatar_Product(info.productAvatarID);
                    db.closeConnection();

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(position);
                        }
                    });
                }
            }).start();
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductOverviewViewHolder extends RecyclerView.ViewHolder{
        FragmentRatingTabNewItemBinding binding;

        public ProductOverviewViewHolder(@NonNull FragmentRatingTabNewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v->{
                if(onCLickItemListener!= null){onCLickItemListener.onClickItem(this.getBindingAdapterPosition());}
            });
        }
    }
}
