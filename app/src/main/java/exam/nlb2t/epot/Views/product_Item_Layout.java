package exam.nlb2t.epot.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import exam.nlb2t.epot.R;

public class product_Item_Layout extends LinearLayout {

    LinearLayout layout_ViewGroup;
    FrameLayout layout_tag_image;
    RelativeLayout layout_price_amountsold;

    ImageView imagePro;
    Tag_Salepro tag_salepro;
    TextView tv_Namepro;
    TextView tv_Pricepro;
    TextView tv_Amountpro;
    int Size;


    public product_Item_Layout(Context context) {
        super(context);
        Init(context, null);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public product_Item_Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context, attrs);
    }

    public product_Item_Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context, attrs);
    }

    public product_Item_Layout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(context, attrs);
    }

    private void Init(Context context, AttributeSet attrs)
    {

        layout_ViewGroup = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.product_item_layout, this, false);

        imagePro = layout_ViewGroup.findViewById(R.id.Image_Product);
        tag_salepro = layout_ViewGroup.findViewById(R.id.Tag_Salepro);
        tv_Amountpro = layout_ViewGroup.findViewById(R.id.textview_proSold);
        tv_Namepro = layout_ViewGroup.findViewById(R.id.textview_proName);
        tv_Pricepro = layout_ViewGroup.findViewById(R.id.textview_proPrice);


        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.product_Item_Layout);


            Size = ta.getDimensionPixelSize(R.styleable.product_Item_Layout_Size, 0);
            tag_salepro.Text = ta.getString(R.styleable.product_Item_Layout_Text_tagpro);
            tv_Namepro.setText(ta.getString(R.styleable.product_Item_Layout_Name_pro));
            tv_Pricepro.setText(ta.getString(R.styleable.product_Item_Layout_Price_pro));
            tv_Amountpro.setText(ta.getString(R.styleable.product_Item_Layout_Amount_proSold));
            imagePro.setImageResource(ta.getResourceId(R.styleable.product_Item_Layout_Image_pro, R.mipmap.mango));

        this.addView(layout_ViewGroup, layout_ViewGroup.getLayoutParams());
    }
}
