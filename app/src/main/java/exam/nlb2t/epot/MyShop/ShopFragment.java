package exam.nlb2t.epot.MyShop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import exam.nlb2t.epot.Views.LoadingView;
import exam.nlb2t.epot.databinding.FragmentCartThachBinding;
import exam.nlb2t.epot.databinding.MyShopBinding;

public class ShopFragment extends Fragment {
    MyShopBinding binding;
    Shop_TabAdapter adapter;
    Thread reloadThread;

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

    public void reload()
    {
        showLoadingScreen();
        reloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                adapter = new Shop_TabAdapter(getChildFragmentManager()
                        , FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                if(getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.myShopViewPaper.setAdapter(adapter);
                            binding.myShopViewPaper.setOffscreenPageLimit(3);
                            binding.myShopTabLayout.setupWithViewPager(binding.myShopViewPaper);
                            closeLoadingScreen();
                        }
                    });
                }
            }
        });

        reloadThread.start();
    }

    public void showLoadingScreen()
    {
        View view = new LoadingView(getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        binding.getRoot().addView(view, binding.getRoot().getChildCount());
    }

    public void closeLoadingScreen()
    {
        binding.getRoot().removeViewAt(binding.getRoot().getChildCount() - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void setEventHandler()
    {
        //TODO : Write code here <Set all listener in here>
    }

    public void releaseAdapter()
    {
        if(reloadThread != null && reloadThread.isAlive())
        {
            try {
                reloadThread.join();
            } catch (InterruptedException e) {
                Log.e("MY_TAG", e.getMessage());
            }
        }
        adapter = null;
    }
}
