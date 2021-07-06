package exam.nlb2t.epot.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import exam.nlb2t.epot.DialogFragment.LoginLoadingDialog;
import exam.nlb2t.epot.MainActivity;
import exam.nlb2t.epot.Views.Login.LoginScreen;
import exam.nlb2t.epot.databinding.DialogGifAndTextBinding;

public class SplashActivity extends AppCompatActivity {
    DialogGifAndTextBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogGifAndTextBinding.inflate(getLayoutInflater());
        Log.d("MY_TAG", "On show login");
        setContentView(binding.getRoot());
        new Handler().postDelayed(this::finish, 7000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}
