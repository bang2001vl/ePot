package exam.nlb2t.epot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import exam.nlb2t.epot.Database.DatabaseController;

public class LoginTab extends Fragment {
    Context context;
    EditText email;
    EditText pass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.login_tab, container,false);
        root.findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.context = root.getContext();
        return  root;
    }

    void Login(String username, String password)
    {
        DatabaseController databaseController = new DatabaseController();
        if(databaseController.CheckUserExist(username))
        {
            if(databaseController.CheckPassword(username, password))
            {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                FragmentActivity parent = getActivity();
                if(parent != null)
                {
                    parent.finish();
                }
                else
                {
                    Log.e("NULL POINTER", "parent activity is null");
                }
            }
        }
    }
}
