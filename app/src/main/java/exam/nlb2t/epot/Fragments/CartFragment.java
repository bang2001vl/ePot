package exam.nlb2t.epot.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import exam.nlb2t.epot.Card_ListItemAdapter;
import exam.nlb2t.epot.ClassInformation.Product;
import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.Card_ItemView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    private Map<String, List<ProductBuyInfo>> data;
    private Map<String, LinearLayout> data_ContainerViews;
    private boolean[] checkList;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(@NonNull ProductBuyInfo[] buyInfos) {
        CartFragment fragment = new CartFragment();

        Map<String, List<ProductBuyInfo>> map = groupBySaler(buyInfos);
        fragment.data = map;
        fragment.data_ContainerViews = new HashMap<>(fragment.data.size());
        fragment.checkList = new boolean[map.size()];
        for (int i = 0; i<fragment.checkList.length; i++)
        {
            fragment.checkList[i] = false;
        }

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
        LinearLayout saler_list_holder = view.findViewById(R.id.saler_layout_holder);

        if(data != null && data.size()>0) {
            initSalerList(view, saler_list_holder, inflater, container);

            txtTotal = view.findViewById(R.id.txt_total_money);
            btnPurchase = view.findViewById(R.id.button_purchase);

            btnPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(List<ProductBuyInfo> buyInfo: data.values())
                    {

                        StringBuilder builder = new StringBuilder();
                        builder.append("saler : ").append(buyInfo.get(0).product.saler.FullName);
                        builder.append(" | items count = ").append(buyInfo.size());
                        Log.d("MY_TRACE", builder.toString());
                    }

                }
            });

            requireCalculated();
        }
        else {
            TextView textView = new TextView(view.getContext());
            textView.setText("Empty cart");
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            saler_list_holder.addView(textView, params);
        }
        return view;
    }

    TextView txtTotal;
    Button btnPurchase;
    Map<String, Card_ListItemAdapter> adapters;

    void initSalerList(View root, LinearLayout saler_list_holder, LayoutInflater inflater, ViewGroup container)
    {
        List<String> salerNameSet = new ArrayList<>(data.keySet());
        Collections.sort(salerNameSet);

        adapters = new HashMap<>(salerNameSet.size());
        for (String salerName : salerNameSet) {

            LinearLayout item_list_container = (LinearLayout) inflater.inflate(R.layout.card_list_saler, container, false);

            initItemList(item_list_container, salerName);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0, getResources().getDimensionPixelSize(R.dimen.normal_padding));
            saler_list_holder.addView(item_list_container, params);

        }
    }

    void initItemList(@NonNull LinearLayout container, String salerName)
    {
        TextView txtSalerName = container.findViewById(R.id.saler_name);
        ImageButton btnMore = container.findViewById(R.id.button_more_saler);

        txtSalerName.setText(salerName);

        LinearLayout items_container = container.findViewById(R.id.linearLayout);

        List<ProductBuyInfo> buyInfoList = data.get(salerName);
        for(ProductBuyInfo buyInfo: buyInfoList)
        {
            Card_ItemView card_itemView = new Card_ItemView(container.getContext());
            card_itemView.setData(buyInfo.product.productName, buyInfo.product.currentPrice, buyInfo.product.originPrice,
                    buyInfo.product.avaiableAmount, buyInfo.Amount, buyInfo.product.mainImage);
            card_itemView.setOnClickDeleteListener(onClickDeleteItem);
            card_itemView.setOnListItemChangedListener(onListItemChangedListener);
            card_itemView.Tag = buyInfo;
            items_container.addView(card_itemView);
        }

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.card_saler_item_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId()==R.id.menu_select_all)
                        {
                            isFreezeCalculated = true;
                            for(int i = items_container.getChildCount() -1; i>-1; i--)
                            {
                                Card_ItemView cardItemView = (Card_ItemView) items_container.getChildAt(i);
                                cardItemView.setChecked(true);
                            }
                            isFreezeCalculated = false;
                            requireCalculated();
                            return true;
                        }
                        else if (item.getItemId() == R.id.menu_deselect_all)
                        {
                            isFreezeCalculated = true;
                            for(int i = items_container.getChildCount() -1; i>-1; i--)
                            {
                                Card_ItemView cardItemView = (Card_ItemView) items_container.getChildAt(i);
                                cardItemView.setChecked(false);
                            }
                            isFreezeCalculated = false;
                            requireCalculated();
                            return true;
                        }
                        else if (item.getItemId() == R.id.menu_delete_all)
                        {
                            data.remove(salerName);
                            ((ViewGroup)container.getParent()).removeView(container);
                            return true;
                        }
                        return  false;
                    }
                });

                popupMenu.show();
            }
        });


        data_ContainerViews.put(salerName, items_container);
    }

    View.OnClickListener onClickDeleteItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Card_ItemView card_itemView = (Card_ItemView) v;
            ProductBuyInfo productBuyInfo = (ProductBuyInfo) card_itemView.Tag;

            data.get(productBuyInfo.product.saler.ShopName).remove(productBuyInfo);
            ((ViewGroup)card_itemView.getParent()).removeView(card_itemView);

            if(card_itemView.getChecked()){requireCalculated();}
        }
    };

    Card_ItemView.OnListItemChangedListener onListItemChangedListener = new Card_ItemView.OnListItemChangedListener() {
        @Override
        public void onCheckProductChanged(View view, boolean isChecked) {
            requireCalculated();
        }

        @Override
        public void onNumberProductChanged(View view, int newNumber) {
            Card_ItemView card_itemView = (Card_ItemView)view;
            ProductBuyInfo buyInfo = (ProductBuyInfo)card_itemView.Tag;
            buyInfo.Amount = newNumber;
            if(card_itemView.getChecked()) {
                requireCalculated();
            }
        }
    };

    boolean isFreezeCalculated = false;
    void requireCalculated()
    {
        if(!isFreezeCalculated) {
            int total = 0;

            for (LinearLayout container : data_ContainerViews.values()) {
                for (int i = container.getChildCount() - 1; i > -1; i--) {
                    Card_ItemView card_itemView = (Card_ItemView) container.getChildAt(i);
                    if (card_itemView.getChecked()) {
                        ProductBuyInfo buyInfo = (ProductBuyInfo) card_itemView.Tag;
                        total += buyInfo.product.currentPrice * buyInfo.Amount;
                    }
                }
            }
            txtTotal.setText(String.format(Locale.getDefault(), "%,d VND", total));
            btnPurchase.setEnabled(total > 0);
        }
    }

    public static Map<String, List<ProductBuyInfo>> groupBySaler(ProductBuyInfo[] buyInfos)
    {
        Map<String, List<ProductBuyInfo>> map = new HashMap<>(10);
        String shopname;
        for(int i =0; i<buyInfos.length;i++)
        {
            shopname = buyInfos[i].product.saler.ShopName;
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