package exam.nlb2t.epot.ProductDetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thunderstudio.mylib.Views.ChooseAmountLayout;

import java.util.List;
import java.util.Locale;

import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.NumberPickerView;
import exam.nlb2t.epot.singleton.Helper;

public class ChooseItemDetailBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG_NAME = "PRODUCT_NAME";
    public static final String TAG_MAX = "MAX";
    public static final String TAG_PRICE_ORIGIN = "PRICE_ORIGIN";
    public static final String TAG_PRICE_CURRENT = "PRICE_CURRENT";
    public static final String TAG_AMOUNT = "AMOUNT";

    String productName = "DEFAULT_NAME";
    int productMaxAmount = 100;
    int productSinglePrice_origin = 1000;
    int productSinglePrice_current = 1000;

    List<Pair<String, String[]>> list_options = null;
    NumberPickerView amountPicker;
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

        NumberPickerView chooseAmountLayout = view.findViewById(R.id.amount_picker);

        TextView txtName = view.findViewById(R.id.txtName_item);
        TextView txtMax = view.findViewById(R.id.txtAmount_Max);
        TextView txtPrice_origin = view.findViewById(R.id.price_origin);
        TextView txtPrice_current = view.findViewById(R.id.price_current);
        Button btnSubmit = view.findViewById(R.id.btn_submit);

        if(savedInstanceState != null) {
            this.productMaxAmount = savedInstanceState.getInt(TAG_MAX);
            this.productName = savedInstanceState.getString(TAG_NAME);
            this.productSinglePrice_origin = savedInstanceState.getInt(TAG_PRICE_ORIGIN);
        }

        txtName.setText(this.productName);
        txtMax.setText(getString(R.string.max_formatted, this.productMaxAmount));
        if(productSinglePrice_current < productSinglePrice_origin)
        {
            txtPrice_origin.setPaintFlags(txtPrice_origin.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtPrice_origin.setText(Helper.getMoneyString(productSinglePrice_origin));
        }
        else
        {
            txtPrice_origin.setVisibility(View.GONE);
        }
        txtPrice_current.setText(Helper.getMoneyString(productSinglePrice_current));

        chooseAmountLayout.controller.min = 1;
        chooseAmountLayout.controller.max = productMaxAmount;
        chooseAmountLayout.controller.setNumber(1);
        amountPicker = chooseAmountLayout;

        if(this.bitmap != null)
        {
            ImageView imageView = view.findViewById(R.id.image_item);
            imageView.setImageBitmap(bitmap);
        }

        /*if(this.list_options != null)
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
*/

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {

                /*int[] selected_params = null;
                if(list_options != null) {
                    selected_params = new int[list_options.size()];
                    for (int i = 0; i < selected_params.length; i++) {
                        selected_params[i] = getSelectedOptionIndex(i);
                    }
                }*/
                if(onClickSubmitListener != null) {
                    onClickSubmitListener.OnClickSubmit(view, (int) amountPicker.controller.getNumber(), null);
                }
            }
        });
        return view;
    }

    public void setAmountSold(int val)
    {

    }

    OnClickSubmitListener onClickSubmitListener =  null;

    public void setOnClickSubmitListener(OnClickSubmitListener listener)
    {
        onClickSubmitListener = listener;
    }

    public ChooseItemDetailBottomSheet()
    {

    }

    public ChooseItemDetailBottomSheet(@NonNull String productName, int maxAmount, int price_origin, int price_current, @Nullable Bitmap bitmap, @Nullable List<Pair<String, String[]>> list_options)
    {
        this.productName = productName;
        this.productMaxAmount = maxAmount;
        this.productSinglePrice_origin =  price_origin;
        this.productSinglePrice_current = price_current;
        this.bitmap = bitmap;
        this.list_options = list_options;
    }

    public interface OnClickSubmitListener
    {
        void OnClickSubmit(View view, int amount, int[] params);
    }
}
