package exam.nlb2t.epot;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import exam.nlb2t.epot.Views.forgotpass_enter_phone;
import exam.nlb2t.epot.Views.home_shopping;
import exam.nlb2t.epot.Views.signup;

//for login with facebook

public class LoginScreen extends AppCompatActivity {

    CallbackManager callbackManager;

    private EditText et_username;
    private TextInputEditText tet_password;
    private TextView tv_forgotpass;
    private Button btn_login;
    private TextView tv_signup;
    private LoginButton lgb_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        et_username = (EditText) findViewById(R.id.et_usename);
        tet_password = (TextInputEditText) findViewById(R.id.tip_pass);
        tv_forgotpass = (TextView) findViewById(R.id.tv_forgotpass);
        tv_signup = (TextView) findViewById(R.id.tv_signup);
        btn_login = (Button) findViewById(R.id.btn_login);
        lgb_login = (LoginButton) findViewById(R.id.login_button);

        tv_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, forgotpass_enter_phone.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, home_shopping.class);
                startActivity(intent);
            }
        });
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, signup.class);
                startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
