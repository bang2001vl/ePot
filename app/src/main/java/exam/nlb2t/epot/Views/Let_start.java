package exam.nlb2t.epot.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import exam.nlb2t.epot.R;

public class Let_start extends AppCompatActivity {

    private Button btn_login;
    private Button btn_back;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_let_start);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_back = (Button) findViewById(R.id.btn_back);
        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser().isEmailVerified())
                {
                    Intent intent = new Intent(Let_start.this, home_shopping.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(v.getContext() , "Có vẻ bạn chưa xác nhận mail, vui lòng xác nhận và thử lại!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}