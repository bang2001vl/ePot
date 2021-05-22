package exam.nlb2t.epot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class OrderAdapter extends FragmentPagerAdapter{

    private  List<Fragment> fragmentList=new ArrayList<>();
    private List<String> titleList=new ArrayList<>();

    public OrderAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public Fragment getItem(int position) {
        return fragmentList.get(position); }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

}

