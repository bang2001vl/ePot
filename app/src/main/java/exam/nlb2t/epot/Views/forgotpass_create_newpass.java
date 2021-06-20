package exam.nlb2t.epot.Views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import exam.nlb2t.epot.R;

public class forgotpass_create_newpass extends Fragment {

    TextInputEditText tit_pass;
    TextInputEditText tit_define_pass;
    TextInputLayout til_define_pass;
    TextInputLayout til_pass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgotpass_creat_newpass, container, false);

        tit_define_pass = (TextInputEditText) view.findViewById(R.id.tit_define_pass);
        tit_pass = (TextInputEditText) view.findViewById(R.id.tit_pass);
        til_define_pass =(TextInputLayout) view.findViewById(R.id.til_define_pass);
        til_pass =(TextInputLayout) view.findViewById(R.id.til_pass);

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
                    /*tit_pass.setError(getResources().getString(R.string.error_length_6_to_50));*/
                    til_pass.setError(getResources().getString(R.string.error_length_6_to_50));
                }
                else
                {
                    til_pass.setError(null);
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
                if (!Objects.requireNonNull(tit_pass.getText()).toString().equals(s.toString()))
                {
                    /*tit_define_pass.setError(getResources().getString(R.string.error_define_pass));*/
                    til_define_pass.setError(getResources().getString(R.string.error_define_pass));
                }
                else
                {
                   /* tit_define_pass.setError(null);*/
                    til_define_pass.setError(null);
                }
            }
        });
        return view;
    }
}
