package exam.nlb2t.epot.RatingProduct;

import android.os.Bundle;
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

import exam.nlb2t.epot.Database.DBControllerRating;
import exam.nlb2t.epot.PersonBill.BillAdapter;
import exam.nlb2t.epot.databinding.FragmentEmptyRatingBinding;
import exam.nlb2t.epot.databinding.FragmentRatingTabNewBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class RatingDialogTab_New extends Fragment {
    FragmentRatingTabNewBinding binding;
    FragmentEmptyRatingBinding emptybinding;
    List<ProductOverviewAdpterItem> list;
    ProductOverviewAdapter adapter;

    boolean hasMoreData = true;
    int lastIndex = 1;
    int step = 20;

    protected Helper.OnSuccessListener onRatingSuccessListener;

    public void setOnRatingSuccessListener(Helper.OnSuccessListener onRatingSuccessListener) {
        this.onRatingSuccessListener = onRatingSuccessListener;
    }

    public RatingDialogTab_New()
    {
        list = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRatingTabNewBinding.inflate(inflater, container, false);

        setupAdapter(binding.recyclerViewMain);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.recyclerViewMain.setLayoutManager(layoutManager);

        binding.scrollViewMain.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if(list.size() == 0) return;
            ScrollView scrollView = binding.scrollViewMain;
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            if(view == null) return;
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff == 0 && hasMoreData) {
                showLoading();
                loadMoreData();
            }
        });

        emptybinding = null;
        return binding.getRoot();
    }

    protected void setupAdapter(RecyclerView recyclerView)
    {
        adapter = new ProductOverviewAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnCLickItemListener(postion -> {
            ProductOverviewAdpterItem info = list.get(postion);
            int userID = Authenticator.getCurrentUser().id;
            RatingProductDialogFragment dialog = new RatingProductDialogFragment(info.productID, userID);
            dialog.setOnSuccessListener(new Helper.OnSuccessListener() {
                @Override
                public void OnSuccess(Object sender) {
                    adapter.list.remove(postion);
                    adapter.notifyItemRemoved(postion);
                    if(onRatingSuccessListener != null) {
                        onRatingSuccessListener.OnSuccess(postion);
                    }
                }
            });
            dialog.show(getChildFragmentManager(), "rating");
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showLoading();
        reloadData();
    }

    public void reloadData()
    {
        int oldLength = list.size();
        if(oldLength > 0)
        {
            list.clear();
            if(adapter != null)
            {
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
        new Thread(() -> {
            List<ProductOverviewAdpterItem> data = getMoreData();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    list.addAll(data);
                    if(adapter != null && data.size() > 0) {
                        adapter.notifyItemRangeInserted(list.size() - data.size(), data.size());
                    }
                    hideLoading();
                    layoutData();
                });
            }

            hasMoreData = data.size() == step;
            lastIndex += data.size();
        }).start();
    }

    public List<ProductOverviewAdpterItem> getMoreData()
    {
        List<ProductOverviewAdpterItem> rs;
        DBControllerRating db = new DBControllerRating();
        rs = db.getUserNotRatingPD(Authenticator.getCurrentUser().id, lastIndex, lastIndex + step -1);
        db.closeConnection();
        return rs;
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

    void layoutData()
    {
        if(binding==null) return;

        ViewGroup contentLayout = (ViewGroup) binding.getRoot();
        if (list.size() == 0) {
            if(emptybinding == null) {
                emptybinding = FragmentEmptyRatingBinding.inflate(getLayoutInflater(), contentLayout, false);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                contentLayout.addView(emptybinding.getRoot(), contentLayout.getChildCount(), params);
            }
            return;
        }

        if(emptybinding != null)
        {
            emptybinding = null;
            contentLayout.removeViewAt(contentLayout.getChildCount() - 1);
        }
    }
}
