package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import exam.nlb2t.epot.Database.DBControllerRating;
import exam.nlb2t.epot.Database.Tables.RatingBaseDB;

public class RatingProductDialogFragment_Edit extends RatingProductDialogFragment{
    int ratingID;
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DBControllerRating db = new DBControllerRating();
        RatingBaseDB rating = db.getRating(productID, userID);
        db.closeConnection();

        if(db.hasError())
        {
            Snackbar.make(binding.getRoot(), db.getErrorMsg(), BaseTransientBottomBar.LENGTH_LONG).show();
            dismiss();
        }

        if(rating != null)
        {
            ratingID = rating.id;
            binding.startMyRating.setProgress(rating.star);
            binding.reviewMyRating.setText(rating.comment);
            binding.btnRating.setText("Lưu thay đổi");

            binding.btnRating.setOnClickListener(v->{
                int star = binding.startMyRating.getProgress();
                String comment = binding.reviewMyRating.getText().toString();
                DBControllerRating db2 = new DBControllerRating();
                boolean isOK = db2.updateRating(ratingID, userID, star, comment);
                db2.closeConnection();

                if(isOK)
                {
                    RatingProductDialogFragment_Edit.this.dismiss();
                }
                else {
                    Snackbar.make(binding.getRoot(), db.getErrorMsg(), BaseTransientBottomBar.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Snackbar.make(binding.getRoot(), "Chưa có đánh giá nào trước đây", BaseTransientBottomBar.LENGTH_LONG).show();
            dismiss();
        }
    }

    public RatingProductDialogFragment_Edit(int productID, int userID) {
        super(productID, userID);
    }
}
