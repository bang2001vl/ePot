package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import exam.nlb2t.epot.OrderAdapter;
import exam.nlb2t.epot.OrderTab;
import exam.nlb2t.epot.R;

public class OrderFragment extends DialogFragment{

    View myFragment;
    TabLayout tabLayout;
    ViewPager viewPager;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }
    public static OrderFragment getInstance(){
        return new OrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment= inflater.inflate(R.layout.fragment_order, container, false);
        viewPager=myFragment.findViewById(R.id.order_viewpager);
        tabLayout=myFragment.findViewById(R.id.order_tablayout);
        return myFragment;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });

    }

    private void setUpViewPager(ViewPager viewPager) {
        OrderAdapter adapter=new OrderAdapter(getChildFragmentManager());
        adapter.addFragment(new OrderTab(), "Đã mua");
        adapter.addFragment(new OrderTab(), "Đang giao");
        adapter.addFragment(new OrderTab(), "Đã hủy");

        viewPager.setAdapter(adapter);
    }

}