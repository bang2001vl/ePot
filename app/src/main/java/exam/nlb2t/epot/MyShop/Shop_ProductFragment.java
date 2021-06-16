package exam.nlb2t.epot.MyShop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.databinding.MyShopProductTabBinding;
import exam.nlb2t.epot.singleton.Helper;

public class Shop_ProductFragment extends Fragment {
    MyShopProductTabBinding binding;
    List<ProductBaseDB> products;
    RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyShopProductTabBinding.inflate(inflater, container, false);
        setEventHandler();

        LinearLayoutManager layout = new LinearLayoutManager(container.getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.layoutProductMyShop.setLayoutManager(layout);

        //TODO: Find UserID to login app
        adapter = new Product_TabAdapter(1);
        binding.layoutProductMyShop.setAdapter(adapter);

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
            addProductFragment.setOnSubmitOKListener(new Helper.OnSuccessListener() {
                @Override
                public void OnSuccess(Object sender) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
