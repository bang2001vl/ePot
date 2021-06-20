package exam.nlb2t.epot.Views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
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
    public TextInputLayout til_pass;
    public TextInputLayout til_confirm_pass;

    Calendar myCalendar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_new_account, container, false);

        edt_usename = (EditText) view.findViewById(R.id.et_usename);
        edt_name = (EditText) view.findViewById(R.id.et_name);
        edt_birth = (EditText) view.findViewById(R.id.birthday);
        tit_pass = (TextInputEditText) view.findViewById(R.id.tit_pass);
        tit_define_pass = (TextInputEditText) view.findViewById(R.id.tit_define_pass);
        acs_sex = (AppCompatSpinner) view.findViewById(R.id.acs_sex);
        til_pass = (TextInputLayout) view.findViewById(R.id.til_pass);
        til_confirm_pass = (TextInputLayout) view.findViewById(R.id.til_define_pass);

        String[] items = new String[]{"Nữ", "Nam"};
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_item ,items);
        acs_sex.setAdapter(adapter);

        Pattern pattern = Pattern.compile("[\\p{P}\\p{S}]");

        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edt_birth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(container.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
                    edt_usename.setError(getResources().getString(R.string.error_not_special_char));
                }
                else
                    {
                        if (edt_usename.getText().toString().length() > 50)
                        {
                            edt_usename.setError(getResources().getString(R.string.error_not_50_char));
                        }
                        else
                            {
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
                    /*tit_pass.setError(getResources().getString(R.string.error_length_6_to_50));*/
                    til_pass.setError(getResources().getString(R.string.error_length_6_to_50));
                }
                else
                {
                    til_pass.setError(null);
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
                    edt_name.setError(getResources().getString(R.string.error_not_special_char));
                } else {
                    if (edt_name.getText().toString().length() > 50) {
                        edt_name.setError(getResources().getString(R.string.error_not_50_char));
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
                if (!Objects.requireNonNull(tit_pass.getText()).toString().equals(s.toString()))
                {
                    til_confirm_pass.setError(getResources().getString(R.string.error_define_pass));
                }
                else
                {
                    til_confirm_pass.setError(null);
                }
            }
        });
        return view;
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CHINESE);

       edt_birth.setText(sdf.format(myCalendar.getTime()));
    }
}
