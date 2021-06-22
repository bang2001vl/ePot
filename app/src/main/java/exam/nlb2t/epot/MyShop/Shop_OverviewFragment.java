package exam.nlb2t.epot.MyShop;

import android.os.Bundle;
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
        //TODO : Write code here <Set all listener in here>
        getParentFragmentManager().setFragmentResultListener(Bill_TabAdapter.NOTIFY_STATUS_CHANGED_MESSAGE, Shop_OverviewFragment.this, (resquestKey,result) -> {
            Log.i("Test", "test ne 2");
            loadData();
        });
    }

    public void loadData() {
        DBControllerBill db = new DBControllerBill();
        int[] listNumber = db.getAllNumberBill(Authenticator.getCurrentUser().id);
        db.closeConnection();
        if (listNumber != null) {
            binding.itemComplete.setValue(listNumber[BillBaseDB.BillStatus.SUCCESS.getValue()]);
            binding.itemShipping.setValue(listNumber[BillBaseDB.BillStatus.IN_SHIPPING.getValue()]);
            binding.itemConfirm.setValue(listNumber[BillBaseDB.BillStatus.WAIT_CONFIRM.getValue()]);
            binding.itemCancel.setValue(listNumber[BillBaseDB.BillStatus.DEFAULT.getValue()]);
        }

        DBControllerProduct db2 = new DBControllerProduct();
        binding.itemSell.setValue(db2.getNumberProducts(Authenticator.getCurrentUser().id));
        binding.itemOutofstock.setValue(db2.getNumberProductOutofStock(Authenticator.getCurrentUser().id));
        db2.closeConnection();
    }
}
