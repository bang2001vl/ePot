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
            if(!checkPassword())
            {
                return;
            }
            String email = this.emailEDT.getText().toString();
            String phone = this.phoneNummber.getText().toString();
            if(!checkUsername(phone, email)){return;}

            DatabaseController databaseController = new DatabaseController();

            if(databaseController.InsertUser(phone, email, password.getText().toString(), "test_name", 0,
                    Calendar.getInstance().getTime().toString(), "08/30/2001", "test_address", "test_shopname",
                    0, "test_info"))
            {
                Toast.makeText(getContext(), "Sign up successfully !!!", Toast.LENGTH_LONG).show();
                if(this.onSignUpSuccessListener != null)
                {
                    onSignUpSuccessListener.OnSuccess(SignupTab.this);
                }
            }
            else
            {
                Toast.makeText(getContext(), "Somethings went wrong", Toast.LENGTH_LONG).show();
            }
            databaseController.closeConnection();
        });
        return  root;
    }

    boolean checkUsername(String phone, String email)
    {
        DatabaseController databaseController = new DatabaseController();
        boolean rs = !databaseController.CheckEmailExist(email);
        if(rs) {
            rs = !databaseController.CheckphoneExist(phone);
            if(!rs)
            {
                Toast.makeText(getContext(), "Phone number already exist", Toast.LENGTH_LONG).show();
                this.phoneNummber.selectAll();
                this.phoneNummber.requestFocus();
            }
        }
        else
        {
            Toast.makeText(getContext(), "Email already exist", Toast.LENGTH_LONG).show();
            this.emailEDT.selectAll();
            this.emailEDT.requestFocus();
        }
        databaseController.closeConnection();

        return rs;
    }

    boolean checkPassword()
    {
        return password2.getText().toString().equals(password.getText().toString()) ;
    }

    public interface OnSignUpSuccessListener
    {
        void onSignUpSuccess();
    }
}
