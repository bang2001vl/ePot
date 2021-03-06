package exam.nlb2t.epot.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.chip.ChipGroup;

import exam.nlb2t.epot.R;

public class MyShopOverview_Item extends BaseCustomViewGroup {

    public MyShopOverview_Item(Context context) {
        super(context);
        initCustomView(context,null);
    }

    public MyShopOverview_Item(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCustomView(context,attrs);
    }

    public MyShopOverview_Item(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomView(context,attrs);
    }

    public MyShopOverview_Item(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCustomView(context,attrs);
    }

    TextView ItemValueView;
    TextView ItemNameView;

    public void initCustomView(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_shop_overview_item, this, false);

        ItemValueView = view.findViewById(R.id.my_shop_overview_item_value);
        ItemNameView = view.findViewById(R.id.my_shop_overview_item_name);

        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(view, params);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyShopOverview_Item);
            String name = typedArray.getString(R.styleable.MyShopOverview_Item_OverviewName);
            int value = typedArray.getInt(R.styleable.MyShopOverview_Item_OverviewValue, 0);
            float size = typedArray.getDimension(R.styleable.MyShopOverview_Item_OverviewSize, getResources().getDimension(R.dimen.my_shop_overview_item_size));

            if (name == null) name = "Name of item";
            setData(name,value,size);
            typedArray.recycle();
        }
    }

    private void setData(String name, int value, float size) {
        ItemValueView.setTextSize(size/getResources().getDisplayMetrics().density);
        ItemNameView.setTextSize(size/getResources().getDisplayMetrics().density);

        ItemValueView.setText(String.valueOf(value));
        ItemNameView.setText(name);
    }

    public void setValue(int value) {
        ItemValueView.setText(String.valueOf(value));
    }

    public void setName(String name) {
        ItemNameView.setText(name);
    }
}
