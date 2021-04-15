package exam.nlb2t.epot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.ClassInformation.ProductBuyInfoParcel;
import exam.nlb2t.epot.Fragments.CartFragment;
import exam.nlb2t.epot.R;

public class CartActivity extends AppCompatActivity {
    ProductBuyInfo[] products;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        init();
    }

    public static final String NAME_PARCEL = "parcel";
    void init()
    {
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        if(intent != null)
        {
            ArrayList<ProductBuyInfoParcel> parcels = intent.getParcelableArrayListExtra(NAME_PARCEL);
            if(parcels != null)
            {
                products = new ProductBuyInfo[parcels.size()];
                for(int i = 0; i<products.length; i++)
                {
                    products[i] = new ProductBuyInfo(parcels.get(i));
                }
                CartFragment cartFragment = CartFragment.newInstance(products);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cartFragment).commit();
            }
            else {
                Log.d("MY_DEBUG", "Intent.parcels is null");
            }
        }
    }

}
