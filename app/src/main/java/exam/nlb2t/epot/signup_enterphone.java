package exam.nlb2t.epot;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signup_enterphone#} factory method to
 * create an instance of this fragment.
 */
public class signup_enterphone extends Fragment {

    private Button btn_sent_otp;
    public EditText edt_phone;
    public TextView tv_error;
    private AppCompatSpinner acs_country;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_signup_enterphone, container, false);
        edt_phone = (EditText) view.findViewById(R.id.et_Phone);
        tv_error = (TextView) view.findViewById(R.id.Error);
        acs_country = (AppCompatSpinner) view.findViewById(R.id.acs_country);

        String[] items = new String[]{"+84"};
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_item ,items);
        acs_country.setAdapter(adapter);

        Pattern pattern = Pattern.compile(".*\\D.*");
        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher matcher = pattern.matcher(edt_phone.getText().toString());
                if( matcher.find()) {
                    edt_phone.setError("Chỉ được nhập số!");
                }
                else
                {
                    if(edt_phone.length() != 9)
                    {
                        edt_phone.setError("Nhập sai định dạng sđt!");
                    }
                }
            }
        });
        return view;

    }

}