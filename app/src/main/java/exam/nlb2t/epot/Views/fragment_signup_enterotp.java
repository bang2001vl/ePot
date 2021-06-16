package exam.nlb2t.epot.Views;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.regex.Pattern;

import exam.nlb2t.epot.R;

public class fragment_signup_enterotp extends Fragment {
    public EditText edt_otp;
    public TextView tv_sent_otp;
    private TextView tv_coundown;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.signup_enterotp, container, false);
        edt_otp = (EditText) view.findViewById(R.id.et_otp);
        tv_sent_otp = (TextView) view.findViewById(R.id.tv_sent_otp);
        tv_coundown = (TextView) view.findViewById(R.id.tv_countdown);

        Pattern pattern = Pattern.compile("^0-9");

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_coundown.setText( "" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                /*Button btn_next= container.findViewById(R.id.btn_next);
                if (btn_next != null)
                {
                    btn_next.setEnabled(true);
                }*/
            }
        }.start();
        return view;
    }
}
