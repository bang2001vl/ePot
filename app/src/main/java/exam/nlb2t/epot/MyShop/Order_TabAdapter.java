package exam.nlb2t.epot.MyShop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import exam.nlb2t.epot.Database.DBControllerBill;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.EmptyBillFragment;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentEmptyBillBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class Order_TabAdapter extends FragmentStatePagerAdapter {
    String[] tabtitles;
    Fragment[] fragments;

    public Order_TabAdapter(@NonNull FragmentManager frag, int behavior) {
        super(frag, behavior);

        tabtitles = new String[]{"Tất cả", "Chờ xác nhận", "Đang giao", "Hủy bỏ", "Thành công"};
        fragments = new Fragment[tabtitles.length];
//        DBControllerBill db = new DBControllerBill();
//        int countBills = db.getNumberBillbyStatus(Authenticator.getCurrentUser().id,null);
//        if (countBills == 0) {
//            fragments[0] = new EmptyBillFragment();
//        }
//        else {
//            fragments[0] = new Shop_BillFragment(null);
//        }
//
//        for (int i = 0; i < tabtitles.length - 1; i++) {
//            countBills = db.getNumberBillbyStatus(Authenticator.getCurrentUser().id, BillBaseDB.BillStatus.values()[i]);
//            if (countBills == 0) {
//                fragments[i+1] = new EmptyBillFragment();
//            }
//            else {
//                fragments[i+1] = new Shop_BillFragment(BillBaseDB.BillStatus.values()[i]);
//            }
//        }
//        db.closeConnection();

        fragments[0] = new Shop_BillFragment(null);
        fragments[1] = new Shop_BillFragment(BillBaseDB.BillStatus.WAIT_CONFIRM);
        fragments[2] = new Shop_BillFragment(BillBaseDB.BillStatus.IN_SHIPPING);
        fragments[3] = new Shop_BillFragment(BillBaseDB.BillStatus.DEFAULT);
        fragments[4] = new Shop_BillFragment(BillBaseDB.BillStatus.SUCCESS);

        setEventHandler();
    }

    private void setEventHandler() {
        for (int i=0; i< fragments.length; i ++) {
            ((Shop_BillFragment)fragments[i]).setNotifyStatusChangedListener((f,t)->receiveNotifyChanged(f,t));
        }
    }
    int fromIndex,toIndex;
    private void receiveNotifyChanged(BillBaseDB.BillStatus fromStatus, BillBaseDB.BillStatus toStatus) {
        switch (fromStatus) {
            case DEFAULT:
                fromIndex = 3;
                break;
            case SUCCESS:
                fromIndex = 4;
                break;
            case IN_SHIPPING:
                fromIndex = 2;
                break;
            case WAIT_CONFIRM:
                fromIndex = 1;
                break;
        }
        switch (toStatus) {
            case DEFAULT:
                toIndex = 3;
                break;
            case SUCCESS:
                toIndex = 4;
                break;
            case IN_SHIPPING:
                toIndex = 2;
                break;
            case WAIT_CONFIRM:
                toIndex = 1;
                break;
        }
        //notifyStatusChangedListener.notifyChanged(fromStatus,toStatus);
        notifyDataSetChanged();
//        for (int i = 0; i < fragments.length; i++) {
//            Bill_TabAdapter adapter = ((Shop_BillFragment)fragments[i]).getBill_Tab_Adapter();
//            if (adapter.status == toStatus) {
//                //TODO: Notify item in fragment have Status = toStatus is added
//                Log.i("STATUS_CHANGED", "Notify fragment " + i + " is added");
//                adapter.notifyItemInserted(adapter.billList.size());
//            }
//        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment frag;
        DBControllerBill db = new DBControllerBill();
        int countBills = db.getNumberBillbyStatus(Authenticator.getCurrentUser().id,((Shop_BillFragment)fragments[position]).statusBill);

        if (countBills == 0) {
            frag = new EmptyBillFragment();
        }
        else frag = fragments[position];
        return frag;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof Shop_BillFragment) {
            Shop_BillFragment fragment = (Shop_BillFragment)object;
            if (fragment == fragments[0] || fragment == fragments[fromIndex] || fragment == fragments[toIndex]) return POSITION_NONE;
        }
        return POSITION_UNCHANGED;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
