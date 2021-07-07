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
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductInBill;
import exam.nlb2t.epot.OrderTab;
import exam.nlb2t.epot.databinding.MyShopOrderTabBinding;
import exam.nlb2t.epot.databinding.OrderTabBinding;

public class Shop_OrderFragment extends Fragment {
    MyShopOrderTabBinding binding;
    Order_TabAdapter adapter;

    public static final String NOTIFY_BILL_WAIT_CONFIRM_ADDED = "NotifyNewBillAdded";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyShopOrderTabBinding.inflate(inflater,container,false);
        setEventHandler();

        adapter = new Order_TabAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.myShopOrderViewpager.setAdapter(adapter);
        binding.myShopOrderViewpager.setOffscreenPageLimit(4);
        binding.myShopOrderTab.setupWithViewPager(binding.myShopOrderViewpager);

        return binding.getRoot();
    }

    private void setEventHandler() {
        getChildFragmentManager().setFragmentResultListener(Shop_BillFragment.NOTIFY_STATUS_CHANGED, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                getParentFragmentManager().setFragmentResult(Shop_BillFragment.NOTIFY_STATUS_CHANGED_TO_OVERVIEW_FRAGMENT, result);
                getParentFragmentManager().setFragmentResult(Shop_BillFragment.NOTIFY_STATUS_CHANGED_TO_PRODUCT_FRAGMENT, result);
            }
        });
    }

    public void reloadData_WaitComfirm(){
        Log.d("MY_TAG", "Path 3");
        if(adapter == null) return;
        adapter.ReloadBill(null);
        adapter.ReloadBill(BillBaseDB.BillStatus.WAIT_CONFIRM);
        getParentFragmentManager().setFragmentResult(Shop_BillFragment.NOTIFY_STATUS_CHANGED_TO_OVERVIEW_FRAGMENT, new Bundle());
    }
}
