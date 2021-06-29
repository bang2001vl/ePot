package exam.nlb2t.epot.ProductDetail.Rating;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import exam.nlb2t.epot.Database.DBControllerRating;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProductRatingDialogLayoutBinding.inflate(inflater, container, false);

        adapter = new RatingViewRecycleAdapter(list);

        binding.scrollViewMain.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if(list.size() == 0) return;
            ScrollView scrollView = binding.scrollViewMain;
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            if(view == null) return;
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff <= 00 && hasMoreData) {
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
        int oldSize = list.size();
        if(oldSize > 0){
            list.clear();
            if(adapter != null){adapter.notifyItemRangeRemoved(0, oldSize);}
        }

        lastIndex = 1;
        loadMoreData();
    }

    private void loadMoreData() {
        if(!hasMoreData) return;
        hasMoreData = false;
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
                    lastIndex += step;
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
        binding.txtRatingCount1StarProductDetail.setText(star1+"");
        binding.txtRatingCount2StarProductDetail.setText(star2+"");
        binding.txtRatingCount3StarProductDetail.setText(star3+"");
        binding.txtRatingCount4StarProductDetail.setText(star4+"");
        binding.txtRatingCount5StarProductDetail.setText(star5+"");
        int total = star1+star2+star3+star4+star5;
        binding.txtRatingCountProductDetail.setText(String.format(Locale.getDefault(), "%d đánh giá", total));

        if(total == 0)
        {
            binding.txtRatingAverageProductDetail.setText("0");
            binding.seekbarRatingCount1StarProductDetail.setProgress(0);
            binding.seekbarRatingCount2StarProductDetail.setProgress(0);
            binding.seekbarRatingCount3StarProductDetail.setProgress(0);
            binding.seekbarRatingCount4StarProductDetail.setProgress(0);
            binding.seekbarRatingCount5StarProductDetail.setProgress(0);
            return;
        }

        float average_star = (star1+star2*2+star3*3+star4*4+star5*5) / (1f * total);
        binding.txtRatingAverageProductDetail.setText(String.format(Locale.getDefault(),"%.1f", average_star));

        binding.seekbarRatingCount1StarProductDetail.setProgress(star1 * 100 / total, true);
        binding.seekbarRatingCount2StarProductDetail.setProgress(star2 * 100 / total, true);
        binding.seekbarRatingCount3StarProductDetail.setProgress(star3 * 100 / total, true);
        binding.seekbarRatingCount4StarProductDetail.setProgress(star4 * 100 / total, true);
        binding.seekbarRatingCount5StarProductDetail.setProgress(star5 * 100 / total, true);
    }
}
