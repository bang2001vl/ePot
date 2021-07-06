package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import exam.nlb2t.epot.databinding.DialogCartCompletePaymentBinding;
import exam.nlb2t.epot.databinding.DialogGifAndTextBinding;
import exam.nlb2t.epot.singleton.Helper;
import pl.droidsonroids.gif.GifDrawable;

public class LoginLoadingDialog extends DialogFragment {
    Helper.OnSuccessListener onClickSubmitListener;
    public void setOnClickSubmitListener(Helper.OnSuccessListener  listener)
    {
        this.onClickSubmitListener = listener;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogGifAndTextBinding binding = DialogGifAndTextBinding.inflate(inflater, container, false);

        GifDrawable gif = ((GifDrawable)binding.gifImg.getDrawable());
        gif.setLoopCount(1);
        gif.setSpeed(1.2f);
        return binding.getRoot();
    }
}
