package exam.nlb2t.epot.Views.Registration;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exam.nlb2t.epot.R;

public class fragment_signup_enterotp extends Fragment {
    public EditText edt_otp;
    public TextView tv_sent_otp;
    public TextView tv_coundown;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.signup_enterotp, container, false);

        edt_otp = (EditText) view.findViewById(R.id.et_otp);
        tv_sent_otp = (TextView) view.findViewById(R.id.tv_sent_otp);
        tv_coundown = (TextView) view.findViewById(R.id.tv_countdown);
        tv_sent_otp.setVisibility(View.INVISIBLE);

        Pattern pattern = Pattern.compile(".*\\D.*");
        edt_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher matcher = pattern.matcher(edt_otp.getText().toString());
                if (matcher.find() || edt_otp.length() != 6)
                    edt_otp.setError("Sai định dạng");
            }
        });

        return view;
    }
}
