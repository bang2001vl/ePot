package exam.nlb2t.epot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import exam.nlb2t.epot.Database.Tables.ProductBaseDB;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_ProItem_Container#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_ProItem_Container extends Fragment {

    public List<ProductBaseDB> productList ;
    RecyclerView proGrid;
    public  ProductAdapter productAdapter;


    public fragment_ProItem_Container() {
        // Required empty public constructor
    }

    public static fragment_ProItem_Container newInstance(List<ProductBaseDB> productList) {
        fragment_ProItem_Container fragment = new fragment_ProItem_Container();
        fragment.productList = productList;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__pro_item__container, container, false);
        if(productList != null)
         {
            proGrid =  view.findViewById(R.id.Gridpro);
            productAdapter = new ProductAdapter(productList, this.getContext());

            proGrid.setAdapter(productAdapter);
            proGrid.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        }
        return view;
    }



}