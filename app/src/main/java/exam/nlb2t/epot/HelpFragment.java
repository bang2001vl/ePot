package exam.nlb2t.epot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.databinding.FragmentFavoriteProdBinding;
import exam.nlb2t.epot.databinding.FragmentHelpBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class HelpFragment extends DialogFragment {

    FragmentHelpBinding binding;
    public HelpFragment() {
        // Required empty public constructor
    }
    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHelpBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private void setEventHandler() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}