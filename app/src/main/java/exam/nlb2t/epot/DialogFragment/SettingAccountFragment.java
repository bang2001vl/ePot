package exam.nlb2t.epot.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exam.nlb2t.epot.ChangePasswordFragment;
import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Database.Tables.UserBaseDB;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentSettingAccountBinding;
import exam.nlb2t.epot.singleton.Authenticator;

public class SettingAccountFragment extends DialogFragment {

    FragmentSettingAccountBinding binding;
    private UserBaseDB currentuser= Authenticator.getCurrentUser();
    Calendar myCalendar;
    DBControllerUser dbControllerUser=new DBControllerUser();
    Context context;

    public SettingAccountFragment() {
        // Required empty public constructor
    }
    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingAccountBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvUsername.setText(currentuser.username);
        binding.tvFullname.setText(currentuser.fullName);
        getBirthday();
        binding.tvSex.setSelection(currentuser.gender);

    }
    private void setEventHandler() {
            binding.btnChangeprofile.setVisibility(View.VISIBLE);
            setVisible();
            binding.btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   dismiss();
                }
            });
            binding.btnChangeprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.btnChangeprofile.setVisibility(View.INVISIBLE);
                    setVisible();

                }
            });
            binding.btnSaveprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckErrorUserInfo() == -1) {
                        Toast.makeText(context, getResources().getString(R.string.error_not_enough_info), Toast.LENGTH_SHORT).show();
                    } else {
                        if (CheckErrorUserInfo() == 0) {
                            Toast.makeText(context, getResources().getString(R.string.error_incorrect_info), Toast.LENGTH_SHORT).show();
                        } else {
                            binding.btnChangeprofile.setVisibility(View.VISIBLE);
                            setVisible();
                            int day = Integer.parseInt(binding.tvBirthday.getText().toString().substring(0, 2));
                            int month = Integer.parseInt(binding.tvBirthday.getText().toString().substring(3, 5)) - 1;
                            int year = Integer.parseInt(binding.tvBirthday.getText().toString().substring(6, 10));
                            dbControllerUser.updateUser(currentuser.id, binding.tvFullname.getText().toString(), binding.tvSex.getSelectedItemPosition(), day, month, year);
                        }
                    }
                }
            });
            binding.btnChangepassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangePasswordFragment changePasswordFragment=new ChangePasswordFragment();
                    changePasswordFragment.show(getFragmentManager(),"Tag");

                }
            });
        String[] items = new String[]{"Nam", "Nữ"};
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item ,items);
        binding.tvSex.setAdapter(adapter);

        Pattern pattern = Pattern.compile("[\\p{P}\\p{S}]");

        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthday();
            }

        };

        binding.tvBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.tvFullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher matcher = pattern.matcher(binding.tvFullname.getText().toString());
                if (matcher.find()) {
                    binding.tvFullname.setError(getResources().getString(R.string.error_not_special_char));
                } else {
                    if (binding.tvFullname.getText().toString().length() > 50) {
                        binding.tvFullname.setError(getResources().getString(R.string.error_not_50_char));
                    } else {
                        binding.tvFullname.setError(null);
                    }
                }
            }
        });

    }
    private void updateBirthday() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CHINESE);

        binding.tvBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    private  void getBirthday(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CHINESE);

        binding.tvBirthday.setText(sdf.format(currentuser.birthday));
    }
    private void setVisible()
    {
        if (binding.btnChangeprofile.getVisibility()==View.VISIBLE){
            binding.btnSaveprofile.setVisibility(View.INVISIBLE);
            binding.tvFullname.setEnabled(false);
            binding.tvSex.setEnabled(false);
            binding.tvBirthday.setEnabled(false);
        }
        else {
            binding.btnSaveprofile.setVisibility(View.VISIBLE);
            binding.tvFullname.setEnabled(true);
            binding.tvSex.setEnabled(true);
            binding.tvBirthday.setEnabled(true);
        }
    }
    private int CheckErrorUserInfo()
    {
        if (binding.tvFullname.getText().toString().equals("")|| (binding.tvBirthday.getText().toString().equals("")))
        {
            return -1;
        }
        if (binding.tvFullname.getError() != null || binding.tvBirthday.getError() != null)
        {
            return 0;
        }
        return 1;
    }
}