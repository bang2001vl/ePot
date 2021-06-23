package exam.nlb2t.epot.RatingProduct;

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

import exam.nlb2t.epot.Database.DBControllerRating;
import exam.nlb2t.epot.PersonBill.BillAdapter;
import exam.nlb2t.epot.databinding.FragmentRatingTabNewBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;

public class RatingDialogTab_New extends Fragment {
    FragmentRatingTabNewBinding binding;
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

            if (getActivity() != null && adapter != null && data.size() > 0) {
                list.addAll(data);
                getActivity().runOnUiThread(() -> {
                    adapter.notifyItemRangeInserted(list.size() - 1, data.size());
                });
                hasMoreData = data.size() == step;
            }

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRatingTabNewBinding.inflate(inflater, container, false);

        setupAdapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.recyclerViewMain.setLayoutManager(layoutManager);
        return binding.getRoot();
    }

    protected void setupAdapter()
    {
        adapter = new ProductOverviewAdapter(list);
        binding.recyclerViewMain.setAdapter(adapter);

        adapter.setOnBindingLastPositionListener(new BillAdapter.OnBindingLastPositionListener() {
            @Override
            public void onBindingLastPostion(int postion) {
                loadMoreData();
            }
        });
        adapter.setOnCLickItemListener(new BillAdapter.OnBindingLastPositionListener() {
            @Override
            public void onBindingLastPostion(int postion) {
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
            }
        });
    }
}
