package exam.nlb2t.epot.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import exam.nlb2t.epot.ProductAdapter;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;

public class HomepageFragment extends Fragment implements OnItemClickListener {
    HomeShoppingBinding binding;
    private RecyclerView rcVCategory;
    private CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    private RecyclerView rcVNewProduct;
    List<ProductBaseDB> productBaseDBList;
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
        categoryList = getListCategory();
        categoryAdapter = new CategoryAdapter(view.getContext(),categoryList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),RecyclerView.HORIZONTAL, false);
        rcVCategory.setLayoutManager(linearLayoutManager);
        categoryAdapter.setData(categoryList);
        rcVCategory.setAdapter(categoryAdapter);

        // new product
        binding.buttonMoreProductNew.setOnClickListener(v->{
            productBaseDBList.addAll(getMoreData());
            productAdapter.notifyDataSetChanged();
        });

        rcVNewProduct = binding.recycleViewNewProduct;
        productAdapter = new ProductAdapter(productBaseDBList,view.getContext(),this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        productBaseDBList = getMoreData();
        productAdapter.setData(productBaseDBList);
        rcVNewProduct.setLayoutManager(gridLayoutManager);
        rcVNewProduct.setAdapter(productAdapter);

        //number sold max
        rcVMaxSold = binding.recycleViewMaxSold;
        productAdapterMaxSold = new ProductAdapter(productBaseDBList,view.getContext(),this);
        gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        productAdapterMaxSold.setData(getDataMaxSold());
        rcVMaxSold.setLayoutManager(gridLayoutManager);
        rcVMaxSold.setAdapter(productAdapterMaxSold);
    }

    private List<ProductBaseDB> getDataMaxSold() {
        List<ProductBaseDB> list = new ArrayList<>();
        sql = "SELECT TOP 30 PRODUCT.ID, SALER_ID, CATEGORY_ID, NAME, PRICE, PRICE_ORIGIN, AMOUNT, " +
                "AMOUNT_SOLD, PRIMARY_IMAGE_ID, DETAIL, CREATED_DATE, DELETED, DATA " +
                "FROM PRODUCT join  AVATAR on PRIMARY_IMAGE_ID = AVATAR.ID ORDER BY AMOUNT_SOLD DESC, CREATED_DATE DESC";
        DBControllerProduct dbControllerProduct = new DBControllerProduct();
        list = dbControllerProduct.getNewProductList(sql);
        return  list;
    }

    private List<Category> getListCategory(){
        List<Category> list = new ArrayList<>();
        List<Pair<String, Bitmap>> categoryList = DBControllerCategory.getCategories();
        for (Pair<String, Bitmap> i:categoryList) {
            list.add(new Category(i.first, i.second));
        }
        return  list;
    }

    void setEventHandler(){
        //TODO : Write code here <Set all listener in here>
    }


    public List<ProductBaseDB> getMoreData()
    {
        List<ProductBaseDB> list = new ArrayList<>();

        sql = "SELECT * from " +
                "(SELECT product.ID, SALER_ID, CATEGORY_ID, NAME, PRICE, PRICE_ORIGIN, AMOUNT, AMOUNT_SOLD, " +
                "PRIMARY_IMAGE_ID, DETAIL, CREATED_DATE, DELETED, DATA, ROW_NUMBER() OVER(ORDER BY CREATED_DATE  DESC) AS STT " +
                "FROM PRODUCT join  AVATAR on PRIMARY_IMAGE_ID = AVATAR.ID where DATEDIFF(DAY,PRODUCT.CREATED_DATE, GETDATE()) < 7) as tamp " +
                "where STT BETWEEN " + currentLastIndex + " AND " + (currentLastIndex + step -1);
        DBControllerProduct dbControllerProduct = new DBControllerProduct();
        list = dbControllerProduct.getNewProductList(sql);

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
        Toast.makeText(getContext(),"id: "+id ,Toast.LENGTH_LONG).show();
    }
}
