package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exam.nlb2t.epot.R;

public class SettingAccountFragment extends DialogFragment {

    public SettingAccountFragment() {
        // Required empty public constructor
    }
    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }
    public static OrderFragment getInstance(){
        return new OrderFragment();
    }
    public static SettingAccountFragment newInstance(String param1, String param2) {
        SettingAccountFragment fragment = new SettingAccountFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_account, container, false);
    }
}