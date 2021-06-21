package exam.nlb2t.epot.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Category.Category;
import exam.nlb2t.epot.Category.CategoryAdapter;
import exam.nlb2t.epot.Category.DBControllerCategory;
import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.OnItemClickListener;
import exam.nlb2t.epot.Views.Item_product_container.ProductAdapter;
import exam.nlb2t.epot.ProductAdapterItemInfo;
import exam.nlb2t.epot.ProductDetail.ProductDetailFragment;
import exam.nlb2t.epot.Views.Search_Product.fragment_search;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class HomepageFragment extends Fragment implements OnItemClickListener {
    HomeShoppingBinding binding;
    private RecyclerView rcVCategory;
    private CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    private RecyclerView rcVNewProduct;
    private ProductAdapter productAdapter;

    private RecyclerView rcVMaxSold;
    private ProductAdapter productAdapterMaxSold;

    private androidx.appcompat.widget.SearchView searchView;

    int step = 10;
    int currentLastIndex = 1;
    String sql;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeShoppingBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = binding.searchBar;
        //category
        rcVCategory = binding.recycleViewCategory;
        categoryList = list_Categoty;
        categoryAdapter = new CategoryAdapter(view.getContext(),categoryList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),RecyclerView.HORIZONTAL, false);
        rcVCategory.setLayoutManager(linearLayoutManager);
        categoryAdapter.setData(categoryList);
        rcVCategory.setAdapter(categoryAdapter);

        // new product
        binding.buttonMoreProductNew.setOnClickListener(v->{
            productAdapter.addproduct(getMoreData());
        });

        rcVNewProduct = binding.recycleViewNewProduct;
        productAdapter = new ProductAdapter(list_New,view.getContext(),this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        rcVNewProduct.setLayoutManager(gridLayoutManager);
        rcVNewProduct.setAdapter(productAdapter);

        //number sold max
        rcVMaxSold = binding.recycleViewMaxSold;
        productAdapterMaxSold = new ProductAdapter(list_TopSold,view.getContext(),this);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        rcVMaxSold.setLayoutManager(gridLayoutManager);
        rcVMaxSold.setAdapter(productAdapterMaxSold);

        list_Categoty = null;
        list_TopSold = null;
        list_New = null;
    }

    List<ProductAdapterItemInfo> list_New;
    List<ProductAdapterItemInfo> list_TopSold;
    List<Category> list_Categoty;
    public void LoadFirstData()
    {
        list_New = getMoreData();
        list_TopSold = getDataMaxSold();
        list_Categoty = getListCategory();
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

    void setEventHandler(){
        //TODO : Write code here <Set all listener in here>
        binding.searchBar.setOnClickListener(v->{
            fragment_search dialog = new fragment_search();
            dialog.show(getChildFragmentManager(), "search");
        });
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
            binding.buttonMoreProductNew.setVisibility(View.GONE);
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
        searchView.setQuery("Danh mục " + string,true);
    }

    @Override
    public void onItemClickProduct(int id) {
        //Toast.makeText(getContext(),"id: "+id ,Toast.LENGTH_LONG).show();
        Log.d("MY_TAG", "Open product with id = " + id);
        ProductDetailFragment dialog = new ProductDetailFragment();
        dialog.productID = id;
        dialog.show(getChildFragmentManager(), "detailProduct");
    }

    public HomepageFragment()
    {
        LoadFirstData();
    }
}
