package exam.nlb2t.epot.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import exam.nlb2t.epot.R;

public class ItemCard_View_Thach extends BaseCustomViewGroup{

    public ItemCard_View_Thach(Context context) {
        super(context);
        init(context, null);
    }

    public ItemCard_View_Thach(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ItemCard_View_Thach(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ItemCard_View_Thach(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    TextView productName;
    TextView productQuantity;
    ImageButton btn_decrease;
    ImageButton btn_increase;
    TextView productPrice;
    ImageView img_product;
    CheckBox ckb_choose;

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.itemcard_template, this, false);

        productName = view.findViewById(R.id.titlename_Product);
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

            float ImgSize = typedArray.getDimension(R.styleable.ItemCard_View_ItemImgSize, getResources().getDimension(R.dimen.ProductImgSize));
            float TextSize = typedArray.getDimension(R.styleable.ItemCard_View_ItemTextSize, getResources().getDimension(R.dimen.ProductTextSize)/getResources().getDisplayMetrics().density);

//            Maybe some params here
            setData(ItemQuantity, isCheck, ImgSize, TextSize);
            typedArray.recycle();
        }

        initListener();
    }

    private void setData(int quantity, boolean isCheck, float imgsize, float textsize) {
//
//        Add data of product
//
        ckb_choose.setChecked(isCheck);
        productQuantity.setText(String.valueOf(quantity));

        productName.setTextSize((int)textsize);
        productQuantity.setTextSize((int)textsize);
        productPrice.setTextSize((int)textsize);

        img_product.getLayoutParams().height=(int)imgsize;
        img_product.getLayoutParams().width=(int)imgsize;
        img_product.requestLayout();
    }

    private void initListener() {
        btn_increase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductChangeListener.OnQuantityProductChanged(ItemCard_View_Thach.this, 1);
            }
        });
        btn_decrease.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductChangeListener.OnQuantityProductChanged(ItemCard_View_Thach.this, -1);
            }
        });
        ckb_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onProductChangeListener.OnCheckProductChanged(ItemCard_View_Thach.this, isChecked);
            }
        });
    }

    public void setIsCheck(boolean isCheck) {
        ckb_choose.setChecked(isCheck);
    }
    public boolean isCheck() {
        return ckb_choose.isChecked();
    }

    public interface OnProductChangeListener {
        void OnCheckProductChanged(View view, boolean isCheck);
        void OnQuantityProductChanged(View view, int increasement);
    }

    OnProductChangeListener onProductChangeListener = new OnProductChangeListener() {
        @Override
        public void OnCheckProductChanged(View view, boolean isCheck) {

        }

        @Override
        public void OnQuantityProductChanged(View view, int quantity) {

        }
    };

    public void setOnProductChangeListener(OnProductChangeListener listener) {
        onProductChangeListener = listener;
    }
}
