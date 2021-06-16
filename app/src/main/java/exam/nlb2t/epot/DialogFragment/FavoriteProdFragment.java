package exam.nlb2t.epot.DialogFragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exam.nlb2t.epot.R;


public class FavoriteProdFragment extends DialogFragment {


    public FavoriteProdFragment() {
        // Required empty public constructor
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }

    public static FavoriteProdFragment newInstance(String param1, String param2) {
        FavoriteProdFragment fragment = new FavoriteProdFragment();
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
        return inflater.inflate(R.layout.fragment_favorite_prod, container, false);
    }
}