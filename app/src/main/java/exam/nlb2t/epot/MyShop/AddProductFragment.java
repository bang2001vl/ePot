package exam.nlb2t.epot.MyShop;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.UpdateProductBinding;

public class AddProductFragment extends DialogFragment {
    UpdateProductBinding binding;
    List<Bitmap> images;
    ImagesDialog imagesDialog;
    public boolean isOK = false;

    public AddProductFragment()
    {
        images = new ArrayList<>();

        imagesDialog = new ImagesDialog(images);
        imagesDialog.setOnDismissListener(dialog -> {
            binding.buttonAddImage.setText(String.format("Ảnh\n(%d)", images.size()));
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UpdateProductBinding.inflate(inflater, container, false);
        setEventHandler();
        binding.textView2.setText("Thêm sản phẩm");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : Write code here <Get data from database and set to view>
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!requestPermise())
        {
            binding.imagePrimary.setEnabled(false);
            binding.buttonAddImage.setEnabled(false);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        return dialog;
    }

    void setEventHandler() {
        binding.buttonSave.setOnClickListener(v-> this.saveToDB());
        binding.buttonAddImage.setOnClickListener(v-> this.chooseImage());
        binding.btnClose.setOnClickListener(v-> this.dismiss());
        binding.imagePrimary.setOnClickListener(v-> this.choosePrimaryImage());
    }
    void saveToDB()
    {
        String name = getName();
        String description = getDescription();
        int amount = getAmount();
        int price = getPrice();
        if(name != null && description != null && amount != -1 && price != -1)
        {
            DatabaseController databaseController = new DatabaseController();
            if(databaseController.insertProduct(0,0,name, price,
                    amount, getImagePrimary(), description, images)) {
                this.dismiss();
                isOK = true;
            }
            else {
                Toast.makeText(getContext(), "Lỗi kết nối với đatabasse", Toast.LENGTH_LONG).show();
            }
        }
    }

    void chooseImage() {
        imagesDialog.show(getChildFragmentManager(), "choose image");
    }

    Bitmap imagePrimary;
    Bitmap getImagePrimary()
    {
        if(imagePrimary == null)
        {
            return BitmapFactory.decodeResource(getResources(), R.drawable.vans);
        }
        return imagePrimary;
    }

    int getPrice()
    {
        EditText editText = binding.editTextNumberDecimal;
        int rs = -1;
        try {
            rs = Integer.parseInt(editText.getText().toString());
            if(rs < 0 || ((rs%1000) != 0)){
                throw new NumberFormatException();}
            else {return rs;}
        }
        catch (NumberFormatException e)
        {
            editText.setError("Giá tiền không hợp lệ");
            return rs;
        }
    }

    int getAmount()
    {
        EditText editText = binding.editTextAmount;
        int rs = -1;
        try {
            rs = Integer.parseInt(editText.getText().toString());
            if(rs < 0){
                throw new NumberFormatException();}
            else {return rs;}
        }
        catch (NumberFormatException e)
        {
            editText.setError("Số lượng không hợp lệ");
            return rs;
        }
    }

    String getName()
    {
        EditText editText = binding.editTextTextProductName;
        String rs = editText.getText().toString();
        if(rs.length() > 0)
        {
            final String filter = ",./;[]-=`~!@#$%^&*()_+}|{:?>-*/+'\"\\\t\n";
            for(int i = 1; i<filter.length(); i++)
            {
                if(rs.contains(filter.substring(i-1,i)))
                {
                    editText.setError("Tên sản phẩm không được chứa kí tự đặc biệt");
                    return null;
                }
            }
            return rs;
        }
        else
        {
            editText.setError("Đây là thông tin bắt buộc");
            return null;
        }
    }

    String getDescription()
    {
        EditText editText = binding.editTextTextProductName;
        String rs = editText.getText().toString();
        if(rs.length() > 0)
        {
            final String filter = "";
            for(int i = 1; i<filter.length(); i++)
            {
                if(rs.contains(filter.substring(i-1,i)))
                {
                    editText.setError("Không được chứa kí tự đặc biệt");
                    return null;
                }
            }
            return rs;
        }
        else
        {
            editText.setError("Đây là thông tin bắt buộc");
            return null;
        }
    }

    final int REQUEST_CHOOSE_IMAGE = 0x102;
    final int REQUEST_PERMISSION = 0x101;

    boolean requestPermise()
    {
        Activity activity = getActivity();
        if(activity != null)
        {
            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            }
            else {return true;}
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                binding.buttonAddImage.setEnabled(true);
            }
        }
    }

    void choosePrimaryImage()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK)
        {
            if(getContext() == null || getContext().getContentResolver() == null) {
                return;
            }
            InputStream inputStream;
            try {
                inputStream = getContext().getContentResolver().openInputStream(data.getData());
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if(bitmap != null) {
                if(imagePrimary != null){imagePrimary.recycle();}
                imagePrimary = bitmap;
                binding.imagePrimary.setImageBitmap(bitmap);
            }
        }
    }
}
