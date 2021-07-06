package exam.nlb2t.epot.RatingProduct;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import exam.nlb2t.epot.Database.DBControllerRating;
import exam.nlb2t.epot.Database.Tables.RatingBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.Error_toast;
import exam.nlb2t.epot.Views.Success_toast;

public class RatingProductDialogFragment_Edit extends RatingProductDialogFragment{
    int ratingID;
    RatingBaseDB rating;

    public RatingProductDialogFragment_Edit(int productID, int userID) {
        super(productID, userID);
        rating = new RatingBaseDB();
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(()->{
            DBControllerRating db = new DBControllerRating();
            RatingBaseDB rating = db.getRating(userID, productID);
            db.closeConnection();
            getActivity().runOnUiThread(()->{
                if(db.hasError())
                {
                    Error_toast.show(getContext(), db.getErrorMsg(), true);
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
                        new Thread(()->{
                            DBControllerRating db2 = new DBControllerRating();
                            boolean isOK = db2.updateRating(ratingID, productID, star, comment);
                            db2.closeConnection();
                            getActivity().runOnUiThread(()->{
                                if(isOK)
                                {
                                    Success_toast.show(getContext(), "Đánh giá thành công", true);
                                }
                                else {
                                    Error_toast.show(getContext(), "Có lỗi xảy ra", true);
                                }
                            });
                        }).start();
                        RatingProductDialogFragment_Edit.this.dismiss();
                    });
                }
                else
                {
                    Success_toast.show(getContext(), "Chưa có đánh giá nào trước đây", true);
                    dismiss();
                }
            });
        }).start();
    }
}
