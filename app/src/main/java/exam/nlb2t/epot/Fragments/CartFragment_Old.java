package exam.nlb2t.epot.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.ProductDetail.ProductBuyInfo;
import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.DialogFragment.PaymentDialogFragment;
import exam.nlb2t.epot.DialogFragment.PaymentSucessDialog;
import exam.nlb2t.epot.DialogFragment.PopupMenuDialog;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.Card_ItemView_New;
import exam.nlb2t.epot.Views.LoadingView;
import exam.nlb2t.epot.databinding.FragmentCartBinding;
import exam.nlb2t.epot.databinding.FragmentEmptyBagBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.CartDataController;
import exam.nlb2t.epot.singleton.Category;
import exam.nlb2t.epot.singleton.Helper;

public class CartFragment_Old extends Fragment {
    private Map<String, List<ProductBuyInfo>> data;
    private Map<String, LinearLayout> data_ContainerViews;
    private boolean[] checkList;
    View.OnClickListener onItemDeleted;
    View.OnClickListener onItemAmountChanged;
    public void setOnItemDeleted(View.OnClickListener listener){onItemDeleted = listener;}
    public void setOnItemAmountChanged(View.OnClickListener listener){onItemAmountChanged = listener;}
    
    FragmentCartBinding binding;
    FragmentEmptyBagBinding bindingEmpty;

    public CartFragment_Old() {

    }

    public void requestLoadData()
    {
        if(getContext() == null || getActivity() == null) return;
        new Thread(()->{
            List<Pair<Integer, Integer>> list = CartDataController.getAllData(getContext());
            getActivity().runOnUiThread(()->{
                requestLoadData(list);
            });
        }).start();
    }

    public void requestLoadData(List<Pair<Integer, Integer>> list) {
        if(list.size() == 0) {
            setData(new ProductBuyInfo[0]);
            layoutData();
            return;
        }
        showLoadingScreen();

        new Thread(() -> {
            ProductBuyInfo[] arr = new ProductBuyInfo[list.size()];
            for(int i = 0; i<arr.length; i++)
            {
                arr[i] = ProductBuyInfo.loadFromDB(list.get(i).first, list.get(i).second);
            }
            setData(arr);

            getActivity().runOnUiThread(() -> {
                layoutData();
                closeLoadingScreen();
            });
        }).start();
    }

    public void showLoadingScreen()
    {
        binding.getRoot().addView(new LoadingView(getContext()), binding.getRoot().getChildCount()
        , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void closeLoadingScreen()
    {
        binding.getRoot().removeViewAt(binding.getRoot().getChildCount() - 1);
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


    public Map<String, List<ProductBuyInfo>> groupBySaler(ProductBuyInfo[] buyInfos)
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
                List<ProductBuyInfo> list = new ArrayList<>(3);
                list.add(buyInfos[i]);
                map.put(shopname, list);
            }
        }

        return  map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        bindingEmpty = null;

        layoutData();

        return binding.getRoot();
    }

