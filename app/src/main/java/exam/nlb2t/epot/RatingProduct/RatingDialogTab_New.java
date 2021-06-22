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
import exam.nlb2t.epot.DialogFragment.RatingProductDialogFragment;
import exam.nlb2t.epot.PersonBill.BillAdapter;
import exam.nlb2t.epot.databinding.FragmentRatingTabNewBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class RatingDialogTab_New extends Fragment {
    FragmentRatingTabNewBinding binding;
    List<ProductOverviewAdpterItem> list;
    ProductOverviewAdapter adapter;

    boolean hasMoreData = true;
    int lastIndex = 1;
    int step = 20;

    public RatingDialogTab_New()
    {
        list = new ArrayList<>();
        reloadData();
    }

    public void reloadData()
    {
        lastIndex = 1;
        int oldLength = list.size();
        if(oldLength > 0)
        {
            list.clear();
            if(adapter != null)
            {
                adapter.notifyItemRangeRemoved(0, oldLength);
            }
        }
        loadMoreData();
    }

    public void loadMoreData()
    {
        if(!hasMoreData) return;
        hasMoreData = false;
        new Thread(() -> {
            List<ProductOverviewAdpterItem> data = getMoreData();
            list.addAll(data);

            if (getActivity() != null && adapter != null && data.size() > 0) {
                getActivity().runOnUiThread(() -> {
                    adapter.notifyItemRangeInserted(list.size() - data.size() - 1, data.size());
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRatingTabNewBinding.inflate(inflater, container, false);
        adapter = new ProductOverviewAdapter(list);
        binding.recyclerViewMain.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.recyclerViewMain.setLayoutManager(layoutManager);
        setEventHandler();
        return binding.getRoot();
    }

    protected void setEventHandler()
    {
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
                RatingProductDialogFragment dialog = new RatingProductDialogFragment(userID, info.productID);
                dialog.show(getChildFragmentManager(), "rating");
            }
        });
    }
}
