package exam.nlb2t.epot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

import exam.nlb2t.epot.ClassInformation.Product;
import exam.nlb2t.epot.Views.Tag_Salepro;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_ProItem_Container#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_ProItem_Container extends Fragment {

    List<Product> productList ;
    ArrayAdapter<Product> ProductAdapter;
    GridView proGrid;

    public fragment_ProItem_Container() {
        // Required empty public constructor
    }


    public static fragment_ProItem_Container newInstance(List<Product> productList) {
        fragment_ProItem_Container fragment = new fragment_ProItem_Container();
        fragment.productList = productList;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductAdapter = new ArrayAdapter<Product>(this.getContext(), R.layout.fragment__pro_item__container, productList);

        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__pro_item__container, container, false);
        if(productList != null) {
            proGrid = (GridView) view.findViewById(R.id.Gridpro);
            proGrid.setNumColumns(2);
            proGrid.setAdapter(ProductAdapter);

            for (int i = 0; i < productList.size(); i++) {
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.product_item_layout, container, false);

                TextView tv = ((TextView) linearLayout.findViewById(R.id.textview_proPrice));
                tv.setText(productList.get(i).CurrentPrice);
                ((TextView) linearLayout.findViewById(R.id.textview_proSold)).setText(productList.get(i).NumberSold);
                ((TextView) linearLayout.findViewById(R.id.textview_proName)).setText(productList.get(i).Description);
                ((Tag_Salepro) linearLayout.findViewById(R.id.Tag_Salepro)).Text = (productList.get(i).PecentSale);
                ((ImageView) linearLayout.findViewById(R.id.Image_Product)).setImageBitmap(productList.get(i).MainImage);

                //ViewGroup.LayoutParams params = new GridLayout.LayoutParams();
                proGrid.addView(linearLayout);
            }
        }
        return view;
    }
}