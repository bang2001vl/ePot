package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentDefaultAddressBinding;
import exam.nlb2t.epot.databinding.FragmentSettingAccountBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class DefaultAddressFragment extends DialogFragment {

    FragmentDefaultAddressBinding binding;
    private UserBaseDB currentuser= Authenticator.getCurrentUser();
    private String[] address=new String[5];

    public DefaultAddressFragment() {
        // Required empty public constructor
    }
    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDefaultAddressBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       openView();

    }
    private void setEventHandler() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentuser.setAddress(binding.name.getText().toString(),binding.phone.getText().toString(),binding.DetailAddress.getText().toString(),"");
                dismiss();
            }
        });
    }
    private void openView()
    {
        if (currentuser.address==null)
        {
            binding.name.setText(currentuser.fullName);
            binding.phone.setText(currentuser.phoneNumber);
        }
        else
        {
            address=currentuser.getAddress();
            binding.name.setText(address[0]);
            binding.phone.setText(address[1]);
            binding.DetailAddress.setText(address[2]);
        }
    }
}