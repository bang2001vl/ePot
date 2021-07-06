package exam.nlb2t.epot.Category;


import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.OnItemClickListener;
import exam.nlb2t.epot.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> listCategory;
    private OnItemClickListener onItemClickListener;
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Category> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<Category> listCategory) {
        this.listCategory = listCategory;
    }

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public CategoryAdapter(Context context, List<Category> listCategory, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listCategory = listCategory;
        this.onItemClickListener = onItemClickListener;
    }

    public CategoryAdapter(List<Category> listCategory, OnItemClickListener onItemClickListener) {
        this.listCategory = listCategory;
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<Category> list)
    {
        listCategory = list;
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageCategory);
            textView = itemView.findViewById(R.id.textViewNameCategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClickCategory(textView.getText().toString());
                }
            });
        }
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_tab, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = listCategory.get(position);
        if (category != null)
        {
            holder.textView.setText(category.getName());
            if(category.getAvatar() != null) {
                holder.imageView.setImageBitmap(category.getAvatar());
            }
            else {
                Handler handler = new Handler();
                new Thread(()->{
                    DBControllerProduct db = new DBControllerProduct();
                    category.setAvatar(db.getAvatar_Product(category.avatarID));
                    db.closeConnection();

                    handler.post(()->notifyItemChanged(position));
                }).start();
            }
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
            Category category = listCategory.get(position);
            int imageID = category.avatarID;



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
        if (listCategory.size() != 0) return listCategory.size();
        return 0;

    }


    public interface OnClickItemListener
    {
        void onClick(Category category);
    }
}
