package exam.nlb2t.epot.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import java.util.List;

import exam.nlb2t.epot.Category.DBControllerCategory;
import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;
import exam.nlb2t.epot.fragment_ProItem_Container;

public class HomepageFragment extends Fragment {
    HomeShoppingBinding binding;

    public static HomepageFragment newInstance(/*Params here*/)
    {
        HomepageFragment fragment = new HomepageFragment();
        // //TODO : Write code here <Setup new fragment>
        return fragment;
    }

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

        List<Pair<String, Bitmap>> categoryList = DBControllerCategory.getCategories();


    }

    void setEventHandler()
    {
        //TODO : Write code here <Set all listener in here>
    }
}
