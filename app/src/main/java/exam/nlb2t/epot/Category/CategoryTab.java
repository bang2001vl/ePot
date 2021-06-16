package exam.nlb2t.epot.Category;


import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.R;

public class CategoryTab extends RecyclerView.Adapter<CategoryTab.AdapterDataObserver> {

    private Context context;
    private int layout;
    private List<Category> categoryList;

    public CategoryTab(Context context, int layout, List<Category> categoryList) {
        this.context = context;
        this.layout = layout;
        this.categoryList = categoryList;
    }

    @Override
    public CategoryTab.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryTab.AdapterDataObserver holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.AdapterDataObserver holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.Adapter holder, int position) {

    }

    @NonNull
    @Override
    public RecyclerDataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerDataAdapter.DataViewHolder holder, int position) {

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    public class AdapterDataObserver extends RecyclerView.ViewHolder {
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout, null);
        Category category = categoryList.get(position);
        TextView textView = convertView.findViewById(R.id.imageCategory);
        ImageView imageView = convertView.findViewById(R.id.imageCategory);
        textView.setText(category.getName());
        imageView.setImageResource(category.getAvatar_id());
        return convertView;
    }*/
}
