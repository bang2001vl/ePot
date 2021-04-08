package exam.nlb2t.epot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import exam.nlb2t.epot.ClassInformation.Product;
import exam.nlb2t.epot.Views.product_Item_Layout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_ProItem_Container#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_ProItem_Container extends Fragment {

    List<Product> productList ;
    FlexboxLayout proGrid;

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
        /*for (int i = 0; i < productList.size(); i++) {

        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__pro_item__container, container, false);
        if(productList != null) {
            proGrid = (FlexboxLayout) view.findViewById(R.id.Gridpro);

            for (int i = 0; i < productList.size(); i++) {

                product_Item_Layout proItem = new product_Item_Layout(this.getContext());

                proItem.Set_value(productList.get(i));
               // FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                proGrid.addView(proItem);
                //proGrid.getChildAt(i).getLayoutParams().width = proGrid.getWidth()/2;
            }
            /*setColumnCount(2);*/
        }

        return view;
    }

    /*public void setColumnCount(int columnCount)
    {
        int n = proGrid.getChildCount();
        for(int i = 0; i<n; i++)
        {
            View view = proGrid.getChildAt(i);
            view.getLayoutParams().width /= 2;
            view.getLayoutParams().height /= 2;
        }
    }*/
}