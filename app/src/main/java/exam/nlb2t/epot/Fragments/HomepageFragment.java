package exam.nlb2t.epot.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import exam.nlb2t.epot.Category.Category;
import exam.nlb2t.epot.Category.CategoryAdapter;
import exam.nlb2t.epot.Category.DBControllerCategory;
import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.OnItemClickListener;
import exam.nlb2t.epot.ProductAdapterItemInfo;
import exam.nlb2t.epot.ProductDetail.ProductDetailFragment;
import exam.nlb2t.epot.Views.Item_product_container.ProductAdapter;
import exam.nlb2t.epot.Views.Search_Product.fragment_search;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;
import exam.nlb2t.epot.fragment_ProItem_Container;
import exam.nlb2t.epot.singleton.Authenticator;

public class HomepageFragment extends Fragment implements OnItemClickListener {
    HomeShoppingBinding binding;

    private CategoryAdapter categoryAdapter;
    private ProductAdapter adapter_new;
    private ProductAdapter adapter_TopSold;

    private androidx.appcompat.widget.SearchView searchView;

    List<Category> list_Categoty;
    List<ProductAdapterItemInfo> list_TopSold;
    List<ProductAdapterItemInfo> list_New;

    int step = 10;
    int currentLastIndex = 1;
    boolean hasMoreData = true;

