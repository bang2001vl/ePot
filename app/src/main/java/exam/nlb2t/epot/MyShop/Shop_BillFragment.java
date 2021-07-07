package exam.nlb2t.epot.MyShop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductInBill;
import exam.nlb2t.epot.databinding.FragmentOrderTabBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class Shop_BillFragment extends Fragment {
    Bill_TabAdapter adapter;
    final List<BillBaseDB> listBill;
    final Handler mHandler = new Handler(Looper.myLooper());
    int lastIndex;
    int step;
    boolean hasMoreData;

    BillBaseDB.BillStatus statusBill;
    FragmentOrderTabBinding binding;

    public static final String NOTIFY_STATUS_CHANGED = "NotifyStatusChange";
    public static final String NOTIFY_STATUS_CHANGED_TO_PRODUCT_FRAGMENT = "NotifyStatusChange_Product";
    public static final String NOTIFY_STATUS_CHANGED_TO_OVERVIEW_FRAGMENT = "NotifyStatusChange_Overview";

    public Shop_BillFragment(BillBaseDB.BillStatus status) {
        this.statusBill = status;
        listBill = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderTabBinding.inflate(inflater, container, false);
        adapter = new Bill_TabAdapter(listBill, statusBill);
        adapter.setHasStableIds(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.RecycelviewBill.setLayoutManager(layoutManager);

        binding.RecycelviewBill.setItemViewCacheSize(10);
        binding.RecycelviewBill.setDrawingCacheEnabled(true);
        binding.RecycelviewBill.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        binding.RecycelviewBill.setAdapter(adapter);


        setEventHandler();
        binding.RecycelviewBill.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    //scroll down
                    int visibleItemsCount = layoutManager.getChildCount();
                    int passVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();

                    if (passVisibleItems + visibleItemsCount == totalItemCount) {
                        //if (passVisibleItems + visibleItemsCount == totalItemCount)
                        //wait for load data finished
                        loadAsyncData();
                    }
                }
            }
        });

        return binding.getRoot();
    }

    boolean isFullBill;
    public void loadAsyncData() {
        if (isFullBill) return;

        isFullBill = true;
        int step = 5;

        new Thread(new Runnable() {
            @Override
            public void run() {
                DBControllerBill db = new DBControllerBill();
                List<BillBaseDB> data = db.getBillsOverviewbyStatus(Authenticator.getCurrentUser().id, statusBill, listBill.size(), step);
                db.closeConnection();

                isFullBill = data.size() < step;

                mHandler.post(() -> {
                    listBill.addAll(data);
                    if (Order_TabAdapter.NUMBER_BILL_LOAD_DONE < 5) {
                        //MEANS: Load init
                        synchronized (Order_TabAdapter.class) {
                            Order_TabAdapter.NUMBER_BILL_LOAD_DONE++;
                        }
                        OnListBillChanged.OnChanged();
                    }
                    if (adapter != null) {
                        adapter.notifyItemRangeInserted(listBill.size() - data.size(), data.size());
                    }
                });
//                if(getActivity() != null)
//                {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            listBill.addAll(data);
//                            if(adapter != null){
//                                adapter.notifyItemRangeInserted(0, data.size());
//                            }
//                            OnListBillChanged.OnChanged();
//                        }
//                    });
//                }
            }
        }).start();
    }

    Bill_TabAdapter.OnStatusTableChangedListener notifyStatusChangedListener;

    public void setNotifyStatusChangedListener(Bill_TabAdapter.OnStatusTableChangedListener notifyStatusChanged) {
        this.notifyStatusChangedListener = notifyStatusChanged;
    }

    public void TranferStatus(BillBaseDB bill, BillBaseDB.BillStatus newStatus) {
        if (listBill.contains(bill)) {
            int index = listBill.indexOf(bill);
            listBill.get(index).status = newStatus;
            adapter.notifyItemChanged(index);
        } else {
            listBill.add(bill);
            if (adapter != null) adapter.notifyItemInserted(listBill.size() - 1);
        }
    }

    void setEventHandler() {
        adapter.setNotifyStatusChangedListener((f, t, b) -> {
            //TODO: Notify Status Bill Change to another fragment and it's adapter
            Bundle message = new Bundle();
            DBControllerProduct db = new DBControllerProduct();
            List<ProductInBill> productInBill = db.getProductInBill(b.id);
            int[] productIDs = new int[productInBill.size()];
            int[] quantities = new int[productInBill.size()];
            for (int i = 0; i < productInBill.size(); i++) {
                productIDs[i] = productInBill.get(i).getId();
                quantities[i] = productInBill.get(i).getQuantity();
            }

            message.putInt("FromStatus", f.getValue());
            message.putInt("ToStatus", t.getValue());
            message.putIntArray("ProductIDs", productIDs);
            message.putIntArray("Quantities", quantities);

            //This ParentFragmentManger must be called before notifyStatusChangeListener because it may be null
            getParentFragmentManager().setFragmentResult(NOTIFY_STATUS_CHANGED, message);
            notifyStatusChangedListener.notifyChanged(f, t, b);
        });
    }

    public interface INotifyOnListBillChanged {
        void OnChanged();
    }

    private INotifyOnListBillChanged OnListBillChanged;

    public void setOnListBillChanged(INotifyOnListBillChanged onListBillChanged) {
        this.OnListBillChanged = onListBillChanged;
    }

//    public void ReloadBill() {
//        Shop_BillFragment newBill = new Shop_BillFragment(this.statusBill);
//        newBill.loadAsyncData();
//        getParentFragmentManager().beginTransaction().detach(Shop_BillFragment.this).attach(Shop_BillFragment.this).commit();
//    }
}
