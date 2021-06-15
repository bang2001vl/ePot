package exam.nlb2t.epot.Views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import exam.nlb2t.epot.R;

public class forgotpass_enter_phone extends AppCompatActivity {
    private EditText edt_phone;
    private TextView tv_Error;
    private Button btn_sentOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fogotpass_enter_phone);


    }

}