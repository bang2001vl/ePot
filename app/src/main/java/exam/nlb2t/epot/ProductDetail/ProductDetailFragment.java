package exam.nlb2t.epot.ProductDetail;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.Locale;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.databinding.FragmentProductDetailBinding;
import exam.nlb2t.epot.singleton.Helper;

public class ProductDetailFragment extends Fragment {
    FragmentProductDetailBinding binding;
    public int productID;
    ProductBaseDB product;
    int imageCount = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        ImageViewAdapter adpater = new ImageViewAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, getContext());
        binding.viewpaperProductDetail.setAdapter(adpater);
        binding.viewpaperProductDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.txtNumberViewpaperProductDetail.setText(
                        String.format(Locale.getDefault(),"%d/%d",position + 1, adpater.getCount())
                );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getImagesFromDB();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getProductFromDB();
    }

    void getProductFromDB()
    {
        if(getActivity() == null || getContext() == null) return;
        Activity activity = getActivity();
        Context context = getContext();
        Handler handler = new Handler();
        Runnable runnable = () -> {
            // Get data in background
            DBControllerProduct db = new DBControllerProduct();
            ProductBaseDB data = db.getProduct(productID);
            db.closeConnection();

            // Update UI when done
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProduct(context, data);
                }
            });
        };
        handler.post(runnable);
    }

    public void setProduct(Context context, ProductBaseDB product)
    {
        this.product = product;
        binding.txtProductNameProductDetail.setText(product.name);
        binding.txtDescriptionProductDetail.setText(product.description);
        binding.txtSaledAlreadyProductDetail.setText(product.amountSold+"");
        Helper helper = new Helper(context);
        if(product.price < product.priceOrigin)
        {
            binding.txtPriceNormalProductDetail.setText(helper.getPrice(product.price));

            TextView t = binding.txtPriceSaleProductDetail;
            t.setPaintFlags(t.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            t.setText(helper.getPrice(product.priceOrigin));
        }
        else
        {
            binding.txtPriceSaleProductDetail.setVisibility(View.GONE);
            binding.txtPriceNormalProductDetail.setText(helper.getPrice(product.price));
        }
    }

    void getImagesFromDB()
    {
        if(getActivity() == null) return;
        Activity activity = getActivity();
        Handler handler = new Handler();
        Runnable runnable = () -> {
            // Get data in background
            DBControllerProduct db = new DBControllerProduct();
            List<Bitmap> data = db.getImages(productID);
            db.closeConnection();

            // Update UI when done
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setImages(data);
                }
            });
        };
        handler.post(runnable);
    }

    public void setImages(List<Bitmap> bitmaps)
    {
        ImageViewAdapter adapter = (ImageViewAdapter) binding.viewpaperProductDetail.getAdapter();
        adapter.setBitmaps(bitmaps);
        binding.txtNumberViewpaperProductDetail.setText(
                String.format(Locale.getDefault(),"%d/%d",1, adapter.getCount())
        );
    }
}
