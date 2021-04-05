package exam.nlb2t.epot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thunderstudio.mylib.Views.ChooseAmountLayout;

import java.util.List;

public class ChooseItemDetailBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG_NAME = "PRODUCT_NAME";
    public static final String TAG_MAX = "MAX";
    public static final String TAG_PRICE = "PRICE";
    public static final String TAG_AMOUNT = "AMOUNT";

    String productName = "DEFAULT_NAME";
    int productMaxAmount = 100;
    int productSinglePrice = 1000;

    List<Pair<String, String[]>> list_options = null;
    ChooseAmountLayout amountPicker;
    public int getAmount()
    {
        return (int)amountPicker.controller.getNumber();
    }

    RadioButton[] selectedOption;
    public int getSelectedOptionIndex(int list_options_index)
    {
        return ((FlexboxLayout.LayoutParams)selectedOption[list_options_index].getLayoutParams()).getOrder();
    }
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
        amountPicker = chooseAmountLayout;

        if(this.bitmap != null)
        {
            ImageView imageView = view.findViewById(R.id.image_item);
            imageView.setImageBitmap(bitmap);
        }

        if(this.list_options != null)
        {
            Context context = view.getContext();
            LinearLayout options_holder = view.findViewById(R.id.options_holder);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            float size = getResources().getDimension(R.dimen.normal_button_text);
            selectedOption = new RadioButton[list_options.size()];

            for (int list_option_index = 0; list_option_index <list_options.size(); list_option_index++) {
                Pair<String, String[]> options = list_options.get(list_option_index);

                LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_option, container, false);
                options_holder.addView(linearLayout, params);

                TextView title = linearLayout.findViewById(R.id.title);
                FlexboxLayout layout_options = linearLayout.findViewById(R.id.flexboxlayout_options);

                title.setText(options.first);
                for (int k =0; k<options.second.length; k++) {
                    String option = options.second[k];
                    RadioButton radioButton = new RadioButton(context);
                    FlexboxLayout.LayoutParams option_params = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    option_params.setMargins(5,0,5,0);
                    if(selectedOption[list_option_index] == null)
                    {
                        selectedOption[list_option_index] = radioButton;
                        radioButton.setChecked(true);
                    }
                    else {radioButton.setChecked(false);}

                    radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
                    radioButton.setText(option);
                    radioButton.setTag(list_option_index);
                    radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked)
                            {
                                int index = (int)buttonView.getTag();
                                selectedOption[index].setChecked(false);
                                selectedOption[index] = radioButton;
                            }
                        }
                    });
                    option_params.setOrder(k);
                    layout_options.addView(radioButton, option_params);
                }
            }
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                Log.d("MY_TAG", String.format ("Amount = %1$d", (int)chooseAmountLayout.controller.getNumber()));
                for(int i = 0; selectedOption!= null && i<selectedOption.length; i++)
                {
                    Log.d("MY_TAG", String.format ("Option %1$d(%4$s) choose index = %2$d (value = %3$s)", i, getSelectedOptionIndex(i), selectedOption[i].getText(), list_options.get(i).first));
                }
                Toast.makeText(ChooseItemDetailBottomSheet.this.getContext(), "Buy : " + (int)chooseAmountLayout.controller.getNumber(), Toast.LENGTH_LONG);
            }
        });
        return view;
    }

    public static ChooseItemDetailBottomSheet newInstance(@NonNull String productName, int maxAmount, int singlePrice, @Nullable Bitmap bitmap, @Nullable List<List<String>> list_options)
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

    public ChooseItemDetailBottomSheet(@NonNull String productName, int maxAmount, int singlePrice, @Nullable Bitmap bitmap, @Nullable List<Pair<String, String[]>> list_options)
    {
        this.productName = productName;
        this.productMaxAmount = maxAmount;
        this.productSinglePrice = singlePrice;
        this.bitmap = bitmap;
        this.list_options = list_options;
    }

    public interface OnClickSubmit
    {
        void OnClickSubmit(Context context, int amount, int[] params);
    }
}
