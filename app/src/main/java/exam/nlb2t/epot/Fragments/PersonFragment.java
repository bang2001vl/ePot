package exam.nlb2t.epot.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import exam.nlb2t.epot.DialogFragment.DefaultAddressFragment;
import exam.nlb2t.epot.DialogFragment.FavoriteProdFragment;
import exam.nlb2t.epot.HelpFragment;
import exam.nlb2t.epot.LoginScreen;
import exam.nlb2t.epot.MainActivity;
import exam.nlb2t.epot.OrderFragment;
import exam.nlb2t.epot.DialogFragment.SettingAccountFragment;
import exam.nlb2t.epot.databinding.FragmentProfileBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;

public class PersonFragment<DialogLayoutBinding> extends Fragment {
    FragmentProfileBinding binding;
    private UserBaseDB currentuser=Authenticator.getCurrentUser();
    private String[] mAddress=new String[2];
    MainActivity mainActivity;
    public PersonFragment(MainActivity ma)
    {
        mainActivity=ma;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvName.setText(currentuser.fullName);
        binding.tvUsername.setText(currentuser.username);
        binding.avtProfile.setImageBitmap(currentuser.getAvatar(currentuser.avatarID));
        getAddress();
        binding.tvCityAddress.setText(mAddress[0]);
        binding.tvStreetAddress.setText(mAddress[1]);

    }

    void setEventHandler()
    {
        binding.btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ShowDialog(new OrderFragment(currentuser.id));
            }
        });
        binding.btnDefaultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(new DefaultAddressFragment());
            }
        });
        binding.btnAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(new SettingAccountFragment());
            }
        });
        binding.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(new FavoriteProdFragment());
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlertDialog();
            }
        });
        binding.btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(new HelpFragment());
            }
        });
    }
    private void ShowDialog(DialogFragment dialogFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialogFragment.show(ft, "dialog");
    }
    private void getAddress()
    {
        mAddress[0]="";
        mAddress[1]="";
       if (currentuser.address!=null)
       {
           mAddress[0]=currentuser.getAddress()[2];
           mAddress[1]=currentuser.getAddress()[3];
       }
    }
    private void openAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Đăng xuất khỏi tài khoản này")
                .setTitle("Đăng xuất");



        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent newIntent = new Intent(mainActivity, LoginScreen.class);
                startActivity(newIntent);
                mainActivity.finish();
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert =builder.create();
        alert.show();
        }





}
