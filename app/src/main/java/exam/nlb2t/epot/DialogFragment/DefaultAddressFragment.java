package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exam.nlb2t.epot.R;

public class DefaultAddressFragment extends DialogFragment {


    public DefaultAddressFragment() {
    }
    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }

    public static OrderFragment getInstance(){
        return new OrderFragment();
    }
    public static DefaultAddressFragment newInstance(String param1, String param2) {
        DefaultAddressFragment fragment = new DefaultAddressFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_default_address, container, false);
    }
}