package exam.nlb2t.epot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.DialogFragment.DetailBillFragment;
import exam.nlb2t.epot.MyShop.AddProductFragment;
import exam.nlb2t.epot.MyShop.Product_TabAdapter;
import exam.nlb2t.epot.PersonBill.BillAdapter;
import exam.nlb2t.epot.PersonBill.BillAdapterItemInfo;
import exam.nlb2t.epot.Views.LoadingView;
import exam.nlb2t.epot.databinding.FragmentEmptyBillBinding;
import exam.nlb2t.epot.databinding.MyShopProductTabBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class OrderTab extends Fragment {

    View v;
    List<BillAdapterItemInfo> bills;
    RecyclerView recyclerView;
    ViewBinding emptybinding = null;
    public BillAdapter recyclerViewAdapter;
    public String buttonText;
    public BillRecyclerViewAdapter.OnClickBtnDetailListener buttonClickListner;

    protected int lastIndex = 0;
    protected int step = 20;

    public OrderTab()
    {
        bills = new ArrayList<>();
        buttonClickListner = new BillRecyclerViewAdapter.OnClickBtnDetailListener() {
            @Override
            public void onClick(BillBaseDB bill, int position) {
                DetailBillFragment dialog = new DetailBillFragment(bill.id, getContext());
                dialog.show(getChildFragmentManager(), DetailBillFragment.NAMEDIALOG);
            }
        };
        loadMore();
    }

    public void reLoad(){
        int oldCount = bills.size();
        if(oldCount > 0) {
            bills.clear();
            if (recyclerViewAdapter != null) {
                recyclerViewAdapter.notifyItemRangeRemoved(0, oldCount);
            }
        }
        lastIndex = 0;
        layoutData();
        loadMore();
    }

    public void loadMore()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<BillAdapterItemInfo> list = loadDataFromDB();
                bills.addAll(bills.size(), list);
                if(list.size() > 0)
                {
                    if(getActivity() == null){return;}
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (recyclerViewAdapter != null) {
                                recyclerViewAdapter.notifyItemRangeInserted(bills.size() - list.size() -1, bills.size());
                            }
                            layoutData();

                            lastIndex += list.size();
                        }
                    });
                }

                if(list.size() < step) {
                    // No more data
                }
            }
        }).start();
    }

    public List<BillAdapterItemInfo> loadDataFromDB() {
        List<BillAdapterItemInfo> rs = null;
        DBControllerBill db = new DBControllerBill();
        rs = db.getBillCustomer_ByStatus(Authenticator.getCurrentUser().id, BillBaseDB.BillStatus.DEFAULT, lastIndex, step);
        db.closeConnection();

        return rs;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO: Find UserID to login app

        v=inflater.inflate(R.layout.fragment_order_tab,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.Recycelview_bill);

        recyclerViewAdapter = new BillAdapter(bills);
        recyclerViewAdapter.setOnBindingLastPositionListener(postion -> loadMore());

        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

        if(buttonText != null){recyclerViewAdapter.setBtnDetailText(buttonText);}
        if(buttonClickListner != null){recyclerViewAdapter.setOnBtnDetailClickListener(buttonClickListner);}
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : Write code here <Get data from database and set to view>
        layoutData();
    }

    boolean isLoading = false;
    public void showLoadingScreen()
    {
        if(v==null || isLoading) return;
        ConstraintLayout constraintLayout = (ConstraintLayout)v;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        constraintLayout.addView(new LoadingView(getContext()), constraintLayout.getChildCount(), params);
        isLoading = true;
    }

    public void hideLoadingScreen()
    {
        if(v==null || !isLoading) return;
        ConstraintLayout constraintLayout = (ConstraintLayout)v;
        constraintLayout.removeViewAt(constraintLayout.getChildCount() - 1);
        isLoading = false;
    }

    void layoutData()
    {
        if(v==null) return;
        ConstraintLayout constraintLayout = (ConstraintLayout)v;
        if (bills.size() == 0) {
            if(emptybinding == null) {
                emptybinding = FragmentEmptyBillBinding.inflate(getLayoutInflater(), constraintLayout, false);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                constraintLayout.addView(emptybinding.getRoot(), constraintLayout.getChildCount(), params);
            }
            return;
        }

        if(emptybinding != null)
        {
            constraintLayout.removeView(emptybinding.getRoot());
            emptybinding = null;
        }
    }
}
