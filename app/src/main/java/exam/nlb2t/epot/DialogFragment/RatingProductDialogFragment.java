package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import exam.nlb2t.epot.Database.DBControllerRating;
import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.databinding.FragmentReviewBinding;

public class RatingProductDialogFragment extends BottomSheetDialogFragment {
    protected FragmentReviewBinding binding;
    protected int productID;
    protected int userID;

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

            if(isOK)
            {
                RatingProductDialogFragment.this.dismiss();
            }
            else {
                Snackbar.make(binding.getRoot(), db.getErrorMsg(), BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    public RatingProductDialogFragment(int productID, int userID) {
        this.productID = productID;
        this.userID = userID;
    }
}
