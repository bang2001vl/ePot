package exam.nlb2t.epot.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import exam.nlb2t.epot.R;

public class ItemCard_View_Thach extends BaseCustomViewGroup{

    public ItemCard_View_Thach(Context context) {
        super(context);
    }

    public ItemCard_View_Thach(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemCard_View_Thach(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ItemCard_View_Thach(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    TextView productName;
    TextView productQuantity;
    Button btn_decrease;
    Button btn_increase;
    TextView productPrice;
    ImageView img_product;
    CheckBox ckb_choose;

    int step = 1;

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.itemcard_template, this, false);

        productName = view.findViewById(R.id.name_Product);
        productQuantity = view.findViewById(R.id.quantity_Product);
        btn_decrease = view.findViewById(R.id.btn_decrease_product);
        btn_increase = view.findViewById(R.id.btn_increase_product);
        productPrice = view.findViewById(R.id.price_Product);
        img_product = view.findViewById(R.id.img_Product);
        ckb_choose = view.findViewById(R.id.cbx_choose);

        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(view, params);

        if (attrs!=null) {
//
//            Incomplete, maybe wait class product
//
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ItemCard_View);
            String ItemName = typedArray.getString(R.styleable.ItemCard_View_ItemName);
            int ItemQuantity = typedArray.getInt(R.styleable.ItemCard_View_ItemQuantity, 0);
            boolean isCheck = typedArray.getBoolean(R.styleable.ItemCard_View_ItemIsCheck, false);

//            Maybe some params here
            setData(ItemQuantity, isCheck);
            typedArray.recycle();
        }

        initListener();
    }

    private void setData(int quantity, boolean isCheck) {
//
//        Add data of product
//
        ckb_choose.setChecked(isCheck);
        productQuantity.setText(quantity);
    }

    private void initListener() {
        btn_increase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductChangeListener.OnQuantityProductChanged(v, step);
            }
        });
    }

    public void setIsCheck(boolean isCheck) {
        ckb_choose.setChecked(isCheck);
    }
    public boolean isCheck() {
        return ckb_choose.isChecked();
    }

    public interface OnItemChangeListener {
        void OnCheckProductChanged(View view, boolean isCheck);
        void OnQuantityProductChanged(View view, int quantity);
    }

    OnItemChangeListener onProductChangeListener = new OnItemChangeListener() {
        @Override
        public void OnCheckProductChanged(View view, boolean isCheck) {

        }

        @Override
        public void OnQuantityProductChanged(View view, int quantity) {

        }
    };

    public void setOnProductChangeListener(OnItemChangeListener listener) {
        onProductChangeListener = listener;
    }
}
