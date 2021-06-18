package exam.nlb2t.epot.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Category.Category;
import exam.nlb2t.epot.Category.CategoryTab;
import exam.nlb2t.epot.Category.DBControllerCategory;
import exam.nlb2t.epot.ClassInformation.Product;
import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.PaginationScrollListener;
import exam.nlb2t.epot.ProductAdapter;
import exam.nlb2t.epot.ProductDetail.NewProductAdapter;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;

public class HomepageFragment extends Fragment {
    HomeShoppingBinding binding;
    private RecyclerView rcVCategory;
    private CategoryTab categoryTab;

    private RecyclerView rcVNewProduct;
    private NewProductAdapter newProductAdapter;
    private List<ProductBaseDB> newHintProductTodayList;
    private List<ProductBaseDB> newProductList;
    private boolean isLoading;
    private boolean isLastPage;

    private int totalPage;
    private int currentPage = 1;

    private int start = 0;
    private int end = 20;
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

        //category
        rcVCategory = binding.recycleViewCategory;
        categoryTab = new CategoryTab(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),RecyclerView.HORIZONTAL, false);
        rcVCategory.setLayoutManager(linearLayoutManager);
        categoryTab.setData(getListCategory());
        rcVCategory.setAdapter(categoryTab);

        //new product
        rcVNewProduct = binding.recycleViewNewProduct;
        newProductAdapter = new NewProductAdapter();
        LinearLayoutManager linearLayoutManagerNewProduct = new LinearLayoutManager(view.getContext());
        rcVNewProduct.setLayoutManager(linearLayoutManagerNewProduct);
        rcVNewProduct.setAdapter(newProductAdapter);
        /*RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        rcVNewProduct.addItemDecoration(itemDecoration);*/
        newProductList = new ArrayList<>();
        DBControllerProduct dbControllerProduct = new DBControllerProduct();
        newProductList = dbControllerProduct.getNewProductList();
        totalPage = (newProductList.size() > ((int)(newProductList.size() / end)*end) ? (int)newProductList.size()/end +1 : newProductList.size()/end);
        setFirstData();

        rcVNewProduct.addOnScrollListener(new PaginationScrollListener(linearLayoutManagerNewProduct ) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage++;
                loadNextPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
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

    public List<ProductBaseDB> getListNewProduct() {
        Toast.makeText(getContext(), "Loading accepted", Toast.LENGTH_SHORT).show();
        List<ProductBaseDB> list = new ArrayList<>();
        for (int it = 0; it < end; it++){
            list.add(newProductList.get(start + it));
        }
        start += end;
        return  list;
    }

    private void setFirstData(){
        newHintProductTodayList = getListNewProduct();
        newProductAdapter.setData(newHintProductTodayList);

        if (currentPage < totalPage)
        {
            newProductAdapter.addFooterLoading();
        }
        else
        {
            isLastPage = true;
        }
    }

    private void loadNextPage(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<ProductBaseDB> list = getListNewProduct();

                newProductAdapter.removeFooterLoading();
                newHintProductTodayList.addAll(list);
                newProductAdapter.notifyDataSetChanged();

                isLoading = false;

                if (currentPage<totalPage){
                    newProductAdapter.addFooterLoading();
                }
                else{
                    isLastPage = true;
                }
            }
        }, 2000);
    }
}
