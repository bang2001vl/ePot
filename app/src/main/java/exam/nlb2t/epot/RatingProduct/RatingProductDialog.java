package exam.nlb2t.epot.RatingProduct;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import exam.nlb2t.epot.databinding.FragmentRatingBinding;

public class RatingProductDialog extends DialogFragment {
    FragmentRatingBinding binding;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen){
            @Override
            public void onBackPressed() {
                RatingProductDialog.this.dismiss();
            }
        };
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRatingBinding.inflate(inflater, container, false);
        RatingDialogTabAdapter adapter = new RatingDialogTabAdapter(
                getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                new Fragment[]{
                        new RatingDialogTab_New(),
                        new RatingDialogTab_New()
                },
                new String[]{
                        "Chưa đánh giá",
                        "Đã đánh giá"
                }
        );
        binding.viewPaperMain.setAdapter(adapter);
        binding.tabLayoutMain.setupWithViewPager(binding.viewPaperMain);

        binding.btnClose.setOnClickListener(v->RatingProductDialog.this.dismiss());
        return binding.getRoot();
    }
}
