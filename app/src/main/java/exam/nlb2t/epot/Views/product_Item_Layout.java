package exam.nlb2t.epot.Views;

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

    public ImageView imagePro;
    public Tag_Salepro tag_salepro;
    public TextView tv_Namepro;
    public TextView tv_Pricepro;
    public TextView tv_Amountpro;
    int Size_tag;
    int Size;


    public product_Item_Layout(Context context) {
        super(context);
        Init(context, null);
        this.addOnLayoutChangeListener((v, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas) -> {

            imagePro.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, right - left, right - left));
            tag_salepro.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (bottom - top) / 3, (right - left) / 4));
        });

    }

    //@SuppressLint("UseCompatLoadingForDrawables")
    public product_Item_Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context, attrs);

        this.addOnLayoutChangeListener((v, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas) -> {

            imagePro.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, right - left, right - left));
            tag_salepro.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (bottom - top) / 3, (right - left) / 4));
        });
    }

    public product_Item_Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context, attrs);
        this.addOnLayoutChangeListener((v, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas) -> {

            imagePro.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, right - left, right - left));
            tag_salepro.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (bottom - top) / 3, (right - left) / 4));
        });
    }

    public product_Item_Layout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(context, attrs);
        this.addOnLayoutChangeListener((v, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas) -> {

            imagePro.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, right - left, right - left));
            tag_salepro.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (bottom - top) / 3, (right - left) / 4));
        });
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
            tag_salepro.SetTextSize(ta.getDimensionPixelSize(R.styleable.product_Item_Layout_Texttagsize, 15));
            tag_salepro.Setsize(ta.getDimensionPixelSize(R.styleable.product_Item_Layout_size_tag, 100));

        this.addView(layout_ViewGroup, layout_ViewGroup.getLayoutParams());

    }


}
