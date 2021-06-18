package exam.nlb2t.epot.MyShop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.databinding.FragmentEmptyBillBinding;
import exam.nlb2t.epot.databinding.FragmentOrderTabBinding;

public class Shop_BillFragment extends Fragment {
    Bill_TabAdapter adapter;
    BillBaseDB.BillStatus statusBill;
    ViewBinding emptybinding;
    FragmentOrderTabBinding binding;
    int userID;

    public Shop_BillFragment(int userID, BillBaseDB.BillStatus status) {
        this.statusBill = status;
        this.userID = userID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DBControllerBill db = new DBControllerBill();
        int countBills = db.getNumberBillbyStatus(userID, statusBill);
        db.closeConnection();

        if (countBills == 0) {
            emptybinding = FragmentEmptyBillBinding.inflate(inflater,container,false);

            return emptybinding.getRoot();
        }
        else {
            binding = FragmentOrderTabBinding.inflate(inflater, container, false);
            adapter = new Bill_TabAdapter(userID, statusBill);
            //TODO:Set adapter for binding

            return binding.getRoot();
        }
    }
}
