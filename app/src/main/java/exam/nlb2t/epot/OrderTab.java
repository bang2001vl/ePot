package exam.nlb2t.epot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.MyShop.AddProductFragment;
import exam.nlb2t.epot.MyShop.Product_TabAdapter;
import exam.nlb2t.epot.databinding.MyShopProductTabBinding;
import exam.nlb2t.epot.singleton.Helper;

public class OrderTab extends Fragment {

    View v;
    List<BillBaseDB> bills;
    RecyclerView recyclerView;
    public OrderTab(){};

    public OrderTab(List<BillBaseDB> Listbill)
    {
        bills=Listbill;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO: Find UserID to login app
        v=inflater.inflate(R.layout.fragment_order_tab,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.Recycelview_bill);
        BillRecyclerViewAdapter recyclerViewAdapter=new BillRecyclerViewAdapter(bills,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : Write code here <Get data from database and set to view>
    }

}