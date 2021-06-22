package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import exam.nlb2t.epot.ProductDetail.ChooseItemDetailBottomSheet;
import exam.nlb2t.epot.databinding.FragmentCartCompletePaymentBinding;
import exam.nlb2t.epot.singleton.Helper;
import pl.droidsonroids.gif.GifDrawable;

public class PaymentSucessDialog extends DialogFragment {
    Helper.OnSuccessListener onClickSubmitListener;
    public void setOnClickSubmitListener(Helper.OnSuccessListener  listener)
    {
        this.onClickSubmitListener = listener;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCartCompletePaymentBinding binding = FragmentCartCompletePaymentBinding.inflate(inflater, container, false);
        binding.btnCompletetePayment.setOnClickListener(v->{
            if(onClickSubmitListener != null)
            {
                onClickSubmitListener.OnSuccess(null);
            }
            PaymentSucessDialog.this.dismiss();
        });
        GifDrawable gif = ((GifDrawable)binding.gifCompletePayment.getDrawable());
        gif.setLoopCount(1);
        gif.setSpeed(1.2f);
        return binding.getRoot();
    }
}
