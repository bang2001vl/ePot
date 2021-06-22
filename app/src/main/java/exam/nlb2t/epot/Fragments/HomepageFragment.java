package exam.nlb2t.epot.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
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
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.Item_product_container.ProductAdapter;
import exam.nlb2t.epot.ProductAdapterItemInfo;
import exam.nlb2t.epot.ProductDetail.ProductDetailFragment;
import exam.nlb2t.epot.Views.Search_Product.fragment_search;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;
import exam.nlb2t.epot.fragment_ProItem_Container;
import exam.nlb2t.epot.singleton.Authenticator;

public class HomepageFragment extends Fragment implements OnItemClickListener {
    HomeShoppingBinding binding;

    private RecyclerView rcVCategory;
    private CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    private fragment_ProItem_Container fragment_new;
    private fragment_ProItem_Container fragment_topSold;

    private androidx.appcompat.widget.SearchView searchView;

    int step = 10;
    int currentLastIndex = 1;
    String sql;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeShoppingBinding.inflate(inflater, container, false);

        //category
        rcVCategory = binding.recycleViewCategory;
        categoryList = list_Categoty;
        categoryAdapter = new CategoryAdapter(getContext(),categoryList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL, false);
        rcVCategory.setLayoutManager(linearLayoutManager);
        rcVCategory.setAdapter(categoryAdapter);

        fragment_new = fragment_ProItem_Container.newInstance(list_New);
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_product_new, fragment_new).commit();

        fragment_topSold = fragment_ProItem_Container.newInstance(list_TopSold);
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_product_top_sold, fragment_topSold).commit();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = binding.searchBar;
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

        // new product
        binding.buttonMoreProductNew.setOnClickListener(v->{
            fragment_new.spinner.setSelection(0, true);
            fragment_new.addProduct(getMoreData());
        });

        fragment_new.setOnClickItemListener(onClickItemListener);

        fragment_topSold.setOnClickItemListener(onClickItemListener);

    }

    List<ProductAdapterItemInfo> list_New;
    List<ProductAdapterItemInfo> list_TopSold;
    List<Category> list_Categoty;
    public void LoadFirstData()
    {
        list_New = new ArrayList<>();

        list_TopSold = new ArrayList<>();

        list_Categoty = getListCategory();
        if(categoryAdapter != null){categoryAdapter.notifyDataSetChanged();}

        onClickRefresh_New();
        onClickRefresh_TopSold();
    }

    private List<ProductAdapterItemInfo> getDataMaxSold() {
        sql = "SELECT TOP 10 PRODUCT.ID, SALER_ID, CATEGORY_ID, NAME, PRICE, PRICE_ORIGIN, AMOUNT, " +
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
        return  list;
    }

    private List<Category> getListCategory(){
        DBControllerCategory db = new DBControllerCategory();
        List<Category> list = db.getCategoriesList_withoutImage();
        db.closeConnection();
        /*List<Pair<String, Bitmap>> categoryList = DBControllerCategory.getCategories();
        for (Pair<String, Bitmap> i:categoryList) {
            list.add(new Category(i.first, i.second));
        }*/
        return  list;
    }

    void submitQuery(String query) {
        fragment_search dialog = new fragment_search(query);
        dialog.show(getChildFragmentManager(), "search");
    }

    public void onClickRefresh_New() {
        if (fragment_new != null) {
            int oldLength = fragment_new.productAdapter.getProductList().size();
            fragment_new.productAdapter.getProductList().clear();
            fragment_new.productAdapter.notifyItemRangeRemoved(0, oldLength);
        }

        new Thread(() -> {
            currentLastIndex = 1;
            step = 9;
            list_New = getMoreData();
            step = 10;

            if (fragment_new != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    fragment_new.addProduct(list_New);
                });
            }

        }).start();
    }

    public void onClickRefresh_TopSold() {
        if (fragment_topSold != null) {
            int oldLength = fragment_topSold.productAdapter.getProductList().size();
            fragment_topSold.productAdapter.getProductList().clear();
            fragment_topSold.productAdapter.notifyItemRangeRemoved(0, oldLength);
        }

        new Thread(() -> {
            currentLastIndex = 1;
            step = 9;
            list_TopSold = getDataMaxSold();
            step = 10;
            if (fragment_topSold != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    fragment_topSold.addProduct(list_TopSold);
                });
            }

        }).start();
    }

    public List<ProductAdapterItemInfo> getMoreData()
    {
        sql = "SELECT ID, SALER_ID, CATEGORY_ID, NAME, PRICE, PRICE_ORIGIN, AMOUNT, AMOUNT_SOLD, PRIMARY_IMAGE_ID, DETAIL, CREATED_DATE, DELETED, STAR_AVG from " +

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
       /* for(ProductBaseDB p : list)
        {


            // Create new custom view to display product
            //View view = getLayoutInflater().inflate(R.layout.product_item_layout, binding.gridNewProduct,false);
            // Set data from p to new view


            // Add new view to grid
            *//*GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            binding.gridNewProduct.addView(view, binding.gridNewProduct.getChildCount() - 1, params);*//*
        }*/

        if(list.size() < step)
        {
            // no more data to get
            if(binding != null) {
                binding.buttonMoreProductNew.setVisibility(View.GONE);
            }
        }

        currentLastIndex += list.size();

        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    @Override
    public void onItemClickCategory(String string) {
        searchView.setQuery(String.format(Locale.getDefault(), "Danh mục: %s;", string), true);
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
        LoadFirstData();
    }
}
