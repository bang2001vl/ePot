package exam.nlb2t.epot.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerNotification;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.NotificationBaseDB;
import exam.nlb2t.epot.NotificationWorkspace.NewBillNotificationView;
import exam.nlb2t.epot.NotificationWorkspace.SuccessBillNotifyView_Customer;
import exam.nlb2t.epot.NotificationWorkspace.SuccessBillNotifyView_Saler;
import exam.nlb2t.epot.NotificationWorkspace.VertifyBillNotificationView;
import exam.nlb2t.epot.databinding.EmptyCartLayoutBinding;
import exam.nlb2t.epot.databinding.FragmentCartThachBinding;
import exam.nlb2t.epot.databinding.FragmentNotificationBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class NotificationFragment extends Fragment {
    FragmentNotificationBinding binding;
    EmptyCartLayoutBinding bindingEmpty;
    int lastIndex = 0;
    int step = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();
    }

    public void requestLoadData()
    {
        List<NotificationBaseDB> list = new ArrayList<>();
        DBControllerNotification db = new DBControllerNotification();
        list = db.getNotidication(Authenticator.getCurrentUser().id, lastIndex, lastIndex+step-1);
        db.closeConnection();

        if(list.size() == 0 && lastIndex == 0)
        {
            if(bindingEmpty != null)
            {
                bindingEmpty = EmptyCartLayoutBinding.inflate(getLayoutInflater(), binding.contentLayout, false);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                binding.contentLayout.addView(bindingEmpty.getRoot(), binding.contentLayout.getChildCount(), params);
            }

            return;
        }

        if(bindingEmpty != null)
        {
            bindingEmpty = null;
            binding.contentLayout.removeViewAt(binding.contentLayout.getChildCount() -1);
        }

        List<BillBaseDB> bills = new ArrayList<>();
        for(NotificationBaseDB noti:list)
        {
            // get bills overview
        }

        for(int i = 0; i<list.size(); i++)
        {
            NotificationBaseDB noti = list.get(i);
            BillBaseDB bill = bills.get(i);

            View view = null;
            if(noti.newStatus == 1)
            {
                view = createNotify_New(noti, bill.keyBill);
            }
            else if(noti.newStatus == 2)
            {
                view = createNotify_Vertify(noti, bill.keyBill);
            }
            else if(noti.newStatus == 3)
            {
                if(noti.receiverID == bill.userID)
                {
                    view = createNotify_SuccessCustomer(noti, bill.keyBill);
                }
                else {
                    view = createNotify_SuccessSaler(noti, bill.keyBill);
                }
            }

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);

            binding.linearLayoutMain.addView(view, lastIndex);
        }

        if(list.size() < step)
        {
            binding.nextButtonLayout.setVisibility(View.GONE);
        }

        lastIndex += list.size();
    }

    View createNotify_New(NotificationBaseDB noti, String key)
    {
        NewBillNotificationView rs = new NewBillNotificationView(getContext());
        rs.setBillKey(key);
        rs.setTime(noti.createdDate);
        return rs;
    }

    View createNotify_Vertify(NotificationBaseDB noti, String key)
    {
        VertifyBillNotificationView rs = new VertifyBillNotificationView(getContext());
        rs.setBillKey(key);
        rs.setTime(noti.createdDate);
        return rs;
    }

    View createNotify_SuccessCustomer(NotificationBaseDB noti, String key)
    {
        SuccessBillNotifyView_Customer rs = new SuccessBillNotifyView_Customer(getContext());
        rs.setBillKey(key);
        rs.setTime(noti.createdDate);
        return rs;
    }

    View createNotify_SuccessSaler(NotificationBaseDB noti, String key)
    {
        SuccessBillNotifyView_Saler rs = new SuccessBillNotifyView_Saler(getContext());
        rs.setBillKey(key);
        rs.setTime(noti.createdDate);
        return rs;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : Write code here <Get data from database and set to view>
    }

    void setEventHandler()
    {
        //TODO : Write code here <Set all listener in here>
    }


}
