package exam.nlb2t.epot;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.PersonBill.BillAdapterItemInfo;
import exam.nlb2t.epot.PersonBill.OrderTab_Default;
import exam.nlb2t.epot.PersonBill.OrderTab_InShipping;
import exam.nlb2t.epot.PersonBill.OrderTab_Success;
import exam.nlb2t.epot.PersonBill.OrderTab_WaitForComfirm;
import exam.nlb2t.epot.singleton.Helper;

public class OrderFragment extends DialogFragment{

    View myFragment;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton back;
    int position;

    List<List<BillAdapterItemInfo>> data;
    List<OrderTab> tabs;

    protected int lastIndex = 1;
    protected int step = 20;

    public OrderFragment(int pos) {
        position=pos;
        data = new ArrayList<>();
        for(int i = 0; i< 4; i++)
        {
            data.add(new ArrayList<>());
        }

    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment= inflater.inflate(R.layout.fragment_order, container, false);
        viewPager=myFragment.findViewById(R.id.order_viewpager);
        tabLayout=myFragment.findViewById(R.id.order_tablayout);
        back=myFragment.findViewById(R.id.btn_back1);

        return myFragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEventHandler();
        setUpViewPager(viewPager);
        selectPage(position);
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

        OrderTab_InShipping tab_inShipping = new OrderTab_InShipping();
        tab_inShipping.setOnSubmitVertifyBillListener(new Helper.OnSuccessListener() {
            @Override
            public void OnSuccess(Object sender) {
                tabs.get(2).reLoad();
                tabs.get(3).reLoad();
            }
        });

        tabs = new ArrayList<>(4);
        tabs.add(new OrderTab_Default());
        tabs.add(new OrderTab_WaitForComfirm());
        tabs.add(tab_inShipping);
        tabs.add(new OrderTab_Success());

        adapter.addFragment(tabs.get(3), "Đã mua");
        adapter.addFragment(tabs.get(2), "Đang giao");
        adapter.addFragment(tabs.get(1), "Chờ xác nhận");
        adapter.addFragment(tabs.get(0), "Đã hủy");

        viewPager.setAdapter(adapter);
    }

    private void setEventHandler() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    void selectPage(int pageIndex){
        tabLayout.setScrollPosition(pageIndex,0f,true);
        viewPager.setCurrentItem(pageIndex);
    }

}
