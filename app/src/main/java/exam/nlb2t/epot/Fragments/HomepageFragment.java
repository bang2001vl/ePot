package exam.nlb2t.epot.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import exam.nlb2t.epot.R;
import exam.nlb2t.epot.fragment_ProItem_Container;

public class HomepageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        /*View fragment_container = view.findViewById(R.id.fragment_container);
        fragment_ProItem_Container fragment = new fragment_ProItem_Container();
        if(this.getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(fragment_container.getId(), fragment).commit();
        }*/
        return view;
    }
}
