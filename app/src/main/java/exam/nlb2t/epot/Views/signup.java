package exam.nlb2t.epot.Views;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import exam.nlb2t.epot.signup_enterphone;

public class signup extends AppCompatActivity {

    private Button btn_back;
    private Button btn_next;
    private EditText edt_phonenumber;
    private EditText edt_otp;
    private FirebaseAuth mAuth;
    private String phone;
    private LinearLayout ln_logo;

    private String verificationId;
    private int count = 0;
    private String Code;
    ConstraintLayout.LayoutParams params;
    boolean Issend = true;

    Context context;

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

       /* check create new acc
       ReplaceFragment(fg_signup_new_account);*/

        ReplaceFragment(fg_signup_enterphone);
        mAuth = FirebaseAuth.getInstance();

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_next = (Button) findViewById(R.id.btn_next);
        ln_logo = (LinearLayout) findViewById(R.id.ln_logo);
        context = this;

         params = ( ConstraintLayout.LayoutParams) ln_logo.getLayoutParams();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_next.getText().toString().equals(getResources().getString(R.string.Sent_OTP)))
                {
                    if (fg_signup_enterphone.edt_phone.getError() == null)
                    {
                            DBControllerUser controllerUser = new DBControllerUser();
                        if (fg_signup_enterphone.edt_phone.getText().toString().length() == 9) phone = "+84" + fg_signup_enterphone.edt_phone.getText().toString();
                        if (!controllerUser.checkExistPhone(phone))
                        {
                            ReplaceFragment(fg_signup_enterotp);
                            btn_next.setText(R.string.Continue);
                            sendVerificationCode(phone);
                        }
                        else
                        {
                            fg_signup_enterphone.edt_phone.setError(getResources().getString(R.string.error_duplicate_phone_number));
                        }
                    }
                }
                else
                {
                    edt_otp = fg_signup_enterotp.edt_otp;
                    if (btn_next.getText().toString().equals(getResources().getString(R.string.Continue)))
                    {
                        if( fg_signup_enterotp.edt_otp.getError() == null)
                        {
                            verifyCode(edt_otp.getText().toString());
                        }
                    }
                    else
                    {
                        if (CheckErrorUserInfo() == -1)
                        {
                            Toast.makeText(context , getResources().getString(R.string.error_not_enough_info), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if (CheckErrorUserInfo() == 0)
                            {
                                Toast.makeText(context , getResources().getString(R.string.error_incorrect_info), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                DBControllerUser controllerUser = new DBControllerUser();

                                if (controllerUser.checkExistUsername(fg_signup_new_account.edt_usename.getText().toString()))
                                {
                                    fg_signup_new_account.edt_usename.setError(getResources().getString(R.string.error_existing_username));
                                }
                                else
                                {
                                    int day = Integer.parseInt(fg_signup_new_account.edt_birth.getText().toString().substring(0, 2));
                                    int month = Integer.parseInt(fg_signup_new_account.edt_birth.getText().toString().substring(3, 5)) - 1;
                                    int year = Integer.parseInt(fg_signup_new_account.edt_birth.getText().toString().substring(6, 10));

                                    controllerUser.insertUser(fg_signup_new_account.edt_usename.getText().toString(), fg_signup_new_account.tit_pass.getText().toString(),phone, fg_signup_new_account.edt_name.getText().toString(),fg_signup_new_account.acs_sex.getSelectedItemPosition(),year, month,day);
                                    Toast.makeText(context, getResources().getString(R.string.annouce_creat_acc_succsess),Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

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
                 /*   params.setMargins(0, 116, 0, 0);
                    ln_logo.setLayoutParams(params);*/
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

        if (fg_signup_enterotp.tv_sent_otp != null && phone != null)
        {
            fg_signup_enterotp.tv_sent_otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendVerificationCode(phone);
                    fg_signup_enterotp.tv_sent_otp.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private  void ReplaceFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            FragmentTransaction fg_transaction = getSupportFragmentManager().beginTransaction();
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

                            params.setMargins(0, 30, 0, 0);
                            ln_logo.setLayoutParams(params);

                            ReplaceFragment(fg_signup_new_account);
                            btn_next.setText(R.string.Sign_Up);

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(signup.this, getResources().getString(R.string.error_wrong_OTP), Toast.LENGTH_LONG).show();
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
                    fg_signup_enterotp.tv_coundown.setText( "(" + millisUntilFinished / 1000 + ")");
                }
                public void onFinish() {
                    fg_signup_enterotp.tv_coundown.setText( "(0)");
                    fg_signup_enterotp.tv_sent_otp.setVisibility(View.VISIBLE);
                    Issend = false;

                    fg_signup_enterotp.tv_sent_otp.setOnClickListener(v -> {
                        Issend = true;
                        sendVerificationCode(phone);
                        fg_signup_enterotp.tv_sent_otp.setVisibility(View.INVISIBLE);
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
            Toast.makeText(signup.this,getResources().getString(R.string.error_connect_firebar), Toast.LENGTH_LONG).show();
            finish();
        }
    };

    // below method is use to verify code from Firebase.
    private Boolean verifyCode(String code) {
        // below line is used for getting getting credentials from our verification id and code.
        Pattern pattern = Pattern.compile(".*\\D.*");
        Matcher matcher = pattern.matcher(code);
        if ( code.length() != 6 || matcher.find() )
        {
            return false;
        }
        else
        {
            if (count == 5)
            {
                Toast.makeText(this, getResources().getString(R.string.error_5_otp), Toast.LENGTH_LONG).show();
                count = 0;
                finish();
            }
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            // after getting credential we are calling sign in method.

            if ( Issend && code != credential.getSmsCode())
            {
                ++count;
                Toast.makeText(this, getResources().getString(R.string.error_wrong_OTP), Toast.LENGTH_LONG).show();
                return false;
            }
            signInWithCredential(credential);
        }
    return true;
    }

    private int CheckErrorUserInfo()
    {
        if (fg_signup_new_account.edt_usename.getText().toString().equals("")|| (fg_signup_new_account.edt_name.getText().toString().equals("")) || fg_signup_new_account.edt_birth.getText().toString().equals("")|| fg_signup_new_account.tit_pass.getText().toString().equals("") || fg_signup_new_account.tit_define_pass.getText().toString().equals(""))
        {
            return -1;
        }
        if (fg_signup_new_account.edt_usename.getError() != null || fg_signup_new_account.edt_birth.getError() != null || fg_signup_new_account.til_pass.getError() != null || fg_signup_new_account.til_confirm_pass.getError() != null || fg_signup_new_account.edt_name.getError() != null)
        {
            return 0;
        }
        return 1;
    }

}
