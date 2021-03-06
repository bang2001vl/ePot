package exam.nlb2t.epot.Views.Item_product_container;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import exam.nlb2t.epot.R;

public class product_Item_Layout extends LinearLayout {

    LinearLayout layout_ViewGroup;

    public ImageView imagePro;
    public TextView tv_Oldproprice;
    public TextView tv_Namepro;
    public TextView tv_Pricepro;
    public TextView tv_Amountpro;
    public TextView tag_salepro;

    int Size;


    public product_Item_Layout(Context context) {
        super(context);
        Init(context, null);
        this.addOnLayoutChangeListener((v, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas) -> {

                   });

    }

    //@SuppressLint("UseCompatLoadingForDrawables")
    public product_Item_Layout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context, attrs);

        this.addOnLayoutChangeListener((v, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas) -> {

                   });
    }

    public product_Item_Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context, attrs);
        this.addOnLayoutChangeListener((v, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas) -> {
                  });
    }

    public product_Item_Layout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(context, attrs);
        this.addOnLayoutChangeListener((v, left, top, right, bottom, leftWas, topWas, rightWas, bottomWas) -> {
        });
    }

    /*@SuppressLint("SetTextI18n")
    public void Set_value(Product product)
    {
        String price = product.originPrice + " đ";
        SpannableString oldproprice = new SpannableString(price);
        oldproprice.setSpan(new StrikethroughSpan(), 0, (price).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        this.tv_Oldproprice.setText(oldproprice );
        this.imagePro.setImageBitmap(product.mainImage);
        this.tv_Pricepro.setText(product.currentPrice +" đ ");
        this.tv_Namepro.setText(" " + product.productName + " ");
        this.tv_Amountpro.setText("Đã bán " + product.numberSold);
        this.tag_salepro.setText(" -" + (int) (product.currentPrice*100/ product.originPrice)+ "% ");

        postInvalidate();
    }*/
    private void Init(Context context, AttributeSet attrs)
    {

        layout_ViewGroup = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.product_item_layout, this, false);

        imagePro = layout_ViewGroup.findViewById(R.id.Image_Product);
        tv_Oldproprice = layout_ViewGroup.findViewById(R.id.textview_OldproPrice);
        tv_Amountpro = layout_ViewGroup.findViewById(R.id.textview_proSold);
        tv_Namepro = layout_ViewGroup.findViewById(R.id.textview_proName);
        tv_Pricepro = layout_ViewGroup.findViewById(R.id.textview_proPrice);
        tag_salepro = layout_ViewGroup.findViewById(R.id.tv_tag_salepro);


        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.product_Item_Layout);


            Size = ta.getDimensionPixelSize(R.styleable.product_Item_Layout_Size, 0);
            tag_salepro.setText(" -" + ta.getString(R.styleable.product_Item_Layout_Text_tagpro) +"% ");
            tv_Namepro.setText(" " + ta.getString(R.styleable.product_Item_Layout_Name_pro) + " " );
            tv_Pricepro.setText(ta.getString(R.styleable.product_Item_Layout_Price_pro) + " đ ");
            tv_Amountpro.setText("Đã bán " + ta.getString(R.styleable.product_Item_Layout_Amount_proSold) +" ");
            imagePro.setImageResource(ta.getResourceId(R.styleable.product_Item_Layout_Image_pro, R.mipmap.mango));

        this.addView(layout_ViewGroup, layout_ViewGroup.getLayoutParams());
        ta.recycle();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        imagePro.getLayoutParams().height = imagePro.getLayoutParams().width = w;
        tag_salepro.setTextSize(w*0.035f);
        tag_salepro.getLayoutParams().width = tag_salepro.getText().length();
        tv_Namepro.setTextSize(w*0.025f);

        tv_Oldproprice.setTextSize(w*0.025f);
        tv_Pricepro.setTextSize(w*0.025f);
        tv_Amountpro.setTextSize(w*0.025f - 1);
    }
}
