package exam.nlb2t.epot.MyShop;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Order_TabAdapter extends FragmentStatePagerAdapter {
    String[] tabtitles;
    Fragment[] fragments;

    public Order_TabAdapter(@NonNull FragmentManager frag, int behavior) {
        super(frag, behavior);

        tabtitles = new String[]{"Tất cả", "Chờ xác nhận", "Đang giao", "Đã hủy"};
        fragments = new Fragment[tabtitles.length];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
