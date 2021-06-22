package exam.nlb2t.epot.DialogFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Pattern;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentChangeAvtBinding;
import exam.nlb2t.epot.singleton.Authenticator;

import static android.app.Activity.RESULT_OK;

public class ChangeAvtFragment extends DialogFragment {

    FragmentChangeAvtBinding binding;

    private UserBaseDB currentuser= Authenticator.getCurrentUser();
    DBControllerUser dbControllerUser=new DBControllerUser();

    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;

    Bitmap bitmap=currentuser.getAvatar();
    public ChangeAvtFragment() {
    }
    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChangeAvtBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageAvt.setImageBitmap(bitmap);


    }
    private void setEventHandler() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permission={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission,PERMISSION_CODE);
                    }
                    else{
                        pickImage();
                    }

                    }
                else {
                    pickImage();
                }
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbControllerUser.updateUser_Avatar(currentuser.id,currentuser.avatarID,bitmap);
                dismiss();
            }
        });
    }


    private void pickImage(){

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==RESULT_OK && requestCode==IMAGE_PICK_CODE){
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                binding.imageAvt.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImage();
                }
                else {
                    Toast.makeText(getContext(), "Lỗi!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}