package exam.nlb2t.epot.MyShop;

import android.os.Bundle;
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

import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.OrderTab;
import exam.nlb2t.epot.databinding.MyShopOrderTabBinding;
import exam.nlb2t.epot.databinding.OrderTabBinding;

public class Shop_OrderFragment extends Fragment {
    MyShopOrderTabBinding binding;
    Order_TabAdapter adapter;

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
        adapter.ReloadBill(BillBaseDB.BillStatus.WAIT_CONFIRM);
    }
}
