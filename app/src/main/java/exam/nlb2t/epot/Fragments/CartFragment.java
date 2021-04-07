package exam.nlb2t.epot.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exam.nlb2t.epot.Card_ListItemAdapter;
import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    private Map<String, List<ProductBuyInfo>> data;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(ProductBuyInfo[] buyInfos) {
        CartFragment fragment = new CartFragment();

        Map<String, List<ProductBuyInfo>> map = groupBySaler(buyInfos);
        fragment.data = map;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        if(data != null) {
            LinearLayout saler_list_holder = view.findViewById(R.id.saler_layout_holder);
            Set<String> salerNameSet = data.keySet();
            for (String salerName : salerNameSet) {
                LinearLayout saler_layout = (LinearLayout) inflater.inflate(R.layout.card_list_saler, container, false);

                TextView txtSalerName = saler_layout.findViewById(R.id.saler_name);
                RecyclerView product_recycleView = saler_layout.findViewById(R.id.recycleLayout);

                txtSalerName.setText(salerName);
                Card_ListItemAdapter adapter = new Card_ListItemAdapter(data.get(salerName));
                product_recycleView.setAdapter(adapter);
                product_recycleView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                saler_list_holder.addView(saler_layout, saler_layout.getLayoutParams());
            }
        }
        return view;
    }

    public static Map<String, List<ProductBuyInfo>> groupBySaler(ProductBuyInfo[] buyInfos)
    {
        Map<String, List<ProductBuyInfo>> map = new HashMap<>(10);
        String shopname;
        for(int i =0; i<buyInfos.length;i++)
        {
            shopname = buyInfos[i].product.Saler.ShopName;
            if(map.containsKey(shopname))
            {
                map.get(shopname).add(buyInfos[i]);
            }
            else
            {
                map.put(shopname, new ArrayList<>(10));
            }
        }
        return  map;
    }

}