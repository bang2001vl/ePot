package exam.nlb2t.epot.Views;

import android.content.Context;
import android.nfc.FormatException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.thunderstudio.mylib.OnValueChanged;
import com.thunderstudio.mylib.Views.ChooseAmountLayout;
import com.thunderstudio.mylib.Views.ChooseNumberLayoutController;

import java.util.Locale;

public class NumberPickerView extends ChooseAmountLayout {
    OnValueChanged<Float> onValueChangeListener;
    public void setOnValueChangeListener(OnValueChanged<Float> onValueChangeListener)
    {
        this.onValueChangeListener = onValueChangeListener;
    }

    public NumberPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    checkEditText();
                }
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    checkEditText();
                    return false;
                }
                return  false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEditText();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEditText();
            }
        });

        controller.setOnPropertyChanged(new OnValueChanged<Float>() {
            @Override
            public void onValueChanged(Float newValue) {

                editText.setText(String.valueOf(newValue.intValue()));
                if(onValueChangeListener != null)
                {
                    onValueChangeListener.onValueChanged(newValue);
                }
            }
        });

    }

    public void checkEditText()
    {
        int val = (int)controller.getNumber();
        try {
            val = Integer.parseInt(editText.getText().toString());
        }
        catch (NumberFormatException e){
            editText.setText(String.valueOf(val));
            editText.setSelection(editText.getText().length());
        }

        if(val != controller.getNumber())
        {
            controller.setNumber(val);
            editText.setText(String.format(Locale.getDefault(),"%d",(int)controller.getNumber()));
            editText.setSelection(editText.getText().length());
        }
    }
}
