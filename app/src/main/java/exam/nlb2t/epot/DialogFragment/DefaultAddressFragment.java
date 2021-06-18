package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Editable;
import android.text.TextWatcher;
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
    private String[] address=new String[4];

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
        Pattern pattern = Pattern.compile(".*\\D.*");
        binding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher matcher = pattern.matcher(binding.phone.getText().toString());
                if( matcher.find()) {
                    binding.phone.setError("Chỉ được nhập số!");
                }
                else
                {
                    if(binding.phone.length() != 9)
                    {
                        binding.phone.setError("Nhập sai định dạng sđt!");
                    }

                }
            }
        });

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
        Pattern pattern = Pattern.compile("[\\p{P}\\p{S}]");
        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher matcher = pattern.matcher(binding.name.getText().toString());
                if (matcher.find()) {
                    binding.name.setError(getResources().getString(R.string.error_not_special_char));
                } else {
                    if (binding.name.getText().toString().length() > 50) {
                        binding.name.setError(getResources().getString(R.string.error_not_50_char));
                    } else {
                        binding.name.setError(null);
                    }
                }
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