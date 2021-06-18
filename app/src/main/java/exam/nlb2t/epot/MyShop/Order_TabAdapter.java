package exam.nlb2t.epot.MyShop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.singleton.Authenticator;

public class Order_TabAdapter extends FragmentStatePagerAdapter {
    String[] tabtitles;
    Fragment[] fragments;

    public Order_TabAdapter(@NonNull FragmentManager frag, int behavior) {
        super(frag, behavior);

        tabtitles = new String[]{"Tất cả", "Chờ xác nhận", "Đang giao", "Đã hủy"};
        fragments = new Fragment[tabtitles.length];
        fragments[0] = new Shop_BillFragment(null);
        fragments[1] = new Shop_BillFragment(BillBaseDB.BillStatus.WAIT_CONFIRM);
        fragments[2] = new Shop_BillFragment(BillBaseDB.BillStatus.IN_SHIPPING);
        fragments[3] = new Shop_BillFragment(BillBaseDB.BillStatus.DEFAULT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
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
