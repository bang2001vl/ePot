package exam.nlb2t.epot.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerNotification;
import exam.nlb2t.epot.Database.Tables.BillBaseDB;
import exam.nlb2t.epot.Database.Tables.NotificationBaseDB;
import exam.nlb2t.epot.NotificationWorkspace.NewBillNotificationView;
import exam.nlb2t.epot.NotificationWorkspace.NotifyViewAdapter;
import exam.nlb2t.epot.NotificationWorkspace.NotifycationInfo;
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
    int lastIndex = 1;
    int step = 2;
    NotifyViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        setEventHandler();
        binding.nextButtonLayout.setOnClickListener(v->{
            loadMore();
        });
        adapter = new NotifyViewAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        loadMore();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void loadMore()
    {
        List<NotifycationInfo> list = new ArrayList<>();
        DBControllerNotification db = new DBControllerNotification();
        list = db.getNotidication(Authenticator.getCurrentUser().id, lastIndex, lastIndex+step-1);
        db.closeConnection();

        if(list.size() == 0 && lastIndex == 1)
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

        for(int i = 0; i<list.size(); i++)
        {
            /*NotifycationInfo info = list.get(i);
            NotificationBaseDB noti = info.notification;

            View view = null;
            if(noti.newStatus == 1)
            {
                view = createNotify_New(noti, info.keyBill);
            }
            else if(noti.newStatus == 2)
            {
                view = createNotify_Vertify(noti, info.keyBill);
            }
            else if(noti.newStatus == 3)
            {
                if(noti.receiverID != info.salerID)
                {
                    view = createNotify_SuccessCustomer(noti, info.keyBill);
                }
                else {
                    view = createNotify_SuccessSaler(noti, info.keyBill);
                }
            }

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            binding.linearLayoutMain.addView(view, binding.linearLayoutMain.getChildCount() - 1, params);*/

        }

        adapter.addItem(list);

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

    void setEventHandler()
    {
        //TODO : Write code here <Set all listener in here>
    }


}
