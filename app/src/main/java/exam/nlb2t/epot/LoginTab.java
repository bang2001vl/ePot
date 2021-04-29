package exam.nlb2t.epot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import exam.nlb2t.epot.Database.DatabaseController;
import exam.nlb2t.epot.singleton.Helper;

public class LoginTab extends Fragment {
    Helper.OnSuccessListener onLoginSuccessListener;

    public void setOnLoginSuccessListener(Helper.OnSuccessListener onLoginSuccessListener) {
        this.onLoginSuccessListener = onLoginSuccessListener;
    }

    EditText email;
    EditText pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.login_tab, container,false);
        email = root.findViewById(R.id.login_email);
        pass = root.findViewById(R.id.login_pass);
        root.findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(email.getText().toString(), pass.getText().toString());
            }
        });
        return  root;
    }

    boolean Login(String username, String password)
    {
        DatabaseController databaseController = new DatabaseController();
        if(!databaseController.CheckUserExist(username))
        {
            Toast.makeText(getContext(), "This Email/PhoneNumber not sign up yet", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!databaseController.CheckPassword(username, password))
        {
            Toast.makeText(getContext(), "Your password is in correct", Toast.LENGTH_LONG).show();
            return false;
        }

        if(onLoginSuccessListener != null)
        {
            onLoginSuccessListener.OnSuccess(LoginTab.this);
        }
        return true;
    }
}
