package exam.nlb2t.epot;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exam.nlb2t.epot.Database.Tables.ProductBaseDB;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_ProItem_Container#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_ProItem_Container extends Fragment {

    private Context context;
    public List<ProductAdapterItemInfo> productList ;
    RecyclerView proGrid;
    public  ProductAdapter productAdapter;
    public Spinner spinner;


    public fragment_ProItem_Container() {
        // Required empty public constructor
    }


    public fragment_ProItem_Container(Context context) {
        this.context = context;
    }
    public static fragment_ProItem_Container newInstance(List<ProductAdapterItemInfo> productList) {
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
        spinner = view.findViewById(R.id.spinnerProduct);
        ArrayList<String> optionProduct = new ArrayList<String>();
        optionProduct.add("(Tên) A -> Z");
        optionProduct.add("(Tên) Z -> A");
        optionProduct.add("(Giá) Thấp -> Cao");
        optionProduct.add("(Giá) Cao -> Thấp");
        optionProduct.add("None");

        ArrayAdapter arrayAdapter = new ArrayAdapter( getContext(), android.R.layout.simple_spinner_item, optionProduct );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        if(productList != null)
         {
            proGrid =  view.findViewById(R.id.Gridpro);
            productAdapter = new ProductAdapter(productList, this.getContext());
            proGrid.setAdapter(productAdapter);
            proGrid.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
            setupSort();
        }
        return view;
    }

    private void setupSort() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        sortByTime();
                        break;
                    case 1:
                        sortByNameA_Z();
                        break;
                    case 2:
                        sortByNameZ_A();
                        break;
                    case 3:
                        sortByPriceLess();
                        break;
                    case 4:
                        sortByPriceMore();
                        break;
                    case 5:
                        sortByTime();
                        break;
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sortByPriceMore() {
        Collections.sort(productList,ProductBaseDB.sortPriceMax);
    }

    private void sortByPriceLess() {
        Collections.sort(productList, ProductBaseDB.sortPriceMin);
    }

    private void sortByNameZ_A() {
        Collections.sort(productList, ProductBaseDB.sortNameAtoZ);
    }

    private void sortByTime() {
        Collections.sort(productList,ProductBaseDB.TimeNew);
    }

    private void sortByNameA_Z() {
        Collections.sort(productList, ProductBaseDB.sortNameAtoZ);
    }
}