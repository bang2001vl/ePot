package exam.nlb2t.epot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Objects;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.Views.forgotpassword;
import exam.nlb2t.epot.databinding.FragmentChangePasswordBinding;
import exam.nlb2t.epot.databinding.FragmentSettingAccountBinding;
import exam.nlb2t.epot.singleton.Authenticator;


public class ChangePasswordFragment extends BottomSheetDialogFragment {

    FragmentChangePasswordBinding binding;
    private UserBaseDB currentuser= Authenticator.getCurrentUser();
    DBControllerUser dbControllerUser=new DBControllerUser();
    Context context;

    public ChangePasswordFragment() {
    }


    @Override
    public View onCreateView(@Nullable  LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private void setEventHandler(){
        binding.newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ( binding.newPass.length() < 6 ||  binding.newPass.length() > 50)
                {
                    binding.newPass.setError(getResources().getString(R.string.error_length_6_to_50));
                }
            }
        });
        binding.rePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Objects.requireNonNull(binding.newPass.getText()).toString().equals(s.toString()))
                {
                    binding.rePass.setError(getResources().getString(R.string.error_define_pass));
                }
                else
                {
                    binding.rePass.setError(null);
                }
            }
        });

       binding.btnSavePass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (CheckErrorUserInfo() == -1) {
                   Toast.makeText(getActivity(), getResources().getString(R.string.error_not_enough_info), Toast.LENGTH_SHORT).show();
               } else {
                   if (CheckErrorUserInfo() == 0) {
                       Toast.makeText(getActivity(), getResources().getString(R.string.error_incorrect_info), Toast.LENGTH_SHORT).show();
                   } else {
                         if (checkPass())
                         {
                             dbControllerUser.UpdatePassword(currentuser.phoneNumber,binding.newPass.getText().toString());
                             Toast.makeText(getContext(), getResources().getString(R.string.ChangePassword_Successful), Toast.LENGTH_SHORT).show();
                             dismiss();
                         }
                         else Toast.makeText(getContext(), getResources().getString(R.string.NotcorrectPassword), Toast.LENGTH_SHORT).show();
                   }
               }
           }
       });
       binding.btnForgotPass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), forgotpassword.class);
               startActivity(intent);
           }
       });
    }
    private int CheckErrorUserInfo()
    {
        if (binding.newPass.getText().toString().equals("")|| (binding.etOldPass.getText().equals(""))|| (binding.rePass.getText().toString().equals("")))
        {
            return -1;
        }
        if (binding.rePass.getError() != null || binding.newPass.getError() != null)
        {
            return 0;
        }
        return 1;
    }
    private boolean checkPass()
    {
        if (dbControllerUser.findUserID(currentuser.username,binding.etOldPass.getText().toString())==-1)
        {
            return false;
        }
        return true;

    }
}