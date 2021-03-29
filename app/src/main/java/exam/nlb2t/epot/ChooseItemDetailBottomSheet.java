package exam.nlb2t.epot;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thunderstudio.mylib.Views.ChooseAmountLayout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import exam.nlb2t.epot.ClassInformation.BuyInfo;

public class ChooseItemDetailBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG_NAME = "PRODUCT_NAME";
    public static final String TAG_MAX = "MAX";
    public static final String TAG_PRICE = "PRICE";

    String productName = "DEFAULT_NAME";
    int productMaxAmount = 100;
    int productSinglePrice = 1000;

    public Bitmap bitmap = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_item_detail, container, false);

        ChooseAmountLayout chooseAmountLayout = view.findViewById(R.id.amount_picker);

        TextView txtName = view.findViewById(R.id.txtName_item);
        TextView txtMax = view.findViewById(R.id.txtAmount_Max);
        TextView txtPrice = view.findViewById(R.id.txtPrice);
        Button btnSubmit = view.findViewById(R.id.btn_submit);

        if(savedInstanceState != null) {
            this.productMaxAmount = savedInstanceState.getInt(TAG_MAX);
            this.productName = savedInstanceState.getString(TAG_NAME);
            this.productSinglePrice = savedInstanceState.getInt(TAG_PRICE);
        }

        txtName.setText(this.productName);
        txtMax.setText(getString(R.string.max_formatted, this.productMaxAmount));
        txtPrice.setText(getString(R.string.price_formatted, this.productSinglePrice + "VND"));

        chooseAmountLayout.controller.min = 1;
        chooseAmountLayout.controller.max = productMaxAmount;
        chooseAmountLayout.controller.setNumber(1);

        if(this.bitmap != null)
        {
            ImageView imageView = view.findViewById(R.id.image_item);
            imageView.setImageBitmap(bitmap);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                Log.d("MY_TAG", "Buy : " + (int)chooseAmountLayout.controller.getNumber());
                Toast.makeText(ChooseItemDetailBottomSheet.this.getContext(), "Buy : " + (int)chooseAmountLayout.controller.getNumber(), Toast.LENGTH_LONG);
            }
        });
        return view;
    }

    public static ChooseItemDetailBottomSheet newInstance(@NonNull String productName, int maxAmount, int singlePrice, @Nullable Bitmap bitmap)
    {
        ChooseItemDetailBottomSheet bottomSheet = new ChooseItemDetailBottomSheet();
        Bundle bundle = new Bundle();

        bundle.putInt(TAG_MAX, maxAmount);
        bundle.putString(TAG_NAME, productName);
        bundle.putInt(TAG_PRICE, singlePrice);

        if(bitmap != null)
        {
            bottomSheet.bitmap = bitmap;
        }

        bottomSheet.setArguments(bundle);
        return bottomSheet;
    }

    public ChooseItemDetailBottomSheet()
    {

    }

    public ChooseItemDetailBottomSheet(@NonNull String productName, int maxAmount, int singlePrice, @Nullable Bitmap bitmap)
    {
        this.productName = productName;
        this.productMaxAmount = maxAmount;
        this.productSinglePrice = singlePrice;
        this.bitmap = bitmap;
    }

    public class ChooseItemDetailController
    {
        BuyInfo buyInfo;
    }
}
