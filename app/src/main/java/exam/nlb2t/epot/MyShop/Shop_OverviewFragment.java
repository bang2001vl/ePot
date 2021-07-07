package exam.nlb2t.epot.MyShop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.databinding.MyShopOverviewTabBinding;
import exam.nlb2t.epot.databinding.MyShopProductTabBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class Shop_OverviewFragment extends Fragment {
    MyShopOverviewTabBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyShopOverviewTabBinding.inflate(inflater, container, false);
        setEventHandler();
        loadData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : Write code here <Get data from database and set to view>
    }

    void setEventHandler() {
        getParentFragmentManager().setFragmentResultListener(Shop_BillFragment.NOTIFY_STATUS_CHANGED_TO_OVERVIEW_FRAGMENT,
                Shop_OverviewFragment.this, (resquestKey, result) -> {
                    //MEANS: Change layout when status of bill is tranfer in tab "Order"

                    new Handler(Looper.getMainLooper()).postDelayed(() -> loadData(), 100);
                });

        getParentFragmentManager().setFragmentResultListener(Shop_OrderFragment.NOTIFY_BILL_WAIT_CONFIRM_ADDED,
                Shop_OverviewFragment.this, (requestKey, result) -> {
                    //MEANS: Change Layout when Customer order one or more than product in MyShop

                    DBControllerBill db = new DBControllerBill();
                    int[] listNumber = db.getAllNumberBill(Authenticator.getCurrentUser().id);
                    db.closeConnection();

                    new Handler(Looper.getMainLooper()).postDelayed(() -> binding.itemConfirm.setValue(listNumber[BillBaseDB.BillStatus.WAIT_CONFIRM.getValue()]), 100);
                });

        getParentFragmentManager().setFragmentResultListener(Shop_ProductFragment.NOTIFY_WAREHOUSE_CHANGED,
                Shop_OverviewFragment.this, (requestKey, result) -> {
                    //MEANS: Change Layout when WareHouse of product is change, check again out of stock

                    DBControllerProduct db = new DBControllerProduct();
                    int outofstockvalue = db.getNumberProductOutofStock(Authenticator.getCurrentUser().id);
                    db.closeConnection();

                    new Handler(Looper.getMainLooper()).postDelayed(() -> binding.itemOutofstock.setValue(outofstockvalue), 100);
                });
    }

    public void loadData() {
        new Thread(() -> {
            DBControllerBill db = new DBControllerBill();
            int[] listNumber = db.getAllNumberBill(Authenticator.getCurrentUser().id);
            db.closeConnection();

            DBControllerProduct db2 = new DBControllerProduct();
            int sellvalue = db2.getNumberProducts(Authenticator.getCurrentUser().id);
            int outofstockvalue = db2.getNumberProductOutofStock(Authenticator.getCurrentUser().id);
            db2.closeConnection();

            if (listNumber != null) {
                binding.itemComplete.setValue(listNumber[BillBaseDB.BillStatus.SUCCESS.getValue()]);
                binding.itemShipping.setValue(listNumber[BillBaseDB.BillStatus.IN_SHIPPING.getValue()]);
                binding.itemConfirm.setValue(listNumber[BillBaseDB.BillStatus.WAIT_CONFIRM.getValue()]);
                binding.itemCancel.setValue(listNumber[BillBaseDB.BillStatus.DEFAULT.getValue()]);
            }
            binding.itemSell.setValue(sellvalue);
            binding.itemOutofstock.setValue(outofstockvalue);
        }).start();
    }
}
