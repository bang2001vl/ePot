package exam.nlb2t.epot.MyShop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.flexbox.FlexboxLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.List;

import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.DialogFragment.PopupMenuDialog;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.PickImageDialogBinding;

public class ImagesDialog extends DialogFragment {
    public List<Bitmap> images;
    int selectedIndex = 0;
    PickImageDialogBinding binding;
    public ImagesDialog(List<Bitmap> images) {
        super();
        this.images = images;
    }

    @Nullable
    @Override
    public Dialog getDialog() {
        Dialog dialog = new Dialog(getContext()){
            @Override
            public void onBackPressed() {
                ImagesDialog.this.dismiss();
            }
        };
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PickImageDialogBinding.inflate(inflater, container, false);
        binding.buttonAddImage.setOnClickListener(v->{
            selectedIndex = -1;
            chooseImage_toAdd();
        });
        binding.btnClose.setOnClickListener(v->{
            this.dismiss();
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!requestPermise())
        {
            binding.buttonAddImage.setEnabled(false);
        }
        View btnAddLayout = binding.buttonAddImage;
        binding.flexboxLayout.removeAllViews();
        FlexboxLayout.LayoutParams layoutParams = createImageView_params();
        for(int i = 0; i< images.size(); i++)
        {
            addImage(images.get(i), i);
        }
        binding.flexboxLayout.addView(btnAddLayout);
    }

    DialogInterface.OnDismissListener onDismissListener = dialog -> {/*Empty*/};
    public void setOnDismissListener(DialogInterface.OnDismissListener listener)
    {
        this.onDismissListener = listener;
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        this.onDismissListener.onDismiss(dialog);
        Log.d("MY_TAG", "on dismiss");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void addImage(Bitmap bitmap, int index)
    {
        FlexboxLayout.LayoutParams layoutParams = createImageView_params();
        final Drawable foreground = getResources().getDrawable(R.drawable.backwithoutline,getContext().getTheme());
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(images.get(index));
        imageView.setTag(index);
        imageView.setOnLongClickListener(longClickImage);
        imageView.setForeground(foreground);
        binding.flexboxLayout.addView(imageView, index, layoutParams);
    }

    void replaceImage(Uri uri, int index)
    {
        if(getContext() == null || getContext().getContentResolver() == null) {
            return;
        }

        InputStream inputStream = null;
        try {
            inputStream = getContext().getContentResolver().openInputStream(uri);
            if(inputStream.available() > DatabaseController.MAX_BYTE_IMAGE)
            {
                Log.e("MY_TAG", "ERROR: Image is too big");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        images.remove(index);
        images.add(index, BitmapFactory.decodeStream(inputStream));
        ImageView imageView = (ImageView) binding.flexboxLayout.getChildAt(index);
        imageView.setImageBitmap(images.get(index));
    }

    void removeImage(int index)
    {
        images.remove(index);
        binding.flexboxLayout.removeViewAt(index);
    }

    void addImage(Uri uri)
    {
        if(getContext() == null || getContext().getContentResolver() == null) {
            return;
        }

        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            inputStream = getContext().getContentResolver().openInputStream(uri);
            if(inputStream.available() > DatabaseController.MAX_BYTE_IMAGE)
            {
                Log.e("MY_TAG", "ERROR: Image is too big");
                return;
            }
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        images.add(bitmap);
    }

    final View.OnLongClickListener longClickImage = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ImageView imageView = (ImageView) v;
            if(imageView != null)
            {
                selectedIndex = (Integer) imageView.getTag();

                /*AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                        .setPositiveButton("Đổi ảnh", (dialog, which) -> {
                            chooseImage_toReplace();
                        })
                        .setNegativeButton("Bỏ chọn ảnh", (dialog, which) -> {
                            removeImage(selectedIndex);
                        })
                        .create();
                alertDialog.show();*/

                String[] options = new String[] {"Đổi ảnh", "Xóa ảnh"};
                PopupMenuDialog dialog = new PopupMenuDialog(options);
                dialog.setOnClickOptionListener(str-> {
                    if (str.equals(options[0])) {
                        chooseImage_toReplace();
                    } else {
                        removeImage(selectedIndex);
                    }
                    dialog.dismiss();
                });
                dialog.show(getChildFragmentManager(), "my_dialog");
            }
            return  true;
        }
    };

    FlexboxLayout.LayoutParams createImageView_params() {
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                getResources().getDimensionPixelOffset(R.dimen.image_preview_width_small)
                , getResources().getDimensionPixelOffset(R.dimen.image_preview_height_small)
        );
        Resources resources = getResources();
        float margin_in_dpi = 5;
        int margin_in_pixel = 25;
        margin_in_pixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                , margin_in_dpi, resources.getDisplayMetrics());
        params.setMargins(margin_in_pixel, margin_in_pixel, margin_in_pixel, margin_in_pixel);
        return params;
    }

    final int REQUEST_PICK_IMAGE_TO_ADD = 0x102;
    void chooseImage_toAdd()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE_TO_ADD);
    }

    final int REQUEST_PICK_IMAGE_TO_REPLACE = 0x103;
    void chooseImage_toReplace()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE_TO_REPLACE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_PICK_IMAGE_TO_ADD && resultCode == Activity.RESULT_OK)
        {
            if(data == null)
            {
                Log.e("ERROR_TRACER", "ERROR: Pick image failed");
                return;
            }
            ClipData clipData = data.getClipData();
            if(clipData != null)
            {
                // Multiple choose
                for(int i = 0; i<clipData.getItemCount(); i++)
                {
                    ClipData.Item item = clipData.getItemAt(i);
                    addImage(item.getUri());
                }
            }
            else {
                // Single choose
                addImage(data.getData());
            }
        }
        if(requestCode == REQUEST_PICK_IMAGE_TO_REPLACE && resultCode == Activity.RESULT_OK)
        {
            if(data == null)
            {
                Log.e("ERROR_TRACER", "ERROR: Pick image failed");
                return;
            }
            if(selectedIndex > -1 && selectedIndex < images.size()) {
                replaceImage(data.getData(), selectedIndex);
            }
        }
    }

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
}
