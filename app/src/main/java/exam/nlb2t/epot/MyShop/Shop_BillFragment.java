package exam.nlb2t.epot.MyShop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.List;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.EmptyBillFragment;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentEmptyBillBinding;
import exam.nlb2t.epot.databinding.FragmentOrderTabBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class Shop_BillFragment extends Fragment {
    Bill_TabAdapter adapter;
    List<BillBaseDB> listBill;
    BillBaseDB.BillStatus statusBill;
    ViewBinding emptybinding;
    FragmentOrderTabBinding binding;

    public Shop_BillFragment(BillBaseDB.BillStatus status) {
        this.statusBill = status;

        DBControllerBill db = new DBControllerBill();
        listBill = db.getBillsOverviewbyStatus(Authenticator.getCurrentUser().id, status);
        db.closeConnection();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderTabBinding.inflate(inflater, container, false);

        adapter = new Bill_TabAdapter(listBill, statusBill);
        adapter.setNotifyStatusChangedListener(notifyStatusChangedListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.RecycelviewBill.setLayoutManager(layoutManager);

        binding.RecycelviewBill.setItemViewCacheSize(10);
        binding.RecycelviewBill.setDrawingCacheEnabled(true);
        binding.RecycelviewBill.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        binding.RecycelviewBill.setAdapter(adapter);

        return binding.getRoot();
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
        }
        else {
            listBill.add(bill);
            if (adapter != null) adapter.notifyItemInserted(listBill.size() - 1);
        }
    }

}