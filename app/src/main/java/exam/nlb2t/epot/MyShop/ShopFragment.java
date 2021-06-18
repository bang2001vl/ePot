package exam.nlb2t.epot.MyShop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import exam.nlb2t.epot.databinding.FragmentCartThachBinding;
import exam.nlb2t.epot.databinding.MyShopBinding;

public class ShopFragment extends Fragment {
    MyShopBinding binding;
    Shop_TabAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyShopBinding.inflate(inflater, container, false);
        setEventHandler();

        adapter = new Shop_TabAdapter(getChildFragmentManager()
                , FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.myShopViewPaper.setAdapter(adapter);
        binding.myShopViewPaper.setOffscreenPageLimit(3);
        binding.myShopTabLayout.setupWithViewPager(binding.myShopViewPaper);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void setEventHandler()
    {
        //TODO : Write code here <Set all listener in here>
    }
}
