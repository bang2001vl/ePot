package exam.nlb2t.epot.MyShop;

import android.app.Activity;
import android.os.Bundle;
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

import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.MyShopProductTabBinding;
import exam.nlb2t.epot.singleton.Helper;

public class Shop_ProductFragment extends Fragment {
    MyShopProductTabBinding binding;
    Product_TabAdapter adapter;

    private final int NUMBER_BEHIND_ITEM_IN_SCROLL = 0;
    private final int NUMBER_PREVIOUS_ITEM_IN_SCROLL = 5;
    private final int NUMBER_ITEM_TO_LOAD = 8;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyShopProductTabBinding.inflate(inflater, container, false);
        setEventHandler();

        LinearLayoutManager layout = new LinearLayoutManager(container.getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.layoutProductMyShop.setLayoutManager(layout);

        //TODO: Find UserID to login app
        adapter = new Product_TabAdapter();
        //adapter.setHasStableIds(true);

        binding.layoutProductMyShop.setItemViewCacheSize(10);
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
                    adapter.addItemToList(NUMBER_ITEM_TO_LOAD);
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : Write code here <Get data from database and set to view>
    }

    void setEventHandler()
    {
        binding.buttonAddProduct.setOnClickListener(v -> {
            AddProductFragment addProductFragment = new AddProductFragment();

            if(getFragmentManager() == null){return;}

            addProductFragment.show(getFragmentManager().beginTransaction(), "createProduct");
            addProductFragment.setOnSubmitOKListener(new Helper.OnSuccessListener() {
                @Override
                public void OnSuccess(Object sender) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = -1;
        try {
            position = adapter.getPosition();
        }
        catch (Exception e) {
            e.printStackTrace();
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.my_shop_context_menu_item:
                SaleDialog dialog = new SaleDialog(adapter.products.get(position));
                dialog.show(getChildFragmentManager(), "SaleDialog");
                break;
        }
        return super.onContextItemSelected(item);
    }
}
