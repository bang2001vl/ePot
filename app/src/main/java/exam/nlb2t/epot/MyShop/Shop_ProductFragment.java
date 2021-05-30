package exam.nlb2t.epot.MyShop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import exam.nlb2t.epot.Fragments.HomepageFragment;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;
import exam.nlb2t.epot.databinding.MyShopProductTabBinding;

public class Shop_ProductFragment extends Fragment {
    MyShopProductTabBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyShopProductTabBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : Write code here <Get data from database and set to view>
    }

    void setEventHandler()
    {
        binding.buttonAddProduct.setOnClickListener(v -> {
            AddProductFragment addProductFragment = new AddProductFragment();
            if(getFragmentManager() == null){return;}
            addProductFragment.show(getFragmentManager().beginTransaction(), "createProduct");
            if(addProductFragment.isOK)
            {
                Log.e("MY_TAG", "SUCCESS: Write to database");
            }
        });
    }
}
