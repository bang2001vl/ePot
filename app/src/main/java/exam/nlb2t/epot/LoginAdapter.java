package exam.nlb2t.epot;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import exam.nlb2t.epot.singleton.Helper;

public class LoginAdapter extends FragmentStatePagerAdapter {
    Helper.OnSuccessListener onSignUpSuccessListener;
    Helper.OnSuccessListener onLoginSuccessListener;

    public void setOnSignUpSuccessListener(Helper.OnSuccessListener onSignUpSuccessListener) {
        this.onSignUpSuccessListener = onSignUpSuccessListener;
    }

    public void setOnLoginSuccessListener(Helper.OnSuccessListener onLoginSuccessListener) {
        this.onLoginSuccessListener = onLoginSuccessListener;
    }

    public LoginAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public int getCount() {
        return 2;
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LoginTab loginTab = new LoginTab();
                loginTab.setOnLoginSuccessListener(onLoginSuccessListener);
                return loginTab;
            case 1:
                SignupTab signupTab = new SignupTab();
                signupTab.setOnSignUpSuccessListener(onSignUpSuccessListener);
                return signupTab;
            default:
                return new LoginTab();

        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position) {
            case 0:
                title="Login";
                break;
            case 1:
                title="Sign Up";
                break;
        }
        return title;
    }

}

