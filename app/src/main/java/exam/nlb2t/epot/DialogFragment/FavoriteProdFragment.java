package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.Views.Item_product_container.ProductAdapter;
import exam.nlb2t.epot.ProductAdapterItemInfo;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentFavoriteProdBinding;
import exam.nlb2t.epot.singleton.Authenticator;


public class FavoriteProdFragment extends DialogFragment {


    FragmentFavoriteProdBinding binding;
    int step = 100;
    int lastIndex = 1;
    List<ProductAdapterItemInfo> list;
    private UserBaseDB currentuser= Authenticator.getCurrentUser();
    public FavoriteProdFragment() {
        // Required empty public constructor
        list = getMoreData();
    }
    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteProdBinding.inflate(inflater, container, false);
        setEventHandler();
        setupRecylerView();
        return binding.getRoot();
    }

    void setupRecylerView()
    {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.mainRecyclerView.setLayoutManager(gridLayoutManager);
        binding.mainRecyclerView.setAdapter(new ProductAdapter(list, getContext()));
    }

    List<ProductAdapterItemInfo> getMoreData()
    {
        DBControllerProduct db = new DBControllerProduct();
        List<ProductBaseDB> data = db.getLikedProduct(currentuser.id, lastIndex, lastIndex + step -1);
        List<ProductAdapterItemInfo> rs = new ArrayList<>(data.size());
        for(ProductBaseDB p:data)
        {
            ProductAdapterItemInfo info = new ProductAdapterItemInfo(p, null);
            rs.add(info);
        }
        db.closeConnection();
        return  rs;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private void setEventHandler() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
