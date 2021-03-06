package exam.nlb2t.epot.RatingProduct;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import exam.nlb2t.epot.Database.DBControllerRating;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.Error_toast;
import exam.nlb2t.epot.Views.Success_toast;
import exam.nlb2t.epot.databinding.FragmentReviewBinding;
import exam.nlb2t.epot.singleton.Helper;

public class RatingProductDialogFragment extends BottomSheetDialogFragment {
    protected FragmentReviewBinding binding;
    protected int productID;
    protected int userID;

    private Helper.OnSuccessListener onSuccessListener;

    public void setOnSuccessListener(Helper.OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public RatingProductDialogFragment(int productID, int userID) {
        this.productID = productID;
        this.userID = userID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnRating.setOnClickListener(v->{
            int star = binding.startMyRating.getProgress();
            String comment = binding.reviewMyRating.getText().toString();

                DBControllerRating db = new DBControllerRating();
                boolean isOK = db.insertRating(productID, userID, star, comment);
                db.closeConnection();
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (isOK) {
                            Success_toast.show(getContext(), "????nh gi?? th??nh c??ng", true);
                            if (onSuccessListener != null) {
                                onSuccessListener.OnSuccess(productID);
                            }
                        } else {
                            Error_toast.show(getContext(), "C?? l???i x???y ra", true);
                        }
                        RatingProductDialogFragment.this.dismiss();
                    });
                }
        });

        if(getDialog() != null) {
            getDialog().setOnShowListener(dialog -> {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
                if(bottomSheet == null) return;
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) bottomSheet.getParent();
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setPeekHeight(bottomSheet.getHeight());
                coordinatorLayout.getParent().requestLayout();
            });
        }
    }
}