    public HomepageFragment()
    {
        list_New = new ArrayList<>();
        list_TopSold = new ArrayList<>();
        list_Categoty = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeShoppingBinding.inflate(inflater, container, false);

        //category
        categoryAdapter = new CategoryAdapter(getContext(),list_Categoty,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL, false);
        binding.recycleViewCategory.setLayoutManager(linearLayoutManager);
        binding.recycleViewCategory.setAdapter(categoryAdapter);

        adapter_new = new ProductAdapter(list_New, getContext());
        setupProductRecycleview(binding.fragmentProductNew, adapter_new);

        adapter_TopSold = new ProductAdapter(list_TopSold, getContext());
        setupProductRecycleview(binding.fragmentProductTopSold, adapter_TopSold);

        binding.stickyScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if(list_New.size() < 1){return;}
            ScrollView scrollView = binding.stickyScrollView;
            ViewGroup viewG = (ViewGroup) scrollView.getChildAt(scrollView.getChildCount() - 1);
            if(viewG == null) return;
            View view = viewG.getChildAt(viewG.getChildCount() - 1);
            if(view == null) return;
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff <= 00 && hasMoreData) {
                showLoading();
                Log.d("My_TAG", "Call load more from Scroll");
                loadMoreData_New();
            }
        });

        hideLoading();
        LoadFirstData();
        return binding.getRoot();
    }

    void setupProductRecycleview(RecyclerView recyclerView, ProductAdapter adapter)
    {
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = binding.searchBar;
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                submitQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.buttonRefeshNew.setOnClickListener(v-> reload_New());
        binding.buttonRefeshTopSold.setOnClickListener(v-> reload_TopSold());

        adapter_new.setOnItemClickListener(onClickItemListener);
        adapter_TopSold.setOnItemClickListener(onClickItemListener);
    }

    void submitQuery(String query) {
        fragment_search dialog = new fragment_search(query);
        dialog.show(getChildFragmentManager(), "search");
    }

    @Override
    public void onItemClickCategory(String string) {
        searchView.setQuery(String.format(Locale.getDefault(), "Danh mục: %s", string), true);
    }

    @Override
    public void onItemClickProduct(int id) {
        //Toast.makeText(getContext(),"id: "+id ,Toast.LENGTH_LONG).show();
    }

    fragment_ProItem_Container.OnClickItemListener onClickItemListener = new fragment_ProItem_Container.OnClickItemListener() {
        @Override
        public void onClick(int position, int productID) {

            Log.d("MY_TAG", "Open product with id = " + productID);
            ProductDetailFragment dialog = new ProductDetailFragment();
            dialog.productID = productID;
            dialog.show(getChildFragmentManager(), "detailProduct");
        }
    };

    public void LoadFirstData()
    {
        Log.d("My_TAG", "First load");

        reloadCategory();

        reload_TopSold();
        reload_New();
    }

    void reloadCategory()
    {
        int oldLength = list_Categoty.size();
        if(oldLength > 0)
        {
            list_Categoty.clear();
            if(categoryAdapter != null){categoryAdapter.notifyItemRangeRemoved(0, oldLength);}
        }

        new Thread(() -> {
            List<Category> data = getListCategory();
            if(getActivity() != null)
            {
                getActivity().runOnUiThread(() -> {
                    list_Categoty.addAll(data);
                    if(categoryAdapter != null)
                        categoryAdapter.notifyItemRangeInserted(0, data.size());
                });
            }
        }).start();
    }

    private List<Category> getListCategory(){
        DBControllerCategory db = new DBControllerCategory();
        List<Category> list = db.getCategoriesList_withoutImage();
        db.closeConnection();
        return  list;
    }

    public void reload_TopSold() {
        Log.d("MY_TAG", "Call refresh list_TopSold");
        int oldLength = list_TopSold.size();
        if(oldLength > 0) {
            list_TopSold.clear();
            if (adapter_TopSold != null) {
                adapter_TopSold.notifyItemRangeRemoved(0, oldLength);
            }
        }

        binding.fragmentProductTopSold.removeAllViews();

        new Thread(() -> {
            final List<ProductAdapterItemInfo> data = getDataMaxSold();
            Log.d("MY_TAG", "topSold = " + data.size());
            new Handler(Looper.getMainLooper()).post(() -> {
                list_TopSold.addAll(data);
                Log.d("MY_TAG", "topSold = " + list_TopSold.size());
                if (adapter_TopSold != null) {
                    adapter_TopSold.notifyItemRangeInserted(list_TopSold.size() - data.size(), data.size());
                }
            });
        }, "LoadProductTopSold").start();
    }

    private List<ProductAdapterItemInfo> getDataMaxSold() {
        String sql = "SELECT TOP 8 PRODUCT.ID, SALER_ID, CATEGORY_ID, NAME, PRICE, PRICE_ORIGIN, AMOUNT, " +
                "AMOUNT_SOLD, PRIMARY_IMAGE_ID, DETAIL, CREATED_DATE, DELETED, STAR_AVG " +
                "FROM PRODUCT ORDER BY AMOUNT_SOLD DESC, CREATED_DATE DESC";
        DBControllerProduct dbControllerProduct = new DBControllerProduct();
        List<ProductBaseDB> subpro = dbControllerProduct.getNewProductList(sql);
        List<ProductAdapterItemInfo> list = new ArrayList<>(subpro.size());
        for(ProductBaseDB p: subpro)
        {
            ProductAdapterItemInfo info = new ProductAdapterItemInfo();
            info.productBaseDB = p;
            info.isLiked = dbControllerProduct.checkLikeProduct(p.id, Authenticator.getCurrentUser().id);
            info.ratingCount = dbControllerProduct.getCountRating(p.id);

            // Image would be get later
            info.productAvatar = null;
            list.add(info);
        }
        dbControllerProduct.closeConnection();
        return  list;
    }

    public void reload_New() {
        int oldLength = list_New.size();
        if(oldLength > 0) {
            list_New.clear();
            if(adapter_new != null) {
                adapter_new.notifyItemRangeRemoved(0, oldLength);
            }
        }

        currentLastIndex = 1;
        hasMoreData = true;
        Log.d("My_TAG", "CAll load more from click refresh");
        loadMoreData_New();
    }

    public void loadMoreData_New()
    {
        if(!hasMoreData) return;
        Log.d("My_TAG", "On load more");
        hasMoreData = false;
        new Thread(() -> {
            List<ProductAdapterItemInfo> data = getMoreData();
            Log.d("MY_TAG", "i before = " + data.size());
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    list_New.addAll(data);
                    if(adapter_new != null && data.size() > 0) {
                        adapter_new.notifyItemRangeInserted(list_New.size() - data.size(), data.size());
                    }
                    hasMoreData = data.size() == step;
                    currentLastIndex += data.size();
                    hideLoading();
                });
            }
        }, "LoadNewProduct").start();
    }

    public List<ProductAdapterItemInfo> getMoreData()
    {
        String sql = "SELECT ID, SALER_ID, CATEGORY_ID, NAME, PRICE, PRICE_ORIGIN, AMOUNT, AMOUNT_SOLD, PRIMARY_IMAGE_ID, DETAIL, CREATED_DATE, DELETED, STAR_AVG from " +

                "(SELECT product.ID, SALER_ID, CATEGORY_ID, NAME, PRICE, PRICE_ORIGIN, AMOUNT, AMOUNT_SOLD, " +
                "PRIMARY_IMAGE_ID, DETAIL, CREATED_DATE, DELETED, STAR_AVG, ROW_NUMBER() OVER(ORDER BY CREATED_DATE  DESC) AS STT " +
                "FROM PRODUCT where DATEDIFF(DAY,PRODUCT.CREATED_DATE, GETDATE()) < 70) as tamp " +
                "where tamp.STT BETWEEN " + currentLastIndex + " AND " + (currentLastIndex + step -1);

        DBControllerProduct dbControllerProduct = new DBControllerProduct();
        List<ProductBaseDB> subpro = dbControllerProduct.getNewProductList(sql);
        List<ProductAdapterItemInfo> list = new ArrayList<>(subpro.size());
        for(ProductBaseDB p: subpro)
        {
            ProductAdapterItemInfo info = new ProductAdapterItemInfo();
            info.productBaseDB = p;
            info.isLiked = dbControllerProduct.checkLikeProduct(p.id, Authenticator.getCurrentUser().id);
            info.ratingCount = dbControllerProduct.getCountRating(p.id);

            // Image would be get later
            info.productAvatar = null;
            list.add(info);
        }

        return list;
    }

    protected void showLoading()
    {
        binding.gifLoadingCircle.setEnabled(true);
        binding.gifLoadingCircle.setVisibility(View.VISIBLE);
    }

    protected void hideLoading()
    {
        binding.gifLoadingCircle.setEnabled(false);
        binding.gifLoadingCircle.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
