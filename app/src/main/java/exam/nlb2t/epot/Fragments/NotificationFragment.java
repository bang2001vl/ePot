package exam.nlb2t.epot.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DBControllerNotification;
import exam.nlb2t.epot.DialogFragment.DetailBillFragment;
import exam.nlb2t.epot.EmptyNotiFragment;
import exam.nlb2t.epot.MainActivity;
import exam.nlb2t.epot.NotificationWorkspace.NotifyViewAdapter;
import exam.nlb2t.epot.NotificationWorkspace.NotifycationInfo;
import exam.nlb2t.epot.PersonBill.BillAdapter;
import exam.nlb2t.epot.databinding.EmptyCartLayoutBinding;
import exam.nlb2t.epot.databinding.FragmentEmptyNotiBinding;
import exam.nlb2t.epot.databinding.FragmentNotificationBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class NotificationFragment extends Fragment {
    FragmentNotificationBinding binding;
    FragmentEmptyNotiBinding bindingEmpty;

    protected NotifyViewAdapter adapter;
    protected List<NotifycationInfo> list;

    boolean hasMoreData = true;
    int lastIndex = 1;
    int step = 10;

    public NotificationFragment()
    {
        list = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);

        adapter = new NotifyViewAdapter(getContext(), list);
        binding.recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        adapter.setOnClickItemPositionListener(postion -> {
            NotifycationInfo info = adapter.list.get(postion);
            DetailBillFragment dialog = new DetailBillFragment(info.notification.billID, getContext());
            dialog.show(getParentFragmentManager(), "detailBill");
            new Thread(()->{
                DBControllerNotification db = new DBControllerNotification();
                db.notifyHasReadNoti(info.notification.id);
                db.closeConnection();
            }).start();
            info.notification.hasRead = true;
            adapter.notifyItemChanged(postion);
            MainActivity activity = (MainActivity) getActivity();
            if(activity != null){
                activity.decreaseNumberNotification();
            }
        });

        binding.scrollViewMain.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if(list.size() == 0) return;
            ScrollView scrollView = binding.scrollViewMain;
            ViewGroup viewG = (ViewGroup) scrollView.getChildAt(scrollView.getChildCount() - 1);
            if(viewG == null) return;
            View view = viewG.getChildAt(viewG.getChildCount() - 1);
            if(view == null) return;
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff <= 00 && hasMoreData) {
                showLoading();
                loadMoreData();
            }
        });
        bindingEmpty = null;
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hideLoading();
        reload();
    }

    public void reload()
    {
        int oldLength = list.size();
        if(oldLength > 0)
        {
            list.clear();
            if(adapter != null){
                adapter.notifyItemRangeRemoved(0, oldLength);
            }
        }
        hasMoreData = true;
        lastIndex = 1;
        loadMoreData();
    }

    public void loadMoreData()
    {
        if(!hasMoreData) return;
        hasMoreData = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBControllerNotification db = new DBControllerNotification();
                List<NotifycationInfo> data = db.getNotidication(Authenticator.getCurrentUser().id, lastIndex, lastIndex+step-1);
                db.closeConnection();

                if(getActivity() != null)
                {
                    getActivity().runOnUiThread(() -> {
                        list.addAll(data);
                        if(adapter != null && data.size() > 0) {
                            adapter.notifyItemRangeInserted(list.size() - data.size(), data.size());
                        }
                        layoutData();
                        hideLoading();
                    });
                }

                hasMoreData = data.size() == step;
                lastIndex += step;
            }
        }).start();
    }

    public void showLoading()
    {
        binding.gifLoadingCircle.setEnabled(false);
        binding.gifLoadingCircle.setVisibility(View.VISIBLE);
    }

    public void hideLoading()
    {
        binding.gifLoadingCircle.setEnabled(true);
        binding.gifLoadingCircle.setVisibility(View.GONE);
    }

    public void layoutData()
    {
        if(list.size() == 0)
        {
            if(bindingEmpty == null)
            {
                bindingEmpty = FragmentEmptyNotiBinding.inflate(getLayoutInflater(), binding.contentLayout, false);
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
    }


}
