package exam.nlb2t.epot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.DialogFragment.DetailBillFragment;
import exam.nlb2t.epot.PersonBill.BillAdapter;
import exam.nlb2t.epot.PersonBill.BillAdapterItemInfo;
import exam.nlb2t.epot.databinding.FragmentEmptyBillBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class OrderTab extends Fragment {

    View v;
    protected List<BillAdapterItemInfo> bills;
    RecyclerView recyclerView;
    ViewBinding emptybinding = null;
    public BillAdapter recyclerViewAdapter;
    public String buttonText;
    public BillRecyclerViewAdapter.OnClickBtnDetailListener buttonClickListner;
    View gifLoadingCircle;

    protected int lastIndex = 0;
    protected int step = 20;
    boolean hasMoreData = true;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO: Find UserID to login app

        v=inflater.inflate(R.layout.fragment_order_tab,container,false);
        gifLoadingCircle = v.findViewById(R.id.gif_loading_circle);

        recyclerView = (RecyclerView) v.findViewById(R.id.Recycelview_bill);

        recyclerViewAdapter = new BillAdapter(bills);

        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

        if(buttonText != null){recyclerViewAdapter.setBtnDetailText(buttonText);}
        if(buttonClickListner != null){recyclerViewAdapter.setOnBtnDetailClickListener(buttonClickListner);}

        ScrollView scrollView = (ScrollView) v.findViewById(R.id.scroll_view_main);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if(bills.size() == 0) return;
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            if(view == null) return;
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff == 0 && hasMoreData) {
                showLoading();
                loadMore();
            }
        });

        emptybinding = null;
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showLoading();
        reLoad();
    }

    public void reLoad(){
        int oldCount = bills.size();
        if(oldCount > 0) {
            bills.clear();
            if (recyclerViewAdapter != null) {
                recyclerViewAdapter.notifyItemRangeRemoved(0, oldCount);
            }
        }
        hasMoreData = true;
        lastIndex = 0;
        loadMore();
    }

    public void loadMore() {
        if (!hasMoreData) return;
        hasMoreData = false;
        new Thread(() -> {
            List<BillAdapterItemInfo> list = loadDataFromDB();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    bills.addAll(bills.size(), list);
                    if (recyclerViewAdapter != null && list.size() > 0) {
                        recyclerViewAdapter.notifyItemRangeInserted(bills.size() - 1, bills.size());
                    }
                    layoutData();
                    hideLoading();
                });
            }

            hasMoreData = list.size() == step;
            lastIndex += list.size();
        }).start();
    }

    public List<BillAdapterItemInfo> loadDataFromDB() {
        List<BillAdapterItemInfo> rs = null;
        DBControllerBill db = new DBControllerBill();
        rs = db.getBillCustomer_ByStatus(Authenticator.getCurrentUser().id, BillBaseDB.BillStatus.DEFAULT, lastIndex, step);
        db.closeConnection();

        return rs;
    }

    public void showLoading()
    {
        gifLoadingCircle.setEnabled(false);
        gifLoadingCircle.setVisibility(View.VISIBLE);
    }

    public void hideLoading()
    {
        gifLoadingCircle.setEnabled(true);
        gifLoadingCircle.setVisibility(View.GONE);
    }

    void layoutData()
    {
        if(v==null) return;
        ViewGroup contentLayout = (ViewGroup) v;
        if (bills.size() == 0) {
            if(emptybinding == null) {
                emptybinding = FragmentEmptyBillBinding.inflate(getLayoutInflater(), contentLayout, false);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentLayout.addView(emptybinding.getRoot(), contentLayout.getChildCount(), params);
            }
            return;
        }

        if(emptybinding != null)
        {
            contentLayout.removeView(emptybinding.getRoot());
            emptybinding = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        emptybinding = null;
    }
}