    public void layoutData()
    {
        if(data == null || data.size() < 1)
        {
            if(bindingEmpty == null)
            {
                bindingEmpty = FragmentEmptyBagBinding.inflate(getLayoutInflater(), binding.salerLayoutHolder, false);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                binding.contentLayout.addView(bindingEmpty.getRoot(), binding.contentLayout.getChildCount(), params);
            }

            binding.txtTotalprice.setText(Helper.getMoneyString(0));
            return;
        }

        if(bindingEmpty != null) {
            binding.contentLayout.removeViewAt(binding.contentLayout.getChildCount() - 1);
            bindingEmpty = null;
        }

        binding.salerLayoutHolder.removeAllViews();

        View view = (View)binding.salerLayoutHolder.getParent().getParent();
        initSalerList(getLayoutInflater(), (ViewGroup) view);

        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Integer, List<ProductBuyInfo>> buyMap = new HashMap<>();
                for (LinearLayout container : data_ContainerViews.values()) {
                    List<ProductBuyInfo> checked = new ArrayList<>();
                    for (int i = container.getChildCount() - 1; i > -1; i--) {
                        Card_ItemView_New card_itemView = (Card_ItemView_New) container.getChildAt(i);
                        if (card_itemView.getChecked()) {
                            ProductBuyInfo buyInfo = (ProductBuyInfo) card_itemView.Tag;
                            checked.add(buyInfo);
                        }
                    }
                    if(checked.size() > 0){
                        int salerID = ((ProductBuyInfo)((Card_ItemView_New) container.getChildAt(0)).Tag).salerOverview.id;
                        buyMap.put(salerID, checked);
                    }
                }
                onClickPayment(buyMap);
            }
        });

        requireCalculated();
    }

    void initSalerList(LayoutInflater inflater, ViewGroup container)
    {
        List<String> salerNameSet = new ArrayList<>(data.keySet());
        Collections.sort(salerNameSet);

        for (String salerName : salerNameSet) {

            LinearLayout item_list_container = (LinearLayout) inflater.inflate(R.layout.card_list_saler, container, false);

            initItemList(item_list_container, salerName);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0, getResources().getDimensionPixelSize(R.dimen.normal_padding));
            binding.salerLayoutHolder.addView(item_list_container, params);

        }
    }

    void initItemList(@NonNull LinearLayout saler_container, String salerName)
    {
        TextView txtSalerName = saler_container.findViewById(R.id.saler_name);
        ImageButton btnMore = saler_container.findViewById(R.id.button_more_saler);

        txtSalerName.setText(salerName);

        LinearLayout items_container = saler_container.findViewById(R.id.linearLayout);

        List<ProductBuyInfo> buyInfoList = data.get(salerName);
        for(ProductBuyInfo buyInfo: buyInfoList)
        {
            Card_ItemView_New item = new Card_ItemView_New(saler_container.getContext());
            item.setData(buyInfo.product.name, buyInfo.product.price,
                    buyInfo.product.amount - buyInfo.product.amountSold, buyInfo.Amount, buyInfo.imagePrimary);
            if(buyInfo.imagePrimary == null)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(getActivity() == null) return;
                        DBControllerProduct db = new DBControllerProduct();
                        Bitmap image = db.getAvatar_Product(buyInfo.product.imagePrimaryID);
                        db.closeConnection();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                item.setAvatar(image);
                            }
                        });
                    }
                }).start();
            }
            item.setOnLongClickListener(onItemLongClick);
            item.setOnListItemChangedListener(onListItemChangedListener);
            item.Tag = buyInfo;

            item.setPadding(3,6,6,5);
            items_container.addView(item);
        }

        // Set event for context menu of saler_layout
        btnMore.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.card_saler_item_menu);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    isFreezeCalculated = true;
                    if (item.getItemId()==R.id.menu_select_all)
                    {
                        onClickSelectedAll(salerName);
                    }
                    else if (item.getItemId() == R.id.menu_deselect_all)
                    {
                        onClickUnselectedAll(salerName);
                    }
                    else if (item.getItemId() == R.id.menu_delete_all)
                    {
                        onClickRemoveSaler(salerName);
                    }
                    isFreezeCalculated = false;
                    requireCalculated();
                    return true;
                }
            });

            popupMenu.show();
        });

        ImageView salerAvatarView = saler_container.findViewById(R.id.image_saler_avatar);
        new Thread(() -> {
            if(getActivity() == null)return;
            int id = data.get(salerName).get(0).salerOverview.avatarID;
            DBControllerUser db = new DBControllerUser();
            Bitmap avatar = db.getAvatar(id);
            db.closeConnection();

            getActivity().runOnUiThread(() -> salerAvatarView.setImageBitmap(avatar));
        }).start();

        data_ContainerViews.put(salerName, items_container);
    }

    View.OnLongClickListener onItemLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            PopupMenuDialog dialog = new PopupMenuDialog(new String[]{"X??a s???n ph???m kh???i gi???"});
            dialog.setOnClickOptionListener(new PopupMenuDialog.OnClickOptionListener() {
                @Override
                public void onClickOption(String option) {
                    if(option.equals(dialog.getOptions()[0]))
                    {
                        onClickDeleteItem.onClick(v);
                    }
                    dialog.dismiss();
                }
            });
            dialog.show(getChildFragmentManager(), null);
            return true;
        }
    };

    View.OnClickListener onClickDeleteItem = v -> {
        Card_ItemView_New item = (Card_ItemView_New) v;
        ProductBuyInfo productBuyInfo = (ProductBuyInfo) item.Tag;
        data.get(productBuyInfo.salerOverview.fullName).remove(productBuyInfo);
        ((ViewGroup)item.getParent()).removeView(item);

        if(item.getChecked()){requireCalculated();}
        if(onItemDeleted != null) {
            onItemDeleted.onClick(item);
        }
    };

    Card_ItemView_New.OnListItemChangedListener onListItemChangedListener = new Card_ItemView_New.OnListItemChangedListener() {
        @Override
        public void onCheckProductChanged(View view, boolean isChecked) {
            requireCalculated();
        }

        @Override
        public void onNumberProductChanged(View view, int newNumber) {
            Log.d("MY_TAG", "On Number changed");
            Card_ItemView_New item = (Card_ItemView_New)view;
            ProductBuyInfo productBuyInfo = (ProductBuyInfo)item.Tag;
            productBuyInfo.Amount = newNumber;
            if(item.getChecked()) {
                requireCalculated();
            }

            if(onItemAmountChanged!=null)
            {
                onItemAmountChanged.onClick(item);
            }
        }
    };

    void onClickSelectedAll(String salerName)
    {
        LinearLayout container = data_ContainerViews.get(salerName);
        if(container == null) return;
        for(int i = container.getChildCount() -1; i>-1; i--)
        {
            Card_ItemView_New cardItemView = (Card_ItemView_New) container.getChildAt(i);
            cardItemView.setChecked(true);
        }
    }

    void onClickUnselectedAll(String salerName)
    {
        LinearLayout container = data_ContainerViews.get(salerName);
        if(container == null) return;
        for(int i = container.getChildCount() -1; i>-1; i--)
        {
            Card_ItemView_New cardItemView = (Card_ItemView_New) container.getChildAt(i);
            cardItemView.setChecked(false);
        }
    }

    public void onClickRemoveSaler(String salerName)
    {
        ViewGroup item_container = data_ContainerViews.get(salerName);
        if(item_container == null) return;
        ViewGroup saler_container = (ViewGroup) item_container.getParent();
        if(saler_container == null) return;
        data.remove(salerName);
        ((ViewGroup)saler_container.getParent()).removeView(saler_container);
        data_ContainerViews.remove(salerName);

        if(onItemDeleted == null) {return;}
        for(int i = 0; i<item_container.getChildCount(); i++)
        {
            onItemDeleted.onClick(item_container.getChildAt(i));
        }
    }

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
            binding.txtTotalprice.setText(Helper.getInstance(getContext()).getPrice(total));
            binding.btnPayment.setEnabled(total > 0);
            binding.btnPayment.setTag(total);
        }
    }

    public void onClickPayment(Map<Integer,List<ProductBuyInfo>> buyMap)
    {
        long total = Integer.parseInt(binding.btnPayment.getTag().toString());
        int priceShip = 21 * 1000;
        PaymentDialogFragment fragment = new PaymentDialogFragment(Authenticator.getCurrentUser().id, total, priceShip, Authenticator.getCurrentUser().address, buyMap);
        fragment.setOnSubmitOKListener(new Helper.OnSuccessListener() {
            @Override
            public void OnSuccess(Object sender) {
                fragment.dismiss();
                if(getContext() == null || getActivity() == null) {return;}
                showLoadingScreen();
                new Thread(()->{

                    if(!checkAmount(buyMap))
                    {
                        getActivity().runOnUiThread(() -> {
                            closeLoadingScreen();
                        });
                        return;
                    }

                    boolean rs = pushToDB(buyMap, fragment.address, priceShip);

                    if(rs)
                    {
                        List<Integer> list = new ArrayList<>();
                        for(List<ProductBuyInfo> checkedProduct: buyMap.values()) {
                            for (ProductBuyInfo p : checkedProduct) {
                                list.add(p.product.id);
                            }
                        }
                        CartDataController.removeProduct(getContext(), list);

                        getActivity().runOnUiThread(()->{
                            closeLoadingScreen();
                            PaymentSucessDialog dialog = new PaymentSucessDialog();
                            dialog.show(getChildFragmentManager(), "tag");
                            requestLoadData();
                        });
                    }
                    else {
                        getActivity().runOnUiThread(() -> {
                            closeLoadingScreen();
                            Snackbar.make(binding.getRoot(), "C?? l???i x???y ra", BaseTransientBottomBar.LENGTH_LONG).show();
                        });
                    }
                }).start();

            }
        });
        fragment.show(getChildFragmentManager(), "payment");
    }

    private boolean checkAmount(Map<Integer,List<ProductBuyInfo>> buyMap)
    {
        DBControllerProduct dbPD = new DBControllerProduct();
        for (Map.Entry<Integer, List<ProductBuyInfo>> entry : buyMap.entrySet()) {
            for(ProductBuyInfo p : entry.getValue())
            {
                ProductBaseDB productBaseDB = dbPD.getProduct(p.product.id);
                if(productBaseDB.amount < productBaseDB.amountSold + p.Amount)
                {
                    p.Amount = productBaseDB.amount - productBaseDB.amountSold;
                    if(p.Amount < 1){
                        CartDataController.removeProduct(getContext(), p.product.id);
                    }
                    else {
                        CartDataController.setProduct(getContext(), p.product.id, p.Amount);
                    }
                    getActivity().runOnUiThread(()->{
                        Snackbar.make(binding.getRoot(), "Kh??ng c??n ????? h??ng cho " + p.product.name, BaseTransientBottomBar.LENGTH_LONG).show();
                        requestLoadData();
                    });
                    dbPD.closeConnection();
                    return false;
                }
            }
        }
        dbPD.closeConnection();
        return true;
    }

    boolean pushToDB(Map<Integer,List<ProductBuyInfo>> buyMap, String address, int priceShip)
    {
        Map<Integer, List<Pair<Integer, Integer>>> map = new HashMap<>();
        for (Map.Entry<Integer, List<ProductBuyInfo>> entry : buyMap.entrySet()) {
            List<Pair<Integer, Integer>> l = new ArrayList<>();
            for(ProductBuyInfo p: entry.getValue()){
                l.add(new Pair<>(p.product.id, p.Amount));
            }
            map.put(entry.getKey(), l);
        }

        DBControllerBill db = new DBControllerBill();
        boolean rs = db.addBill(Authenticator.getCurrentUser().id, priceShip, address, map);
        db.closeConnection();
        return rs ;
    }
}
