package exam.nlb2t.epot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thunderstudio.mylib.Views.ChooseAmountLayout;

import java.util.List;
import java.util.zip.Inflater;

import exam.nlb2t.epot.ClassInformation.Product;

public class Card_ListItemAdapter extends RecyclerView.Adapter<Card_ListItemAdapter.Card_ListItemViewHolder> {
    private List<Product> products;

    @NonNull
    @Override
    public Card_ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_list_item, parent, false);
        Card_ListItemViewHolder viewHolder  = new Card_ListItemViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Card_ListItemViewHolder holder, int position) {
        View view = holder.itemView;
        TextView txtName = view.findViewById(R.id.txtName_item);
        TextView txtPrice = view.findViewById(R.id.txtPrice);
        CheckBox cbSelected = view.findViewById(R.id.cb_choose_item);
        ChooseAmountLayout chooseAmountLayout = view.findViewById(R.id.amount_picker);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class Card_ListItemViewHolder extends RecyclerView.ViewHolder
    {
        public Card_ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public Card_ListItemAdapter(List<Product> data)
    {
        this.products = data;
    }
}
