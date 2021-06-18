package exam.nlb2t.epot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exam.nlb2t.epot.Database.DBControllerUser;
import exam.nlb2t.epot.Views.forgotpassword;
import exam.nlb2t.epot.Views.home_shopping;
import exam.nlb2t.epot.Views.signup;
import exam.nlb2t.epot.singleton.Authenticator;


public class LoginScreen extends AppCompatActivity {

    CallbackManager callbackManager;

    private EditText et_username;
    private TextInputEditText tet_password;
    private TextView tv_forgotpass;
    private Button btn_login;
    private TextView tv_signup;
    private LoginButton lgb_login;
    private TextView tv_resent_otp;
    private SignInButton btn_signin_gg;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login_screen);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //for login with google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        et_username = (EditText) findViewById(R.id.et_usename);
        tet_password = (TextInputEditText) findViewById(R.id.tip_pass);
        tv_forgotpass = (TextView) findViewById(R.id.tv_forgotpass);
        tv_signup = (TextView) findViewById(R.id.tv_signup);
        btn_login = (Button) findViewById(R.id.btn_login);
        lgb_login = (LoginButton) findViewById(R.id.login_button);
        btn_signin_gg = (SignInButton) findViewById(R.id.sign_in_button);

        btn_signin_gg.setSize(SignInButton.SIZE_STANDARD);



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
                Intent intent = new Intent(LoginScreen.this, forgotpassword.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( et_username.getError() != null || tet_password.getError()!= null ) return;

                DBControllerUser controllerUser = new DBControllerUser();
                if (controllerUser.CheckUserLogin(et_username.getText().toString(), tet_password.getText().toString()))
                {
                    Authenticator.Login(et_username.getText().toString(), tet_password.getText().toString());

                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(v.getContext(), getResources().getString(R.string.error_wrong_username_pass), Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, signup.class);
                startActivity(intent);
            }
        });

        btn_signin_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 9);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        lgb_login.setReadPermissions("email");
        lgb_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Thông báo", "======Facebook login success======");
                Log.d("Thông báo", "Facebook Access Token: " + loginResult.getAccessToken().getToken());
                Toast.makeText(LoginScreen.this, "Login Facebook success.", Toast.LENGTH_SHORT).show();
                getFbInfo();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginScreen.this, "Login Facebook cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Thông báo", "======Facebook login error======");
                Log.e("Error", "Error: " + error.toString());
                Toast.makeText(LoginScreen.this, "Login Facebook error.", Toast.LENGTH_SHORT).show();
            }
        });
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
        /*updateUI(account);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
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
            }
            Intent intent = new Intent(LoginScreen.this, home_shopping.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();

            // Signed in successfully, show authenticated UI.
          /*  updateUI(account);*/
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Lỗi", "signInResult:failed code=" + e.getStatusCode());
            /*updateUI(null);*/
        }
    }

    private void getFbInfo() {
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(final JSONObject me, GraphResponse response) {
                            if (me != null) {
                                Log.i("Login: ", me.optString("name"));
                                Log.i("ID: ", me.optString("id"));
                                /*Log.i("Password: ", me.optString("email"));*/

                                Toast.makeText(LoginScreen.this, "Name: " + me.optString("name"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginScreen.this, "ID: " + me.optString("id"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginScreen.this, "Email: " + me.optString("email"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginScreen.this, home_shopping.class);
                                startActivity(intent);
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }
}
