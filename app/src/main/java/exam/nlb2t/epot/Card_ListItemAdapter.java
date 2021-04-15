package exam.nlb2t.epot;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thunderstudio.mylib.OnValueChanged;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.Views.NumberPickerView;

public class Card_ListItemAdapter extends RecyclerView.Adapter<Card_ListItemAdapter.Card_ListItemViewHolder> {
    public Object Tag;
    private List<ProductBuyInfo> products;
    private List<Boolean> checked;

    public List<Boolean> getCheckedArray(){return checked;}

    public void removeItemRange(int from, int count) {
        for(int i = from + count - 1; i>= from; i--)
        {
            products.remove(i);
            checked.remove(i);
        }
        notifyItemRangeRemoved(from, count);
    }

    public void removeItemAt(int position) {
        products.remove(position);
        checked.remove(position);
        notifyItemRemoved(position);
        if(onProductBuyInfoChanged!=null)
        {
            onProductBuyInfoChanged.onDeletedItem(position);
        }
    }

    public void setSelectedAll(boolean value)
    {
        for(int i = 0; i<checked.size(); i++)
        {
            if((checked.get(i) != value) && (onProductBuyInfoChanged != null)) {
                checked.set(i, value);
                onProductBuyInfoChanged.onCheckProductChanged(products.get(i), value);
            }
        }
        this.notifyItemRangeChanged(0, products.size());
    }

    private OnProductBuyInfoChanged onProductBuyInfoChanged;
    public void setOnProductBuyInfoChanged(OnProductBuyInfoChanged onProductBuyInfoChanged)
    {
        this.onProductBuyInfoChanged = onProductBuyInfoChanged;
    }

    @NonNull
    @Override
    public Card_ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.card_list_item, parent, false);
        Card_ListItemViewHolder viewHolder  = new Card_ListItemViewHolder(view);

        viewHolder.cbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = viewHolder.getAdapterPosition();

                if((checked.get(position) != isChecked) && (onProductBuyInfoChanged != null))
                {
                    checked.set(position, isChecked);
                    onProductBuyInfoChanged.onCheckProductChanged(products.get(position), isChecked);
                }
            }
        });

        viewHolder.numberPickerView.setOnValueChangeListener(new OnValueChanged<Float>() {
            @Override
            public void onValueChanged(Float newValue) {
                ProductBuyInfo buyInfo = products.get(viewHolder.getAdapterPosition());
                if(buyInfo.Amount != newValue.intValue()) {
                    buyInfo.Amount = newValue.intValue();
                    if (checked.get(viewHolder.getAdapterPosition()) && onProductBuyInfoChanged != null) {
                        onProductBuyInfoChanged.onNumberProductChanged(buyInfo, buyInfo.Amount);
                    }
                }
            }
        });

        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                removeItemAt(position);
                //Log.d("MY_TRACE", String.format("Click remove position $1%d", position));
            }
        });

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Card_ListItemViewHolder holder, int position) {
        ProductBuyInfo buyInfo = products.get(position);

        holder.setName(buyInfo.product.productName);
        holder.setTxtPrice_current(buyInfo.product.currentPrice);
        holder.setChecked(checked.get(position));

        holder.setMaxAmount(buyInfo.product.avaiableAmount);
        holder.setAmount(buyInfo.Amount);

        if(buyInfo.product.currentPrice == buyInfo.product.originPrice) {
            holder.txtPrice_origin.setVisibility(View.GONE);
        }
        else
        {
            holder.txtPrice_origin.setVisibility(View.VISIBLE);
            holder.setTxtPrice_origin(buyInfo.product.originPrice);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public Card_ListItemAdapter(@NonNull List<ProductBuyInfo> data, List<Boolean> checkedList)
    {
        this.products = data;
        if(checkedList == null) {
            int n = data.size();
            checked = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                checked.add(Boolean.FALSE);
            }
        }
        else
        {
            checked = checkedList;
        }
    }

    public class Card_ListItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtName;
        TextView txtPrice_current;
        TextView txtPrice_origin;
        CheckBox cbSelected;
        NumberPickerView numberPickerView;
        ImageButton btnRemove;

        Context mContext;

        public void setName(String name)
        {
            txtName.setText(name);
        }

        public void setTxtPrice_current(int price)
        {
            txtPrice_current.setText(String.format(Locale.getDefault(),mContext.getString(R.string.format_price), price));
        }

        public void setTxtPrice_origin(int price)
        {
            txtPrice_origin.setText(String.format(Locale.getDefault(),mContext.getString(R.string.format_price_strike), price));
        }

        public void setChecked(boolean isChecked)
        {
            cbSelected.setChecked(isChecked);
        }

        public void setAmount(int amount)
        {
            numberPickerView.controller.setNumber(amount);
        }

        public void setMaxAmount(int val)
        {
            numberPickerView.controller.max = val;
        }

        public int getAmount()
        {
            return (int) numberPickerView.controller.getNumber();
        }

        public Card_ListItemViewHolder(@NonNull View view)
        {
            super(view);
            txtName = view.findViewById(R.id.txtName_item);
            txtPrice_origin = view.findViewById(R.id.price_origin);
            txtPrice_current = view.findViewById(R.id.price_current);
            cbSelected = view.findViewById(R.id.cb_choose_item);
            numberPickerView = view.findViewById(R.id.amount_picker);
            btnRemove = view.findViewById(R.id.btnRemoveItem);

            mContext = view.getContext();

            txtPrice_origin.setPaintFlags(txtPrice_origin.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public interface OnProductBuyInfoChanged
    {
        public void onCheckProductChanged(ProductBuyInfo productBuyInfo, boolean isChecked);
        public void onNumberProductChanged(ProductBuyInfo productBuyInfo, int newNumber);
        void onDeletedItem(int positionon);
    }
}
