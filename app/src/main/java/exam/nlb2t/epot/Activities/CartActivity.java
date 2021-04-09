package exam.nlb2t.epot.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;
import java.util.List;

import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.Fragments.CartFragment;

public class CartActivity extends AppCompatActivity {
    ProductBuyInfo[] products;

    public CartActivity() {
        init(this);
    }



    Context mContext;
    void init(Context context)
    {
        this.mContext = context;
    }

    public void setProducts(List<ProductBuyInfo> productBuyInfos)
    {
        this.products = new ProductBuyInfo[productBuyInfos.size()];
        productBuyInfos.toArray(this.products);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        CartFragment cartFragment = CartFragment.newInstance(products);
        FrameLayout frameLayout = new FrameLayout(mContext);

        setContentView(frameLayout);

        View view = cartFragment.onCreateView(getLayoutInflater(), frameLayout, null);
        frameLayout.addView(view);
    }

    public class ProductBuyInfo extends exam.nlb2t.epot.ClassInformation.ProductBuyInfo implements Serializable {

    }
}
