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
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductMyShop;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.ScrollCutom;
import exam.nlb2t.epot.databinding.MyShopProductTabBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class Shop_ProductFragment extends Fragment {
    MyShopProductTabBinding binding;
    Product_TabAdapter adapter;
    public List<ProductMyShop> products;

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

        binding.scrollViewMain.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if(adapter == null || adapter.getItemCount() == 0) return;
            ScrollView scrollView = binding.scrollViewMain;
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            if(view == null) return;
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff == 0) {
                adapter.addItemToList(adapter.mHandler);
            }
        });

        // First load
        adapter.addItemToList(adapter.mHandler);

//        mScroll = new ScrollCutom((LinearLayoutManager) binding.layoutProductMyShop.getLayoutManager()) {
//            @Override
//            public void loadNextPage(int index_item_end_list) {
//                Log.d("MY_TAG", "Load at index" + index_item_end_list);
//                adapter.addItemToList(index_item_end_list+1, 5, adapter.mHandler);
//            }
//
//            @Override
//            public void loadPreviousPage(int index_item_start_list) {
//
//            }
//
//            @Override
//            public void loadNextPageUI(int index_item_end_list) {
//                int offset = index_item_end_list + 1;
//                if (products.size() - offset < 5) setIsEndPage(true);
//                adapter.notifyItemRangeInserted(offset, products.size() - offset);
//            }
//        };

//        binding.layoutProductMyShop.setAdapter(adapter);
//        binding.layoutProductMyShop.addOnScrollListener(mScroll);
//        binding.layoutProductMyShop.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                //super.onScrolled(recyclerView, dx, dy);
//                if (dy>0) {
//                    //scroll down
//                    int visibleItemsCount = layout.getChildCount();
//                    int passVisibleItems = layout.findFirstVisibleItemPosition();
//                    int totalItemCount = layout.getItemCount();
//
//                    if ( passVisibleItems + visibleItemsCount >= totalItemCount - NUMBER_BEHIND_ITEM_IN_SCROLL) {
//                        //if (passVisibleItems + visibleItemsCount == totalItemCount)
//                            //wait for load data finished
//                            adapter.addItemToList(mhandler);
//                    }
//                }
//                if (dy<0) {
//                    //scroll up
//                }
//            }
//        });

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

            addProductFragment.show(getParentFragmentManager().beginTransaction(), "createProduct");
            addProductFragment.setOnSubmitOKListener(new Helper.OnSuccessListener() {
                @Override
                public void OnSuccess(Object sender) {
                    Toast.makeText(getContext(), "Tạo mới thành công", Toast.LENGTH_LONG).show();
                }
            });
        });

        getParentFragmentManager().setFragmentResultListener(Shop_BillFragment.NOTIFY_STATUS_CHANGED_TO_PRODUCT_FRAGMENT, Shop_ProductFragment.this, (requestKey, result) -> {
            int[] productIDs = result.getIntArray("ProductIDs");
            int[] quantities = result.getIntArray("Quantities");
            if (productIDs != null)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO: reload numbersold
                        synchronized (products) {
                            DBControllerProduct db = new DBControllerProduct();
                            for (int i = 0; i < productIDs.length; i++) {
                                for (int j = 0; j < products.size(); j++) {
                                    if (productIDs[i] == products.get(j).id) {
                                        //products.get(j).amount += quantities[i];
                                        products.get(j).amountSold -= quantities[i];
                                        break;
                                    }
                                }
                                db.updateQuantityProduct(productIDs[i], quantities[i]);
                            }
                            db.closeConnection();
                        }
                    }
                }).start();
        });
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
