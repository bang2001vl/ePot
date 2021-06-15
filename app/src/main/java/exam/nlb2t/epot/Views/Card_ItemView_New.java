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
import exam.nlb2t.epot.databinding.ItemcardTemplateBinding;
import exam.nlb2t.epot.singleton.Helper;

public class Card_ItemView_New extends BaseCustomViewGroup{
    public Card_ItemView_New(Context context) {
        super(context);
    }

    public Card_ItemView_New(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Card_ItemView_New(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Card_ItemView_New(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    View.OnClickListener onClickDeleteListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Empty body
        }
    };
    Card_ItemView_New.OnListItemChangedListener onListItemChangedListener = new Card_ItemView_New.OnListItemChangedListener() {
        @Override
        public void onCheckProductChanged(View view, boolean isChecked) {

        }

        @Override
        public void onNumberProductChanged(View view, int newNumber) {

        }
    };

    public void setOnClickDeleteListener(View.OnClickListener onClickDeleteListener)
    {
        this.onClickDeleteListener = onClickDeleteListener;
    }

    public void setOnListItemChangedListener(Card_ItemView_New.OnListItemChangedListener onListItemChangedListener)
    {
        this.onListItemChangedListener = onListItemChangedListener;
    }

    public Object Tag;
    int amount_max ;
    int amount;
    ItemcardTemplateBinding binding;
    @Override
    public void init(Context context, AttributeSet attrs)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = ItemcardTemplateBinding.inflate(inflater, this, false);

        if(attrs != null)
        {
            TypedArray arr = getContext().obtainStyledAttributes(attrs,R.styleable.Card_ItemView);
            String itemName = arr.getString(R.styleable.Card_ItemView_itemName);
            int maxAmount = arr.getInt(R.styleable.Card_ItemView_maxAmount, 10);
            int amount = arr.getInt(R.styleable.Card_ItemView_amount, 1);
            int price_current = arr.getInt(R.styleable.Card_ItemView_price_current, 9000);
            Drawable drawable = arr.getDrawable(R.styleable.Card_ItemView_itemImage);
            setDataWithDrawable(itemName, price_current, maxAmount, amount, drawable);
            arr.recycle();
        }

        initListener();

        this.addView(binding.getRoot());
    }

    public void setDataWithDrawable(String name, int price_current, int amount_max, int amount_picked, Drawable image)
    {
        setData(name, price_current, amount_max, amount_picked, null);

        if(image != null){
            binding.imgProduct.setImageDrawable(image);
        }
    }

    public void setData(String name, int price_current, int amount_max, int amount_picked, Bitmap image)
    {
        setAmount(amount_picked);
        this.amount_max = amount_max;
        if(name != null){
            binding.titlenameProduct.setText(name);
        }

        binding.priceProduct.setText(Helper.getMoneyString(price_current));
        if(image != null){
            binding.imgProduct.setImageBitmap(image);
        }
    }

    public void setAmount(int val)
    {
        if(this.amount==val)return;
        this.amount = val;
        binding.quantityProduct.setText(String.valueOf(amount));
        if(onListItemChangedListener != null)
        {
            onListItemChangedListener.onNumberProductChanged(this, amount);
        }
    }

    public void setChecked(boolean isChecked)
    {
        binding.cbxChoose.setChecked(isChecked);
    }

    public boolean getChecked()
    {
        return binding.cbxChoose.isChecked();
    }

    void initListener()
    {
        binding.btnIncreaseProduct.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount>=amount_max) return;
                setAmount(amount + 1);
            }
        });

        binding.btnDecreaseProduct.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount < 2) return;
                setAmount(amount - 1);
            }
        });

        binding.cbxChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onListItemChangedListener.onCheckProductChanged(Card_ItemView_New.this, isChecked);
            }
        });
    }


    public interface OnListItemChangedListener
    {
        void onCheckProductChanged(View view, boolean isChecked);
        void onNumberProductChanged(View view, int newNumber);
    }
}
