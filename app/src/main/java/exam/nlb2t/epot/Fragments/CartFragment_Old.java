package exam.nlb2t.epot.Fragments;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.DialogFragment.PopupMenuDialog;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.Card_ItemView_New;
import exam.nlb2t.epot.singleton.Helper;

public class CartFragment_Old extends Fragment {
    private Map<String, List<ProductBuyInfo>> data;
    private Map<String, LinearLayout> data_ContainerViews;
    private boolean[] checkList;

    public CartFragment_Old() {

    }

    public CartFragment_Old(int[] productIDs, int[] arr_amount_picked) {
        ProductBuyInfo[] arr = new ProductBuyInfo[productIDs.length];
        for(int i = 0; i<productIDs.length; i++)
        {
            arr[i] = new ProductBuyInfo(productIDs[i], arr_amount_picked[i]);
        }
        setData(arr);
    }

    public void setData(ProductBuyInfo[] arr)
    {
        data = groupBySaler(arr);
        data_ContainerViews = new HashMap<>(data.size());
        checkList = new boolean[data.size()];
        for (int i = 0; i<checkList.length; i++)
        {
            checkList[i] = false;
        }
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

            txtTotal = view.findViewById(R.id.txt_totalprice);
            btnPurchase = view.findViewById(R.id.btn_payment);
            btnPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(Map.Entry<String, List<ProductBuyInfo>> entry: data.entrySet())
                    {
                        List<ProductBuyInfo> buyInfo = entry.getValue();
                        StringBuilder builder = new StringBuilder();
                        builder.append("saler : ").append(entry.getKey()). append(" {");
                        for(ProductBuyInfo info: buyInfo)
                        {
                            builder.append("\n\t").append(info.product.name)
                            .append(" amount = ").append(info.Amount);
                        }
                        builder.append("\n }\n");
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

    void initSalerList(View root, LinearLayout saler_list_holder, LayoutInflater inflater, ViewGroup container)
    {
        List<String> salerNameSet = new ArrayList<>(data.keySet());
        Collections.sort(salerNameSet);

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
            Card_ItemView_New card_itemView = new Card_ItemView_New(container.getContext());
            card_itemView.setData(buyInfo.product.name, buyInfo.product.price,
                    buyInfo.product.amount - buyInfo.product.amountSold, buyInfo.Amount, buyInfo.imagePrimary);
            card_itemView.setOnLongClickListener(onItemLongClick);
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
                                Card_ItemView_New cardItemView = (Card_ItemView_New) items_container.getChildAt(i);
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
                                Card_ItemView_New cardItemView = (Card_ItemView_New) items_container.getChildAt(i);
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

    View.OnLongClickListener onItemLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            PopupMenuDialog dialog = new PopupMenuDialog(new String[]{"Xóa sản phẩm khỏi giỏ"});
            dialog.setOnClickOptionListener(new PopupMenuDialog.OnClickOptionListener() {
                @Override
                public void onClickOption(String option) {
                    if(option.equals(dialog.getOptions()[0]))
                    {
                        onClickDeleteItem.onClick(v);
                    }
                }
            });
            return true;
        }
    };

    View.OnClickListener onClickDeleteItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Card_ItemView_New card_itemView = (Card_ItemView_New) v;
            ProductBuyInfo productBuyInfo = (ProductBuyInfo) card_itemView.Tag;

            data.get(productBuyInfo.salerOverview.fullName).remove(productBuyInfo);
            ((ViewGroup)card_itemView.getParent()).removeView(card_itemView);

            if(card_itemView.getChecked()){requireCalculated();}
        }
    };

    Card_ItemView_New.OnListItemChangedListener onListItemChangedListener = new Card_ItemView_New.OnListItemChangedListener() {
        @Override
        public void onCheckProductChanged(View view, boolean isChecked) {
            requireCalculated();
        }

        @Override
        public void onNumberProductChanged(View view, int newNumber) {
            Card_ItemView_New card_itemView = (Card_ItemView_New)view;
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
                    Card_ItemView_New card_itemView = (Card_ItemView_New) container.getChildAt(i);
                    if (card_itemView.getChecked()) {
                        ProductBuyInfo buyInfo = (ProductBuyInfo) card_itemView.Tag;
                        total += buyInfo.product.price * buyInfo.Amount;
                    }
                }
            }
            txtTotal.setText(Helper.getInstance(getContext()).getPrice(total));
            btnPurchase.setEnabled(total > 0);
        }
    }

    public static Map<String, List<ProductBuyInfo>> groupBySaler(ProductBuyInfo[] buyInfos)
    {
        Map<String, List<ProductBuyInfo>> map = new HashMap<>(10);
        String shopname;
        for(int i =0; i<buyInfos.length;i++)
        {
            shopname = buyInfos[i].salerOverview.fullName;
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
