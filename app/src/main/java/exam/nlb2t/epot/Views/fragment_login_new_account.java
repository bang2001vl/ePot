package exam.nlb2t.epot.Views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exam.nlb2t.epot.R;

public class fragment_login_new_account extends Fragment {

    public EditText edt_usename;
    public TextInputEditText tit_pass;
    public TextInputEditText tit_define_pass;
    public EditText edt_name;
    public EditText edt_birth;
    public AppCompatSpinner acs_sex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_new_account, container, false);

        edt_usename = (EditText) view.findViewById(R.id.tv_user_name);
        edt_name = (EditText) view.findViewById(R.id.et_name);
        edt_birth = (EditText) view.findViewById(R.id.birthday);
        tit_pass = (TextInputEditText) view.findViewById(R.id.tit_pass);
        tit_define_pass = (TextInputEditText) view.findViewById(R.id.tit_define_pass);
        acs_sex = (AppCompatSpinner) view.findViewById(R.id.acs_sex);

        String[] items = new String[]{"Nam", "Nữ"};
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_item ,items);
        acs_sex.setAdapter(adapter);

        Pattern pattern = Pattern.compile("[\\p{P}\\p{S}]");

        edt_usename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher matcher = pattern.matcher(edt_usename.getText().toString());
                if (matcher.find()) {
                    edt_usename.setError("Chỉ chứa kí tự a-z, A-Z, 0-9");
                } else {
                    edt_usename.setError(null);
                    if (edt_usename.getText().toString().length() > 50) {
                        edt_usename.setError("Độ dài không quá 50 kí tự");
                    } else {
                        edt_usename.setError(null);
                    }
                }
            }
        });
        tit_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ( tit_pass.length() < 6 ||  tit_pass.length() > 50)
                {
                    tit_pass.setError("Độ dài mật khẩu tối thiểu 6, tối đa 50 kí tự");
                }
            }
        });
        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher matcher = pattern.matcher(edt_name.getText().toString());
                if (matcher.find()) {
                    edt_name.setError("Chỉ chứa kí tự a-z, A-Z, 0-9");
                } else {
                    edt_name.setError(null);
                    if (edt_name.getText().toString().length() > 50) {
                        edt_name.setError("Độ dài không quá 50 kí tự");
                    } else {
                        edt_name.setError(null);
                    }
                }
            }
        });
        tit_define_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tit_pass.getText() != tit_define_pass.getText())
                {
                    tit_define_pass.setError("Xác nhận mật khẩu sai!");
                }
            }
        });
        return view;
    }
}
