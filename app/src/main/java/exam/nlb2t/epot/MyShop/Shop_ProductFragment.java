package exam.nlb2t.epot.MyShop;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductMyShop;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.ScrollCutom;
import exam.nlb2t.epot.Views.Success_toast;
import exam.nlb2t.epot.databinding.FragmentStartSellingBinding;
import exam.nlb2t.epot.databinding.MyShopProductTabBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class Shop_ProductFragment extends Fragment {
    MyShopProductTabBinding binding;
    Product_TabAdapter adapter;
    public List<ProductMyShop> products;

    public static String NOTIFY_WAREHOUSE_CHANGED = "NotifyWareHouseChanged";
    //Handler mhandler = new Handler(Looper.myLooper());

    private final int NUMBER_BEHIND_ITEM_IN_SCROLL = 2;
    private final int NUMBER_PREVIOUS_ITEM_IN_SCROLL = 5;

    public Shop_ProductFragment() {
        products = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyShopProductTabBinding.inflate(inflater, container, false);

        LinearLayoutManager layout = new LinearLayoutManager(container.getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.layoutProductMyShop.setLayoutManager(layout);

//        binding.layoutProductMyShop.setHasFixedSize(true);
        binding.layoutProductMyShop.setItemViewCacheSize(10);
        binding.layoutProductMyShop.setDrawingCacheEnabled(true);
        binding.layoutProductMyShop.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        adapter = new Product_TabAdapter(products);
        adapter.setHasStableIds(true);
        adapter.mHandler = new Handler(Looper.myLooper());
        binding.layoutProductMyShop.setAdapter(adapter);
        adapter.setOnLoadMoreSuccessListener(obj->{
            layoutData();
        });
        adapter.setOnUpdatedListener(obj->{
            Success_toast.show(getContext(), "Thay đổi thành công", true);
            getParentFragmentManager().setFragmentResult(NOTIFY_WAREHOUSE_CHANGED, new Bundle());
        });
        adapter.setOnDeletedListener(obj->{
            Success_toast.show(getContext(), "Ngừng bán thành công", true);
        });

        binding.scrollViewMain.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if(adapter == null || adapter.getItemCount() == 0) return;
            ScrollView scrollView = binding.scrollViewMain;
            ViewGroup viewG = (ViewGroup) scrollView.getChildAt(scrollView.getChildCount() - 1);
            View view = viewG.getChildAt(viewG.getChildCount() - 1);
            if(view == null) return;
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff == 0) {
                adapter.addItemToList(adapter.mHandler);
            }
        });

        emptyBinding = null;

        // First load
        adapter.lastIndex = 0;
        adapter.addItemToList(adapter.mHandler);

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

            addProductFragment.show(getChildFragmentManager(), "createProduct");
            addProductFragment.setOnSubmitOKListener(new Helper.OnSuccessListener() {
                @Override
                public void OnSuccess(Object sender) {
                    Success_toast.show(getContext(), "Tạo mới thành công", true);
                    reloadData();
                }
            });
        });

        getParentFragmentManager().setFragmentResultListener(Shop_BillFragment.NOTIFY_STATUS_CHANGED_TO_PRODUCT_FRAGMENT, Shop_ProductFragment.this, (requestKey, result) -> {
            int[] productIDs = result.getIntArray("ProductIDs");
            int[] quantities = result.getIntArray("Quantities");
            boolean isCancelBill = result.getInt("ToStatus") == BillBaseDB.BillStatus.DEFAULT.getValue();
            if (productIDs != null && isCancelBill)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO: reload numbersold if cancel bill
                        synchronized (products) {
                            DBControllerProduct db = new DBControllerProduct();
                            for (int i = 0; i < productIDs.length; i++) {
                                for (int j = 0; j < products.size(); j++) {
                                    if (productIDs[i] == products.get(j).id) {
                                        int i2 = i;
                                        int j2 = j;
                                        //products.get(j).amount += quantities[i];
                                        new Handler(Looper.getMainLooper()).post(()->{
                                            products.get(j2).amountSold -= quantities[i2];
                                            products.get(j2).amount -= quantities[i2];
                                            if (adapter!=null) adapter.notifyItemChanged(j2);
                                        });
                                        db.updateQuantityProduct(productIDs[i], quantities[i]);
                                        break;
                                    }
                                }

                            }
                            db.closeConnection();
                        }
                    }
                }).start();
        });
    }

    void reloadData(){
        int oldLength = adapter.products.size();
        if(oldLength > 0) {
            adapter.products.clear();
            adapter.notifyItemRangeRemoved(0, oldLength);
        }
        adapter.lastIndex = 0;
        adapter.isfullProducts = false;
        getActivity().runOnUiThread(()->adapter.addItemToList(adapter.mHandler));
    }

    FragmentStartSellingBinding emptyBinding;
    void layoutData(){
        if(adapter.products.size() == 0){
            if(emptyBinding == null){
                emptyBinding = FragmentStartSellingBinding.inflate(getLayoutInflater(), binding.getRoot(), false);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                binding.getRoot().addView(emptyBinding.getRoot(), binding.getRoot().getChildCount(), params);
            }
            return;
        }

        if(emptyBinding != null){
            binding.getRoot().removeView(emptyBinding.getRoot());
            emptyBinding = null;
        }
    }

//    private boolean isLoadMoreData() {
//        return mhandler.hasMessages(1);
//    }
//
//    private final int NUMBER_ITEM_TO_LOAD = 5;
//    private int totalNumberPage;
//    /**
//     * Load new product when scroll recycler view
//     * @param pageNumber the page number wants to load, pagenumber start at 0
//     * @param state state scroll, 1 when down, 0 when up
//     */
//    synchronized private void loadPage(int pageNumber, int state) {
//        if (state == 1 && pageNumber > 2) {
//            //remove page at position pageNumber - 2
//        }
//        else if (state == 0 && pageNumber < totalNumberPage - 1) {
//            //remove page at position totalNumberPage - 1
//        }
//    }
}
