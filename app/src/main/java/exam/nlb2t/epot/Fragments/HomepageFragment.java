package exam.nlb2t.epot.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Category.Category;
import exam.nlb2t.epot.Category.CategoryTab;
import exam.nlb2t.epot.Category.DBControllerCategory;
import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;

public class HomepageFragment extends Fragment {
    HomeShoppingBinding binding;
    private RecyclerView rcViCategory;
    private CategoryTab categoryTab;

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
        /*DatabaseController databaseController = new DatabaseController();
        databaseController.closeConnection();*/
        
        rcViCategory = binding.recycleViewCategory;
        categoryTab = new CategoryTab(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),RecyclerView.HORIZONTAL, false);
        rcViCategory.setLayoutManager(linearLayoutManager);

        categoryTab.setData(getListCategory());

        // Add adapter to recyclerview
        rcViCategory.setAdapter(categoryTab);
    }

    private List<Category> getListCategory()
    {
        List<Category> list = new ArrayList<>();
        List<Pair<String, Bitmap>> categoryList = DBControllerCategory.getCategories();
        for (Pair<String, Bitmap> i:categoryList) {
            list.add(new Category(i.first, i.second));
        }
        return  list;
    }

    void setEventHandler()
    {
        //TODO : Write code here <Set all listener in here>
    }
}
