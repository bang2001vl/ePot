package exam.nlb2t.epot.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import exam.nlb2t.epot.R;
import exam.nlb2t.epot.databinding.FragmentProfileBinding;
import exam.nlb2t.epot.databinding.HomeShoppingBinding;

public class PersonFragment extends Fragment {
    FragmentProfileBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        setEventHandler();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO : Write code here <Get data from database and set to view>
    }

    void setEventHandler()
    {
        //TODO : Write code here <Set all listener in here>
    }
}
