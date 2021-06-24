package exam.nlb2t.epot.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
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
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.Search_Product.fragment_search;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;
import exam.nlb2t.epot.fragment_ProItem_Container;
import exam.nlb2t.epot.singleton.Authenticator;

public class HomepageFragment extends Fragment implements OnItemClickListener {
    HomeShoppingBinding binding;

    private RecyclerView rcVCategory;
    private CategoryAdapter categoryAdapter;

    private fragment_ProItem_Container fragment_new;
    private fragment_ProItem_Container fragment_topSold;

    private androidx.appcompat.widget.SearchView searchView;

    List<Category> list_Categoty;
    List<ProductAdapterItemInfo> list_TopSold;
    List<ProductAdapterItemInfo> list_New;

    int step = 10;
    int currentLastIndex = 1;
    boolean hasMoreData = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeShoppingBinding.inflate(inflater, container, false);

        //category
        rcVCategory = binding.recycleViewCategory;
        categoryAdapter = new CategoryAdapter(getContext(),list_Categoty,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL, false);
        rcVCategory.setLayoutManager(linearLayoutManager);
        rcVCategory.setAdapter(categoryAdapter);

        fragment_new = fragment_ProItem_Container.newInstance(list_New);
        fragment_new.hideSpinner = true;
        fragment_new.canScroll = false;
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_product_new, fragment_new).commit();


        fragment_topSold = fragment_ProItem_Container.newInstance(list_TopSold);
        fragment_topSold.hideSpinner = true;
        fragment_topSold.canScroll = false;
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_product_top_sold, fragment_topSold).commit();

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
        return binding.getRoot();
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
        binding.buttonRefeshNew.setOnClickListener(v->onClickRefresh_New());
        binding.buttonRefeshTopSold.setOnClickListener(v->onClickRefresh_TopSold());

        fragment_new.setOnClickItemListener(onClickItemListener);

        fragment_topSold.setOnClickItemListener(onClickItemListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        hideLoading();
        LoadFirstData();
    }

    public void LoadFirstData()
    {
        Log.d("My_TAG", "First load");
        list_Categoty.addAll(getListCategory());
        if(categoryAdapter != null){
            categoryAdapter.notifyItemRangeInserted(list_Categoty.size() -1, list_Categoty.size());
        }

        onClickRefresh_New();
        onClickRefresh_TopSold();
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

    private List<Category> getListCategory(){
        DBControllerCategory db = new DBControllerCategory();
        List<Category> list = db.getCategoriesList_withoutImage();
        db.closeConnection();
        return  list;
    }

    void submitQuery(String query) {
        fragment_search dialog = new fragment_search(query);
        dialog.show(getChildFragmentManager(), "search");
    }

    public void onClickRefresh_New() {
        int oldLength = list_New.size();
        if(oldLength > 0) {
            list_New.clear();
            if(fragment_new != null && fragment_new.productAdapter != null) {
                fragment_new.productAdapter.notifyItemRangeRemoved(0, oldLength);
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
                    if(fragment_new != null && data.size() > 0) {
                        fragment_new.productAdapter.notifyItemRangeInserted(list_New.size() - data.size(), data.size());
                    }
                    hideLoading();
                });
            }
            hasMoreData = data.size() == step;
            currentLastIndex += data.size();
        }, "LoadNewProduct").start();
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

    public void onClickRefresh_TopSold() {
        Log.d("MY_TAG", "Call refresh list_TopSold");
        int oldLength = list_TopSold.size();
        if(oldLength > 0) {
            list_TopSold.clear();

            if (fragment_topSold != null && fragment_topSold.productAdapter != null) {
                fragment_topSold.productAdapter.notifyItemRangeRemoved(0, oldLength);
            }
        }

        new Thread(() -> {
            List<ProductAdapterItemInfo> data = getDataMaxSold();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    // list_TopSold already clear
                    list_TopSold.addAll(data);
                    if (fragment_topSold != null && fragment_topSold.productAdapter != null && list_TopSold.size() > 0) {
                        fragment_topSold.productAdapter.notifyItemRangeInserted(0, data.size());
                    }
                });
            }
        }, "LoadProductTopSold").start();
    }

    public List<ProductAdapterItemInfo> getMoreData()
    {
        String sql = "SELECT ID, SALER_ID, CATEGORY_ID, NAME, PRICE, PRICE_ORIGIN, AMOUNT, AMOUNT_SOLD, PRIMARY_IMAGE_ID, DETAIL, CREATED_DATE, DELETED, STAR_AVG from " +

                "(SELECT product.ID, SALER_ID, CATEGORY_ID, NAME, PRICE, PRICE_ORIGIN, AMOUNT, AMOUNT_SOLD, " +
                "PRIMARY_IMAGE_ID, DETAIL, CREATED_DATE, DELETED, STAR_AVG, ROW_NUMBER() OVER(ORDER BY CREATED_DATE  DESC) AS STT " +
                "FROM PRODUCT where DATEDIFF(DAY,PRODUCT.CREATED_DATE, GETDATE()) < 7) as tamp " +
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
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

    public HomepageFragment()
    {
        list_New = new ArrayList<>();
        list_TopSold = new ArrayList<>();
        list_Categoty = new ArrayList<>();
    }
}
