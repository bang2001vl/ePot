package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentSettingAccountBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class SettingAccountFragment extends DialogFragment {

    FragmentSettingAccountBinding binding;
    private UserBaseDB currentuser= Authenticator.getCurrentUser();

    public SettingAccountFragment() {
        // Required empty public constructor
    }
    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingAccountBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvUsername.setText(currentuser.username);
        binding.tvFullname.setText(currentuser.fullName);
        binding.tvBirthday.setText(getBirthday());
//        binding.tvSex.setSelection(currentuser.gender);

    }
    private void setEventHandler() {
            binding.btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   dismiss();
                }
            });
            binding.btnChangeprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setVisible();

                }
            });
    }
    private String getBirthday()
    {
        return new SimpleDateFormat("MM-dd-yyyy").format(currentuser.birthday);
    }

    private void setVisible()
    {
        if (binding.btnChangeprofile.getVisibility()==View.VISIBLE){
            binding.btnChangeprofile.setVisibility(View.INVISIBLE);
            binding.btnSaveprofile.setVisibility(View.VISIBLE);
            binding.tvFullname.setEnabled(false);
            binding.tvSex.setEnabled(false);
            binding.tvBirthday.setEnabled(false);
        }
        else {
            binding.btnChangeprofile.setVisibility(View.VISIBLE);
            binding.btnSaveprofile.setVisibility(View.INVISIBLE);
            binding.tvFullname.setEnabled(true);
            binding.tvSex.setEnabled(true);
            binding.tvBirthday.setEnabled(true);
        }
    }
}