package exam.nlb2t.epot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.singleton.Helper;

public class SignupTab extends Fragment {
    Helper.OnSuccessListener onSignUpSuccessListener;

    public void setOnSignUpSuccessListener(Helper.OnSuccessListener onSignUpSuccessListener) {
        this.onSignUpSuccessListener = onSignUpSuccessListener;
    }

    EditText phoneNummber;
    EditText password;
    EditText password2;
    EditText emailEDT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.signup_tab, container,false);

        emailEDT = root.findViewById(R.id.signup_email);
        phoneNummber = root.findViewById(R.id.mobile_nums);
        password = root.findViewById(R.id.signup_password);
        password2 = root.findViewById(R.id.confirm);

        root.findViewById(R.id.Signup).setOnClickListener(v -> {
            checkEmailFormat();
            checkPhoneFormat();
            checkPasswordStrength();
            if(emailEDT.getError() != null || phoneNummber.getError() != null || password.getError() != null)
            {
                return;
            }

            checkPasswordSimilar();
            if(password2.getError() != null){return;}

            String email = this.emailEDT.getText().toString();
            String phone = this.phoneNummber.getText().toString();
            checkUsernameExist(phone, email);
            if(emailEDT.getError() != null || phoneNummber.getError() != null){return;}

            RegistAccount();
        });

        password.setOnFocusChangeListener((View.OnFocusChangeListener) (v, hasFocus) -> {
            if(!hasFocus)
            {
                checkPasswordStrength();
            }
        });

        emailEDT.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                checkEmailFormat();
            }
        });

        phoneNummber.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                checkPhoneFormat();
            }
        });

        return  root;
    }

    void RegistAccount()
    {
        String email = this.emailEDT.getText().toString();
        String phone = this.phoneNummber.getText().toString();
        DatabaseController databaseController = new DatabaseController();
        Calendar current = Calendar.getInstance();
        if(databaseController.InsertUser(phone, email, password.getText().toString(), "test_name", 0,
                Helper.getInstance(getContext()).getDateTime(current),
                "08/30/2001", "test_address", "test_shopname",
                0, "test_info"))
        {
            Toast.makeText(getContext(), "Sign up successfully !!!", Toast.LENGTH_LONG).show();
            if(this.onSignUpSuccessListener != null)
            {
                emailEDT.setText("");
                phoneNummber.setText("");
                password.setText("");
                password2.setText(password.getText());
                onSignUpSuccessListener.OnSuccess(SignupTab.this);
            }
        }
        else
        {
            Toast.makeText(getContext(), "Somethings went wrong", Toast.LENGTH_LONG).show();
        }
        databaseController.closeConnection();
    }

    void checkPhoneFormat()
    {
        String phone = phoneNummber.getText().toString();
        if(phone.length() == 0)
        {
            phoneNummber.setError("Please insert your phone number");
            return;
        }
        if (phone.length() != 10 || phone.charAt(0) != '0') {
            phoneNummber.setError("Phone number is wrong format");
        }
    }

    void checkEmailFormat()
    {
        String email = emailEDT.getText().toString();
        if (email.length()==0) {
            emailEDT.setError("Please insert your email");
            return;
        }
        if (!email.contains("@") || !email.contains(".") || (email.lastIndexOf("@") != email.indexOf("@"))) {
            emailEDT.setError("Email is wrong format");
        }
    }

    void checkPasswordStrength()
    {
        String msg;
        String pass = password.getText().toString();
        if(pass.length() < 6 || pass.length() > 32)
        {
            msg = "Password must has 6-32 characters";
            password.setError(msg);
            return;
        }
        int i = 0;
        for(;i<pass.length();i++)
        {
            if(Character.isLowerCase(pass.charAt(i))){break;}
        }
        if(i == pass.length())
        {
            msg = "Password must has at least 1 letter (a-z)";
            password.setError(msg);
            return;
        }
        i = 0;
        for(;i<pass.length();i++)
        {
            if(Character.isUpperCase(pass.charAt(i))){break;}
        }
        if(i == pass.length())
        {
            msg = "Password must has at least 1 letter (A-Z)";
            password.setError(msg);
            return;
        }
        i = 0;
        for(;i<pass.length();i++)
        {
            if(Character.isDigit(pass.charAt(i))){break;}
        }
        if(i == pass.length())
        {
            msg = "Password must has at least 1 digit (0-9)";
            password.setError(msg);
            return;
        }
        i = 0;
        for(;i<pass.length();i++)
        {
            if(!Character.isLetterOrDigit(pass.charAt(i))){break;}
        }
        if(i == pass.length())
        {
            msg = "Password must has at least 1 special character";
            password.setError(msg);
            return;
        }
    }

    void checkUsernameExist(String phone, String email)
    {
        DatabaseController databaseController = new DatabaseController();
        if(databaseController.CheckEmailExist(email)) {
            this.emailEDT.setError("Email already exist");
        }
        else
        {
            if(databaseController.CheckphoneExist(phone))
            {
                this.phoneNummber.setError("Phone number already exist");
            }
        }
        databaseController.closeConnection();
    }

    void checkPasswordSimilar()
    {
        String msg;
        if(!password2.getText().toString().equals(password.getText().toString()))
        {
            msg = "Re-enter password is not correct";
            password2.setError(msg);
        }
    }
}
