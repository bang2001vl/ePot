package exam.nlb2t.epot.Views.Forgotpass;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.Error_toast;
import exam.nlb2t.epot.Views.Registration.fragment_signup_enterotp;
import exam.nlb2t.epot.Views.Registration.signup_enterphone;


public class forgotpassword extends AppCompatActivity {

    private fragment_signup_enterotp fg_forgotpass_otp;
    private signup_enterphone  fg_forgot_phone;
    private forgotpass_create_newpass fg_fogotpass_create_newpass;
    private EditText edt_otp;

    String phone;
    int count = 0;
    int Issend = 1;
    String verificationId;
    String Code;

    private FirebaseAuth mAuth;

    Button btn_next;
    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        fg_forgot_phone = new signup_enterphone();

        ReplaceFragment(fg_forgot_phone);
        mAuth = FirebaseAuth.getInstance();

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_back = (ImageButton) findViewById(R.id.btn_back);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_next.getText().toString().equals(getResources().getString(R.string.Sent_OTP)))
                {
                    phone = fg_forgot_phone.edt_phone.getText().toString();

                    if (fg_forgot_phone.edt_phone.getError() == null)
                    {
                        if (phone.length() == 9) phone = "+84" + fg_forgot_phone.edt_phone.getText().toString();
                        fg_forgotpass_otp = new fragment_signup_enterotp();
                        DBControllerUser controllerUser = new DBControllerUser();
                        if(controllerUser.checkExistPhone(phone))
                        {
                            fg_forgot_phone.tv_error.setVisibility(View.INVISIBLE);
                            sendVerificationCode(phone);

                            ReplaceFragment(fg_forgotpass_otp);
                            btn_next.setText(getResources().getString(R.string.Continue));
                        }
                        else
                        {
                            fg_forgot_phone.tv_error.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else
                {
                        edt_otp = fg_forgotpass_otp.edt_otp;
                        if(btn_next.getText().toString().equals(getResources().getString(R.string.Continue))) {
                            if (edt_otp.getError() == null)
                            {
                                verifyCode(edt_otp.getText().toString());
                            }
                        }
                        else
                        {
                            if (fg_fogotpass_create_newpass.til_pass.getError() == null && fg_fogotpass_create_newpass.til_define_pass.getError() == null)
                            {
                                DBControllerUser controllerUser = new DBControllerUser();
                                controllerUser.UpdatePassword(phone, fg_fogotpass_create_newpass.tit_pass.getText().toString());
                                finish();
                            }
                        }
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_next.getText().toString().equals(getResources().getString(R.string.Sent_OTP))) {
                    finish();
                }
                else
                {
                    if (btn_next.getText().toString().equals(getResources().getString(R.string.Continue)))
                    {
                        /* fg_signup_enterotp.btn_next = btn_next;*/
                        fg_forgotpass_otp.tv_coundown.setText(0);
                        fg_forgotpass_otp.tv_sent_otp.setVisibility(View.INVISIBLE);
                        fg_forgotpass_otp.edt_otp.setText("");
                        ReplaceFragment(fg_forgot_phone);
                        btn_next.setText(R.string.Sent_OTP);
                    }
                    else
                    {
                        ReplaceFragment(fg_forgot_phone);
                        btn_next.setText(R.string.Sent_OTP);
                    }
                }
            }
        });

    }

    private  void ReplaceFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            FragmentTransaction fg_transaction = getSupportFragmentManager().beginTransaction();
            fg_transaction.replace(R.id.fg_forgotpass, fragment);
            fg_transaction.commit();
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            fg_fogotpass_create_newpass = new forgotpass_create_newpass();
                            ReplaceFragment(fg_fogotpass_create_newpass);
                            btn_next.setText(R.string.Confirm);

                        } else {
                            toast_layout.show(forgotpassword.this, getResources().getString(R.string.error_wrong_OTP), true);
                        }
                    }
                });
    }
    private void sendVerificationCode(String number) {
        // this method is used for getting OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it contains a unique id which we are storing in our string which we have already created.

            verificationId = s;
            new CountDownTimer(120000, 1000) {
                public void onTick(long millisUntilFinished) {
                    fg_forgotpass_otp.tv_coundown.setText( "(" + millisUntilFinished / 1000 + ")");
                }
                public void onFinish() {
                    fg_forgotpass_otp.tv_coundown.setText( "(0)");
                    fg_forgotpass_otp.tv_sent_otp.setVisibility(View.VISIBLE);
                    Issend = 0;

                    fg_forgotpass_otp.tv_sent_otp.setOnClickListener(v -> {
                        Issend = 0;
                        sendVerificationCode(phone);
                        fg_forgotpass_otp.tv_sent_otp.setVisibility(View.INVISIBLE);
                    });
                }
            }.start();

        }

        // this method is called when user receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code  which is sent in phone auth credentials.
            Code = phoneAuthCredential.getSmsCode();
            // checking if the code is null or not.
        }

        // this method is called when firebase doesn't sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Error_toast.show(forgotpassword.this, getResources().getString(R.string.error_connect_firebar), true );
            finish();
        }
    };

    // below method is use to verify code from Firebase.
    private Boolean verifyCode(String code) {
        // below line is used for getting getting credentials from our verification id and code.
        Pattern pattern = Pattern.compile(".*\\D.*");
        Matcher matcher = pattern.matcher(code);
        ++count;
        if ( code.length() != 6 || matcher.find() )
        {
            return false;
        }
        else
        {
            if (count >= 5)
            {
                Error_toast.show(forgotpassword.this,  getResources().getString(R.string.error_5_otp), true );
                count = 0;
                finish();
            }
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            // after getting credential we are calling sign in method.

            if ( Issend==1 && code != credential.getSmsCode())
            {

                Error_toast.show(forgotpassword.this,  getResources().getString(R.string.error_wrong_OTP), true );
                return false;
            }
            signInWithCredential(credential);
        }
        return true;
    }

}