package exam.nlb2t.epot.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import exam.nlb2t.epot.R;
import exam.nlb2t.epot.signup_enterphone;

public class signup extends AppCompatActivity {

    private Button btn_back;
    private Button btn_next;
    private EditText edt_phonenumber;
    private EditText edt_otp;
    private FirebaseAuth mAuth;

    private String verificationId;

    private fragment_signup_enterotp fg_signup_enterotp;
    private signup_enterphone fg_signup_enterphone;
    private fragment_login_new_account fg_signup_new_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        fg_signup_enterotp = new fragment_signup_enterotp();
        fg_signup_enterphone = new signup_enterphone();
        fg_signup_new_account = new fragment_login_new_account();

        ReplaceFragment(fg_signup_enterphone);
        mAuth = FirebaseAuth.getInstance();

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_next.getText().toString().equals(getResources().getString(R.string.Sent_OTP)))
                {
                    ReplaceFragment(fg_signup_enterotp);
                    btn_next.setText(R.string.Continue);
                    btn_next.setEnabled(false);
                    String phone = "+84" + fg_signup_enterphone.edt_phone.getText().toString();
                    sendVerificationCode(phone);
                }
                else
                {
                    edt_otp = fg_signup_enterotp.edt_otp;
                    if (btn_next.getText().toString().equals(getResources().getString(R.string.Continue)))
                    {
                        verifyCode(edt_otp.getText().toString());
                    }
                    else
                    {
                        finish();
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
                       ReplaceFragment(fg_signup_enterphone);
                        btn_next.setText(R.string.Sent_OTP);
                    }
                    else
                    {
                        ReplaceFragment(fg_signup_enterotp);
                        btn_next.setText(R.string.Continue);
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
           /* fg_transaction.add(R.id.container_body, fragment);
            fg_transaction.addToBackStack(fragment.getClass().getSimpleName());*/
            fg_transaction.replace(R.id.container_body, fragment);
            fg_transaction.commit();
        }

    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // Mở  fragment đăng kí mới
                            ReplaceFragment(new fragment_login_new_account());
                            btn_next.setText(R.string.Sign_Up);

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edt_otp.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

}
