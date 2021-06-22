package exam.nlb2t.epot.MyShop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import exam.nlb2t.epot.ClassData.Bill;
import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.EmptyBillFragment;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentEmptyBillBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class Order_TabAdapter extends FragmentStatePagerAdapter {
    String[] tabtitles;
    Fragment[] fragments;
    Fragment[] emptyfragments;

    public Order_TabAdapter(@NonNull FragmentManager frag, int behavior) {
        super(frag, behavior);

        tabtitles = new String[]{"Tất cả", "Chờ xác nhận", "Đang giao", "Đơn hủy", "Thành công"};
        fragments = new Fragment[tabtitles.length];
        fragments[0] = new Shop_BillFragment(null);
        fragments[1] = new Shop_BillFragment(BillBaseDB.BillStatus.WAIT_CONFIRM);
        fragments[2] = new Shop_BillFragment(BillBaseDB.BillStatus.IN_SHIPPING);
        fragments[3] = new Shop_BillFragment(BillBaseDB.BillStatus.DEFAULT);
        fragments[4] = new Shop_BillFragment(BillBaseDB.BillStatus.SUCCESS);

        emptyfragments = new Fragment[tabtitles.length];
        for (int i=0; i<tabtitles.length; i++) {
            emptyfragments[i] = new EmptyBillFragment();
        }

        setEventHandler();
    }

    private void setEventHandler() {
        for (int i=0; i< fragments.length; i ++) {
            ((Shop_BillFragment)fragments[i]).setNotifyStatusChangedListener((f,t,b)->receiveNotifyChanged(f,t,b));
        }
    }
    int fromIndex,toIndex;
    private void receiveNotifyChanged(@NonNull BillBaseDB.BillStatus fromStatus, BillBaseDB.BillStatus toStatus, BillBaseDB bill) {
        switch (fromStatus) {
            case DEFAULT:
                fromIndex = 3;
                break;
            case SUCCESS:
                fromIndex = 4;
                break;
            case IN_SHIPPING:
                fromIndex = 2;
                break;
            case WAIT_CONFIRM:
                fromIndex = 1;
                break;
        }
        switch (toStatus) {
            case DEFAULT:
                toIndex = 3;
                break;
            case SUCCESS:
                toIndex = 4;
                break;
            case IN_SHIPPING:
                toIndex = 2;
                break;
            case WAIT_CONFIRM:
                toIndex = 1;
                break;
        }

        Shop_BillFragment fragAtfromIndex = ((Shop_BillFragment)fragments[fromIndex]);
        Shop_BillFragment fragAttoIndex = ((Shop_BillFragment)fragments[toIndex]);
        Shop_BillFragment fragAt0 = ((Shop_BillFragment)fragments[0]);

        fragAt0.TranferStatus(bill, toStatus);
        fragAttoIndex.TranferStatus(bill, toStatus);

        if (fragAttoIndex.listBill.size() == 1 || fragAtfromIndex.listBill.size() == 0) notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment frag;
        if (((Shop_BillFragment)fragments[position]).listBill.size() == 0) {
            frag = emptyfragments[position];
        }
        else frag = fragments[position];
        return frag;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment == fragments[0] || fragment == fragments[fromIndex] || fragment == emptyfragments[toIndex]) return POSITION_NONE;
        return POSITION_UNCHANGED;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
