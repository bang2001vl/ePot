package exam.nlb2t.epot;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.List;
import exam.nlb2t.epot.databinding.FragmentOrderBinding;
import exam.nlb2t.epot.ClassInformation.User;
import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.singleton.Authenticator;

public class OrderFragment extends DialogFragment{

    View myFragment;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton back;
    DBControllerBill dbControllerBill=new DBControllerBill();


    public OrderFragment(int ID) {
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
        adapter.addFragment(new OrderTab(dbControllerBill.getBillsOverviewbyStatus(Authenticator.getCurrentUser().id,BillBaseDB.BillStatus.SUCCESS)), "Đã mua");
        adapter.addFragment(new OrderTab(dbControllerBill.getBillsOverviewbyStatus(Authenticator.getCurrentUser().id,BillBaseDB.BillStatus.IN_SHIPPING)), "Đang giao");
        adapter.addFragment(new OrderTab(dbControllerBill.getBillsOverviewbyStatus(Authenticator.getCurrentUser().id,BillBaseDB.BillStatus.DEFAULT)), "Đã hủy");

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

}