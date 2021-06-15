package exam.nlb2t.epot.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.ProductDetail.ProductDetailFragment;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.ActivityTestingBinding;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;
import exam.nlb2t.epot.fragment_ProItem_Container;

public class HomepageFragment extends Fragment {
    /*HomeShoppingBinding binding;

    public static HomepageFragment newInstance(*//*Params here*//*)
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
        DatabaseController databaseController = new DatabaseController();
        databaseController.closeConnection();
        //TODO : Write code here <Get data from database and set to view>
    }

    void setEventHandler()
    {
        //TODO : Write code here <Set all listener in here>
    }*/

    ActivityTestingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityTestingBinding.inflate(getLayoutInflater());
        binding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailFragment fragment = new ProductDetailFragment();
                fragment.productID = Integer.parseInt(binding.edtInput.getText().toString());

                fragment.show(getChildFragmentManager(), "");
            }
        });
        return binding.getRoot();
    }
}
