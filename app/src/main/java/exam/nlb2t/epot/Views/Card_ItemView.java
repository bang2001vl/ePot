package exam.nlb2t.epot.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.thunderstudio.mylib.OnValueChanged;

import exam.nlb2t.epot.R;

public class Card_ItemView extends BaseCustomViewGroup{
    public Card_ItemView(Context context) {
        super(context);
        init(context, null);
    }

    public Card_ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Card_ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public Card_ItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    View.OnClickListener onClickDeleteListener= new OnClickListener() {
        @Override
        public void onClick(View v) {
            // Empty body
        }
    };
    OnListItemChangedListener onListItemChangedListener = new OnListItemChangedListener() {
        @Override
        public void onCheckProductChanged(View view, boolean isChecked) {

        }

        @Override
        public void onNumberProductChanged(View view, int newNumber) {

        }
    };

    public void setOnClickDeleteListener(OnClickListener onClickDeleteListener)
    {
        this.onClickDeleteListener = onClickDeleteListener;
    }

    public void setOnListItemChangedListener(OnListItemChangedListener onListItemChangedListener)
    {
        this.onListItemChangedListener = onListItemChangedListener;
    }

    public Object Tag;
    TextView txtName;
    TextView txtPrice_current;
    TextView txtPrice_origin;
    CheckBox cbSelected;
    NumberPickerView numberPickerView;
    ImageButton btnRemove;
    ImageView itemImage;
    Context mContext;
    void init(Context context, AttributeSet attrs)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_list_item, this, false);

        txtName = view.findViewById(R.id.txtName_item);
        txtPrice_origin = view.findViewById(R.id.price_origin);
        txtPrice_current = view.findViewById(R.id.price_current);
        cbSelected = view.findViewById(R.id.cb_choose_item);
        numberPickerView = view.findViewById(R.id.amount_picker);
        btnRemove = view.findViewById(R.id.btnRemoveItem);
        itemImage = view.findViewById(R.id.image_item);

        mContext = view.getContext();

        txtPrice_origin.setPaintFlags(txtPrice_origin.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(view, params);

        if(attrs != null)
        {
            TypedArray arr = mContext.obtainStyledAttributes(attrs,R.styleable.Card_ItemView);
            String itemName = arr.getString(R.styleable.Card_ItemView_itemName);
            int maxAmount = arr.getInt(R.styleable.Card_ItemView_maxAmount, 10);
            int amount = arr.getInt(R.styleable.Card_ItemView_amount, 1);
            int price_origin = arr.getInt(R.styleable.Card_ItemView_price_origin, 10000);
            int price_current = arr.getInt(R.styleable.Card_ItemView_price_current, 9000);
            Drawable drawable = arr.getDrawable(R.styleable.Card_ItemView_itemImage);
            setDataWithDrawable(itemName, price_current, price_origin, maxAmount, amount, drawable);
            arr.recycle();
        }
        initListener();
    }

    public void setDataWithDrawable(String name, int price_current, int price_origin, int amount_avaiable, int amount_picked, Drawable image)
    {
        if(name != null){
            txtName.setText(name);
        }
        numberPickerView.controller.max = amount_avaiable;
        numberPickerView.controller.setNumber(amount_picked);

        txtPrice_current.setText(String.format(mContext.getString(R.string.format_price), price_current));
        if(price_current == price_origin)
        {
            txtPrice_origin.setVisibility(GONE);
        }
        else {
            txtPrice_origin.setVisibility(VISIBLE);
            txtPrice_origin.setText(String.format(mContext.getString(R.string.format_price), price_origin));
        }

        if(image != null){
            itemImage.setImageDrawable(image);
        }
    }

    public void setData(String name, int price_current, int price_origin, int amount_avaiable, int amount_picked, Bitmap image)
    {
        setDataWithDrawable(name, price_current, price_origin, amount_avaiable, amount_picked, null);

        if(image != null){
            itemImage.setImageBitmap(image);
        }
    }

    public void setChecked(boolean isChecked)
    {
        cbSelected.setChecked(isChecked);
    }

    public boolean getChecked()
    {
        return cbSelected.isChecked();
    }

    void initListener()
    {
        btnRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteListener.onClick(Card_ItemView.this);
            }
        });

        numberPickerView.setOnValueChangeListener(new OnValueChanged<Float>() {
            @Override
            public void onValueChanged(Float newValue) {
                onListItemChangedListener.onNumberProductChanged(Card_ItemView.this, newValue.intValue());
            }
        });

        cbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onListItemChangedListener.onCheckProductChanged(Card_ItemView.this, isChecked);
            }
        });
    }

    public interface OnListItemChangedListener
    {
        void onCheckProductChanged(View view, boolean isChecked);
        void onNumberProductChanged(View view, int newNumber);
    }
}
