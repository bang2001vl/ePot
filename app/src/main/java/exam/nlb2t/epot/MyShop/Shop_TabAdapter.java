package exam.nlb2t.epot.MyShop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Shop_TabAdapter extends FragmentStatePagerAdapter {
    String[] tagTitles;
    Fragment[] fragments;

    public Shop_TabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tagTitles = new String[]{"Tổng quan", "Sản phẩm", "Đơn hàng"};
        fragments = new Fragment[tagTitles.length];
        fragments[0] = new Shop_OverviewFragment();
        fragments[1] = new Shop_ProductFragment();
        fragments[2] = new Shop_OrderFragment();
    }

    public void reloadBill_InShipping(){
        Shop_OrderFragment shopOrderFragment = (Shop_OrderFragment) fragments[2];
        shopOrderFragment.reloadData_InShipping();
    }

    @Override
    public int getCount() {
        return tagTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tagTitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }


}
