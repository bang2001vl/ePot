package exam.nlb2t.epot.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentPaymnetBinding;

public class DetailBillFragment extends DialogFragment {
    public static final String NAMEDIALOG = "DetailBillFragment";
    FragmentPaymnetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymnetBinding.inflate(inflater, container, false);

        //TODO: Add adapter for fragment_payment.xml
        setEventHandler();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setEventHandler() {
        //TODO: Set event here
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_NoTitleBar_Fullscreen);
        return dialog;
    }
}
