package exam.nlb2t.epot.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import exam.nlb2t.epot.DialogFragment.DefaultAddressFragment;
import exam.nlb2t.epot.DialogFragment.FavoriteProdFragment;
import exam.nlb2t.epot.DialogFragment.OrderFragment;
import exam.nlb2t.epot.DialogFragment.SettingAccountFragment;
import exam.nlb2t.epot.databinding.FragmentProfileBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;

public class PersonFragment<DialogLayoutBinding> extends Fragment {
    FragmentProfileBinding binding;
    private UserBaseDB currentuser=Authenticator.getCurrentUser();
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
        binding.tvCityAddress.setText(currentuser.address);

    }

    void setEventHandler()
    {
        binding.btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ShowDialog(new OrderFragment());
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
    }
    private void ShowDialog(DialogFragment dialogFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialogFragment.show(ft, "dialog");
    }



}
