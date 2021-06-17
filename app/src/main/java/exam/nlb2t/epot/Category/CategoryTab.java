package exam.nlb2t.epot.Category;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.R;

public class CategoryTab extends RecyclerView.Adapter<CategoryTab.CategoryViewHolder> {

    private Context context;
    private List<Category> listCategory;

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

    public CategoryTab(Context context) {
        this.context = context;
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
            textView =itemView.findViewById(R.id.textViewNameCategory);
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
            holder.imageView.setImageBitmap(category.getAvatar_id());
            holder.textView.setText(category.getName());
        }
    }

    @Override
    public int getItemCount() {
        if (listCategory.size() != 0) return listCategory.size();
        return 0;

    }



}
