package exam.nlb2t.epot.Views.Login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.DialogFragment.LoginLoadingDialog;
import exam.nlb2t.epot.MainActivity;
import exam.nlb2t.epot.R;
import exam.nlb2t.epot.Views.Error_toast;
import exam.nlb2t.epot.Views.Forgotpass.forgotpassword;
import exam.nlb2t.epot.Views.Registration.signup;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.Helper;


public class LoginScreen extends AppCompatActivity {

    CallbackManager callbackManager;

    private EditText et_username;
    private TextInputEditText tet_password;
    private TextView tv_forgotpass;
    private Button btn_login;
    private TextView tv_signup;
    private TextView tv_resent_otp;
    private ImageButton btn_signin_gg;
    private SignInButton btn_signin;

    FirebaseUser currentusser;
    LinearLayout ln_gg;

    private FirebaseAuth mAuth;

    Context context;

    LoginLoadingDialog dialogLoading;

    public GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogLoading = new LoginLoadingDialog();

        context = this;
        setContentView(R.layout.activity_login_screen);

        AppEventsLogger.activateApp(this);

        // require infor from user
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        et_username = (EditText) findViewById(R.id.et_usename);
        tet_password = (TextInputEditText) findViewById(R.id.tip_pass);
        tv_forgotpass = (TextView) findViewById(R.id.tv_forgotpass);
        tv_signup = (TextView) findViewById(R.id.tv_signup);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signin_gg = (ImageButton) findViewById(R.id.sign_in_button);
        ln_gg = findViewById(R.id.ln_continue_gg);



        Pattern pattern = Pattern.compile("[\\p{P}\\p{S}]");

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher matcher = pattern.matcher(et_username.getText().toString());
                if( matcher.find())
                {
                    et_username.setError("Chỉ chứa kí tự a-z, A-Z, 0-9");
                }
                else
                {
                    et_username.setError(null);
                    if (et_username.getText().toString().length() > 50)
                    {
                        et_username.setError("Độ dài không quá 50 kí tự");
                    }
                    else
                    {
                        et_username.setError(null);
                    }
                }
            }
        });

        tet_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tet_password.getText().toString().length() > 50)
                {
                    tet_password.setError("Độ dài không quá 50 kí tự");
                }
                else
                {
                    tet_password.setError(null);
                }
            }
        });

        tv_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkConnection()) return;
                Intent intent = new Intent(LoginScreen.this, forgotpassword.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( et_username.getError() != null || tet_password.getError()!= null ) return;
                if(!checkConnection()) return;
                LoginLoadingDialog dialog = new LoginLoadingDialog();
                new Thread(()->{
                    DBControllerUser controllerUser = new DBControllerUser();
                    if (controllerUser.CheckUserLogin(et_username.getText().toString(), tet_password.getText().toString()))
                    {
                        //runOnUiThread(()->dialogLoading.show(getSupportFragmentManager(), "load"));
                        Authenticator.Login(et_username.getText().toString(), tet_password.getText().toString());
                        controllerUser.closeConnection();
                        Authenticator.saveLoginData(LoginScreen.this, et_username.getText().toString(), tet_password.getText().toString());

                        runOnUiThread(()->{
                            loadMainActivity();
                            /*new Handler(getMainLooper()).postDelayed(()->{
                                if(dialogLoading.isCancelable()){
                                    dialogLoading.dismiss();
                                }
                            }, 7000);*/
                        });
                    }
                    else
                    {
                        controllerUser.closeConnection();
                        runOnUiThread(()->{
                            Error_toast.show(context,getResources().getString(R.string.error_wrong_username_pass), true);
                        });
                    }
                }).start();

            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkConnection()) return;
                Intent intent = new Intent(LoginScreen.this, signup.class);
                startActivity(intent);
            }
        });

        ln_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkConnection()) return;
                signIn();
            }
        });

        callbackManager = CallbackManager.Factory.create();
    }

    private void loadMainActivity() {
        Intent intent = new Intent(LoginScreen.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {

        super.onStart();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }
        signOut();
        /*updateUI(account);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Authenticator.HasSavedData(this)){
            if(!checkConnection()) return;
            if(Authenticator.LoginWithSavedData(this))
            {
                loadMainActivity();
            }
        }
    }

    boolean checkConnection(){
        if(!Helper.checkConnectionToServer()){
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Lỗi")
                        .setMessage("Không thể kết nối đến server")
                        .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).create();
                dialog.show();
                return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Thành công", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Lỗi đăng nhập gg", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Thành công", "signInWithCredential:success");
                            }
                         else {
                            // If sign in fails, display a message to the user.
                            Log.w("Lỗi", "signInWithCredential:failure", task.getException());
                        }
                    }
           });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                DBControllerUser controllerUser = new DBControllerUser();
                int id = controllerUser.findUserID_ByEmail(personEmail);
                controllerUser.closeConnection();
                if (id > 0)
                {
                    Authenticator.LoginGG(id);
                    finish();
                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                }
                else
                {
                    assert personEmail != null;
                    String[] t = personEmail.split("@", 2);
                    Intent intent = new Intent(LoginScreen.this, signup.class);
                    intent.putExtra("Google", 1);
                    intent.putExtra("Personname",personFamilyName + " " + personGivenName);
                    intent.putExtra("Personusername", t[0].replaceAll("[^a-zA-Z0-9]", ""));
                    intent.putExtra("Personemail",personEmail);
                    intent.putExtra("pertionphoto", personPhoto);
                    startActivity(intent);
                }
            }


            // Signed in successfully, show authenticated UI.
          /*  updateUI(account);*/
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Lỗi", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    // signin acct gg
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 9);
        currentusser = mAuth.getCurrentUser();
    }
    // signOut acct gg
    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) { }
                });
    }
    // disconnect acct gg with Epot, delete all infor of user
    public void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }



//login zalo

}
