package exam.nlb2t.epot.ProductDetail.Rating;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import exam.nlb2t.epot.Database.DBControllerRating;
import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.databinding.ProductRatingDialogLayoutBinding;

public class ProductRatingDialog extends DialogFragment {
    ProductRatingDialogLayoutBinding binding;
    RatingViewRecycleAdapter adapter;

    List<RatingInfo> list;
    int lastIndex = 1;
    int step = 5;
    boolean hasMoreData;

    int productID;
    int[] star;

    public ProductRatingDialog(int productID)
    {
        list = new ArrayList<>();
    }

    public ProductRatingDialog(int productID, int[] star)
    {
        list = new ArrayList<>();
        this.star = star;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen){
            @Override
            public void onBackPressed() {
                ProductRatingDialog.this.dismiss();
            }
        };
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProductRatingDialogLayoutBinding.inflate(inflater, container, false);

        adapter = new RatingViewRecycleAdapter(list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);

        binding.scrollViewMain.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if(list.size() == 0) return;
            ScrollView scrollView = binding.scrollViewMain;
            ViewGroup viewG = (ViewGroup) scrollView.getChildAt(scrollView.getChildCount() - 1);
            View view = viewG.getChildAt(viewG.getChildCount() - 1);
            if(view == null) return;
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff == 0 && hasMoreData) {
                showLoading();
                loadMoreData();
            }
        });

        if(star != null)
        {
            setStar(star[0], star[1], star[2], star[3], star[4]);
        }
        else {
            setStar(0,0,0,0,0);
            new Thread(() -> {
                DBControllerRating db = new DBControllerRating();
                star = db.getRatingStar(productID);
                db.closeConnection();
                getActivity().runOnUiThread(() -> setStar(star[0], star[1], star[2], star[3], star[4]));
            }).start();
        }

        hideLoading();
        reload();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void reload()
    {
        int oldSize = list.size();
        if(oldSize > 0){
            list.clear();
            if(adapter != null){adapter.notifyItemRangeRemoved(0, oldSize);}
        }

        hasMoreData = true;
        lastIndex = 1;
        loadMoreData();
    }

    private void loadMoreData() {
        if(!hasMoreData) return;
        hasMoreData = false;
        // DEBUG
        /*List<RatingInfo> data2 = new ArrayList<>();
        for(int i = 0; i< step ; i++)
        {
            RatingBaseDB ratingBaseDB = new RatingBaseDB(1, productID, 9, 5, "Default comment", new Date(System.currentTimeMillis()));
            UserBaseDB userBaseDB = new UserBaseDB();
            userBaseDB.id = 9;
            userBaseDB.username = "username";
            userBaseDB.fullName = "User's full-name";
            userBaseDB.avatarID = 3;
            data2.add(new RatingInfo(ratingBaseDB, userBaseDB));
        }
        list.addAll(data2);
        adapter.notifyItemRangeInserted(list.size() - data2.size(), data2.size());
        lastIndex += data2.size();
        hasMoreData = data2.size() == step;
        hideLoading();
        return;*/
        new Thread(() -> {
            DBControllerRating db = new DBControllerRating();
            List<RatingInfo> data = db.getRatingInfo_ByProduct(productID, lastIndex, lastIndex + step -1);
            db.closeConnection();

            if(getActivity() != null){
                getActivity().runOnUiThread(() -> {
                    if(data.size() > 0){
                        list.addAll(data);
                        adapter.notifyItemRangeInserted(list.size() - data.size(), data.size());
                    }
                    lastIndex += data.size();
                    hasMoreData = data.size() == step;
                    hideLoading();
                });
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

    public void setStar(int star1, int star2, int star3, int star4, int star5)
    {
        binding.ratingChart.setStar(star1, star2, star3, star4, star5);
    }
}
