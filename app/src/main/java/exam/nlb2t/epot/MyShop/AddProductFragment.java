package exam.nlb2t.epot.MyShop;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Category.DBControllerCategory;
import exam.nlb2t.epot.Database.DBControllerProduct;
import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.Database.Tables.ImageProductBaseDB;
import exam.nlb2t.epot.Database.Tables.ProductBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.UpdateProductBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;
public class AddProductFragment extends DialogFragment {
    UpdateProductBinding binding;
    List<Bitmap> images;
    ImagesDialog imagesDialog;
    ArrayAdapter<String> adapterCategory;
    ProductBaseDB productBefore;
    public static final String NAMEDIALOG = "AddProductFragment";


//    Bitmap imagePrimary;
//    Bitmap getImagePrimary()
//    {
//        if(imagePrimary == null)
//        {
//            return BitmapFactory.decodeResource(getResources(), R.drawable.vans);
//        }
//        return imagePrimary;
//    }

    public boolean isOK = false;
    Helper.OnSuccessListener onSubmitOKListener;
    public  void setOnSubmitOKListener(Helper.OnSuccessListener listener){
        onSubmitOKListener = listener;}

    public AddProductFragment()
    {
        //MEANS: create new product
        images = new ArrayList<>();

        imagesDialog = new ImagesDialog(images);
        imagesDialog.setOnDismissListener(dialog -> {
            binding.buttonAddImage.setText(String.format("Ảnh\n(%d)", images.size()));
        });
    }

    public AddProductFragment(@NonNull ProductBaseDB product) {
        //MEANS: Change current product
        this.productBefore = product;

        DBControllerProduct db = new DBControllerProduct();
        images = db.getImages(product.id);
        db.closeConnection();

        imagesDialog = new ImagesDialog(images);
        imagesDialog.setOnDismissListener(dialog -> {
            binding.buttonAddImage.setText(String.format("Ảnh\n(%d)", images.size()));
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UpdateProductBinding.inflate(inflater, container, false);

        List<String> categories = DBControllerCategory.getCategoryNames();
        adapterCategory = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        binding.spinnerCategory.setAdapter(adapterCategory);

        setEventHandler();
        //MEANS: this method must be after method setEventHanlder()
        loadCurrentProduct();

        return binding.getRoot();
    }

    private void loadCurrentProduct() {
        if (productBefore != null) {
            binding.buttonAddImage.setText(String.format("Ảnh\n(%d)", images.size()));
            binding.textView2.setText("Sửa sản phẩm");
            binding.txtInfoAmount.setText("Số lượng hàng trong kho");
            binding.imagePrimary.setImageBitmap(productBefore.getImagePrimary());
            binding.editTextTextProductName.setText(productBefore.name);
            binding.editTextNumberDecimal.setText(Helper.getMoneyString(productBefore.price));
            binding.editTextAmount.setText(String.valueOf(productBefore.amount - productBefore.amountSold));
            binding.editTextTextProductInfo.setText(productBefore.description);
            binding.spinnerCategory.setSelection(productBefore.categoryID);

            binding.imagePrimary.setOnClickListener(v->{
                //TODO: Show message "Not change image" when click
                Toast.makeText(getContext(),"Không thể chỉnh sửa ảnh đại diện",Toast.LENGTH_SHORT).show();
            });
            binding.editTextTextProductName.setOnClickListener(v->{
                //TODO: Show message "Not change name" when click
                Toast.makeText(getContext(),"Không thể chỉnh sửa tên mặt hàng",Toast.LENGTH_SHORT).show();
            });
            binding.editTextNumberDecimal.setOnClickListener(v->{
                //TODO: Show message "Not change price" when click
                Toast.makeText(getContext(),"Không thể chỉnh sửa giá gốc của mặt hàng",Toast.LENGTH_SHORT).show();
            });

            binding.imagePrimary.setFocusable(false);
            binding.editTextTextProductName.setFocusable(false);
            binding.editTextNumberDecimal.setFocusable(false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        Bitmap image = getImagePrimary2();
        int salerID = Authenticator.getCurrentUser().id;
        int categoryID = binding.spinnerCategory.getSelectedItemPosition();

        if(name != null && description != null && amount != -1 && price != -1)
        {
            if (productBefore == null) {
                //TODO: Create new product in DB
                DBControllerProduct databaseController = new DBControllerProduct();
                if(databaseController.insertProduct(salerID,categoryID,name, price,
                        amount, image, description, images)) {
                    this.dismiss();
                    if(onSubmitOKListener != null)
                    {
                        onSubmitOKListener.OnSuccess(AddProductFragment.this);
                    }
                }
                else {
                    Toast.makeText(getContext(), "Lỗi kết nối với đatabasse", Toast.LENGTH_LONG).show();
                }
                databaseController.closeConnection();
            }
            else {
                //TODO: Change current product in DB

                DBControllerProduct db = new DBControllerProduct();
                List<ImageProductBaseDB> deletedImages = new ArrayList<>(db.getOverviewImages(productBefore.id));

                for(ImageProductBaseDB mImage : deletedImages) {
                    if (images.removeIf(i -> mImage.value == i)) {
                        Log.i("Deleted duplicate image", "The ID of image is deleted is: " + mImage.id);
                        ImageProductBaseDB i = mImage;
                        images.remove(mImage);

                        i.recycle();
                    }
                }

                //MEANS: update UI
                updateProductUI(categoryID, description, amount + productBefore.amountSold);

                int[] deletedImagesID = new int[deletedImages.size()];
                for (int i = 0; i < deletedImages.size(); i++) {
                    deletedImagesID[i] = deletedImages.get(i).id;
                }

                //MEANS: update DB
                if (db.updateProduct(productBefore, images, deletedImagesID)) {
                    this.dismiss();
                    if(onSubmitOKListener != null)
                    {
                        onSubmitOKListener.OnSuccess(AddProductFragment.this);
                    }
                }
                else {
                    Toast.makeText(getContext(), "Lỗi kết nối với đatabasse", Toast.LENGTH_LONG).show();
                }
                db.closeConnection();
            }
        }
    }

    private void updateProductUI(int categoryID, String description, int newTotalamount) {
        productBefore.categoryID = categoryID;
        productBefore.description = description;
        productBefore.amount = newTotalamount;
    }

    void chooseImage() {
        imagesDialog.show(getChildFragmentManager(), "choose image");
    }


    int getPrice()
    {
        EditText editText = binding.editTextNumberDecimal;
        int rs = -1;
        if (productBefore != null) return productBefore.priceOrigin;
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
        EditText editText = binding.editTextTextProductInfo;
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

    Bitmap getImagePrimary2() {
        Bitmap image = ((BitmapDrawable)binding.imagePrimary.getDrawable()).getBitmap();
        return image;
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
                if(inputStream.available() > DatabaseController.MAX_BYTE_IMAGE)
                {
                    Log.e("MY_TAG", "ERROR: Image is too big");
                    return;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if(bitmap != null) {
                binding.imagePrimary.setImageBitmap(bitmap);
            }
        }
    }

    public void recycle()
    {
        for(int i = images.size() - 1; i > -1 ; i--)
        {
            images.get(i).recycle();
            images.remove(i);
        }

        binding = null;
    }
}
