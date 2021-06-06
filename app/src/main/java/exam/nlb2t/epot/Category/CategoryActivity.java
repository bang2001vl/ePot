package exam.nlb2t.epot.Category;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.List;

import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.databinding.ActivityCategoryBinding;

public class CategoryActivity extends AppCompatActivity {
    ActivityCategoryBinding binding;
    Bitmap selectedImage;
    final int REQUEST_PICK_IMAGE_TO_ADD = 0x112;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());

        binding.btnSubmit.setOnClickListener(v->{
            DBControllerCategory dbControllerCategory = new DBControllerCategory();
            if(dbControllerCategory.createCategory(binding.txtName.getText().toString(), selectedImage))
            {
                Toast.makeText(this, "Thanh cong", Toast.LENGTH_LONG).show();
            }
            dbControllerCategory.closeConnection();
        });

        binding.imageView.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PICK_IMAGE_TO_ADD);
        });

        setContentView(binding.getRoot());

        List<Pair<String, Bitmap>> list = DBControllerCategory.getCategories();

        if(list != null && list.size() > 0)
        {
            binding.txtName.setText(list.get(0).first);
            binding.imageView.setImageBitmap(list.get(0).second);
        }
        else {Log.d("MY_TAG", "FAILED: Cannot load category");}
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkPermission())
        {
            binding.imageView.setEnabled(false);
        }
    }

    final int REQUEST_PERMISSION = 0x111;
    boolean checkPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            return false;
        }
        else {return true;}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if(binding != null){binding.imageView.setEnabled(true);}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_PICK_IMAGE_TO_ADD&&resultCode==Activity.RESULT_OK)
        {
            Uri uri = data.getData();

                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                    if(inputStream.available() > DatabaseController.MAX_BYTE_IMAGE)
                    {
                        Log.e("MY_TAG", "ERROR: Image is too big");
                        return;
                    }
                    if(selectedImage != null){selectedImage.recycle();}
                    selectedImage = BitmapFactory.decodeStream(inputStream);
                    binding.imageView.setImageBitmap(selectedImage);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

        }
    }
}
