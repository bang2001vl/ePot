package exam.nlb2t.epot.MyShop;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.MyShopProductTabBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class Shop_ProductFragment extends Fragment {
    MyShopProductTabBinding binding;
    Product_TabAdapter adapter;
    public final List<ProductBaseDB> products;

    Handler mhandler = new Handler(Looper.getMainLooper());

    private final int NUMBER_BEHIND_ITEM_IN_SCROLL = 2;
    private final int NUMBER_PREVIOUS_ITEM_IN_SCROLL = 5;
    private final int NUMBER_ITEM_TO_LOAD = 5;

    public Shop_ProductFragment() {
        DBControllerProduct db = new DBControllerProduct();
        products = db.getLIMITProduct(Authenticator.getCurrentUser().id, 0, 8);
        db.closeConnection();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyShopProductTabBinding.inflate(inflater, container, false);

        adapter = new Product_TabAdapter(products);
        adapter.setHasStableIds(true);

        LinearLayoutManager layout = new LinearLayoutManager(container.getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.layoutProductMyShop.setLayoutManager(layout);

        binding.layoutProductMyShop.setItemViewCacheSize(8);
        binding.layoutProductMyShop.setDrawingCacheEnabled(true);
        binding.layoutProductMyShop.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        binding.layoutProductMyShop.setAdapter(adapter);
        binding.layoutProductMyShop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemsCount = layout.getChildCount();
                int passVisibleItems = layout.findFirstVisibleItemPosition();
                int totalItemCount = layout.getItemCount();

                if (passVisibleItems + visibleItemsCount >= totalItemCount - NUMBER_BEHIND_ITEM_IN_SCROLL) {
                    if (passVisibleItems + visibleItemsCount == totalItemCount)
                        while (mhandler.hasMessages(1)) ;
                    if (!mhandler.hasMessages(1)) {
                        adapter.addItemToList(totalItemCount, NUMBER_ITEM_TO_LOAD, mhandler);
                    }
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEventHandler();
    }

    void setEventHandler() {
        binding.buttonAddProduct.setOnClickListener(v -> {
            //TODO: Add product to My Shop
            AddProductFragment addProductFragment = new AddProductFragment();

            if (getFragmentManager() == null) {
                return;
            }

            addProductFragment.show(getFragmentManager().beginTransaction(), "createProduct");
            addProductFragment.setOnSubmitOKListener(new Helper.OnSuccessListener() {
                @Override
                public void OnSuccess(Object sender) {
                    Toast.makeText(getContext(), "Tạo mới thành công", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
