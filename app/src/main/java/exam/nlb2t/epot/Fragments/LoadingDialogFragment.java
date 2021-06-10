package exam.nlb2t.epot.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import exam.nlb2t.epot.databinding.FragmentLoadingBinding;

public class LoadingDialogFragment extends DialogFragment {
    FragmentLoadingBinding binding;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoadingBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

}
