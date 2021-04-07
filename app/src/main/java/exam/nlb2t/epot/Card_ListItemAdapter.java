package exam.nlb2t.epot;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thunderstudio.mylib.OnValueChanged;
import com.thunderstudio.mylib.Views.ChooseAmountLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import exam.nlb2t.epot.ClassInformation.Product;
import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;

public class Card_ListItemAdapter extends RecyclerView.Adapter<Card_ListItemAdapter.Card_ListItemViewHolder> {
    private final List<ProductBuyInfo> products;
    private boolean[] checked;

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
        ProductBuyInfo buyInfo = products.get(position);

        holder.setName(buyInfo.product.ProductName);
        holder.setTxtPrice(buyInfo.product.CurrentPrice+"VND");
        holder.setChecked(checked[position]);

        holder.setMaxAmount(buyInfo.product.AvaiableAmount);
        holder.setAmount(buyInfo.Amount);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public void onViewRecycled(@NonNull Card_ListItemViewHolder holder) {
        super.onViewRecycled(holder);
        ProductBuyInfo productBuyInfo = products.get(holder.getAdapterPosition());
        productBuyInfo.Amount = holder.getAmount();
    }

    public Card_ListItemAdapter(@NonNull List<ProductBuyInfo> data)
    {
        this.products = data;
        checked = new boolean[data.size()];
        for(int i = 0; i<checked.length; i++)
        {
            checked[i] = false;
        }
    }

    public class Card_ListItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtName;
        TextView txtPrice;
        CheckBox cbSelected;
        ChooseAmountLayout chooseAmountLayout;

        OnValueChanged<Float> default_listener;

        public void setName(String name)
        {
            txtName.setText(name);
        }

        public void setTxtPrice(String price)
        {
            txtPrice.setText(price);
        }

        public void setChecked(boolean isChecked)
        {
            cbSelected.setChecked(isChecked);
        }

        public void setAmount(int amount)
        {
            chooseAmountLayout.controller.setNumber(amount);
        }

        public void setMaxAmount(int val)
        {
            chooseAmountLayout.controller.max = val;
        }

        public int getAmount()
        {
            return (int)chooseAmountLayout.controller.getNumber();
        }

        public Card_ListItemViewHolder(@NonNull View view)
        {
            super(view);
            txtName = view.findViewById(R.id.txtName_item);
            txtPrice = view.findViewById(R.id.txtPrice);
            cbSelected = view.findViewById(R.id.cb_choose_item);
            chooseAmountLayout = view.findViewById(R.id.amount_picker);
        }
    }
}
