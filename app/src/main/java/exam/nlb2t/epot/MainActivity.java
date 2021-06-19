package exam.nlb2t.epot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dragnell.android.ButtonNumberNotification;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.Fragments.CartFragment;
import exam.nlb2t.epot.Fragments.CartFragment_Old;
import exam.nlb2t.epot.Fragments.HomepageFragment;
import exam.nlb2t.epot.Fragments.NotificationFragment;
import exam.nlb2t.epot.Fragments.PersonFragment;
import exam.nlb2t.epot.MyShop.ShopFragment;
import exam.nlb2t.epot.Views.Card_ItemView_New;
import exam.nlb2t.epot.Views.LoadingView;
import exam.nlb2t.epot.databinding.ActivityMainBinding;
import exam.nlb2t.epot.singleton.CartDataController;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    ButtonNumberNotification icon_card;
    ButtonNumberNotification icon_notification;

    List<ProductBuyInfo> buyInfoList;

    int color;
    int color2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainFragmentAdapter adapter = createAdapter();
        binding.viewPaperMain.setAdapter(adapter);

        binding.tabLayout.setupWithViewPager(binding.viewPaperMain);
        setIcons(binding.tabLayout);

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        color = typedValue.data;
        color2 = getResources().getColor(R.color.drark_gray, getTheme());
        binding.tabLayout.setTabTextColors(Color.BLACK, color);
        binding.tabLayout.setSelectedTabIndicatorColor(color);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setTint(color);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setTint(color2);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        loadInBackground();
    }

    void loadInBackground()
    {
        setContentView(new LoadingView(this));
        Handler handler = new Handler();
        Runnable runnable = () -> {
            long start = System.currentTimeMillis();
            binding = ActivityMainBinding.inflate(getLayoutInflater());

            MainFragmentAdapter adapter = createAdapter();
            binding.viewPaperMain.setAdapter(adapter);

            // Code-line of GOD. I spend 3 hours to find it on Stack Overflow
            // App would be terrible-lagging without it
            binding.viewPaperMain.setOffscreenPageLimit(5);

            binding.viewPaperMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (position){
                        case 1:
                            onOpenTabCart((CartFragment_Old) adapter.getItem(position));
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            binding.tabLayout.setupWithViewPager(binding.viewPaperMain);
            setIcons(binding.tabLayout);

            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            color = typedValue.data;
            color2 = getResources().getColor(R.color.drark_gray, getTheme());

            binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tab.getIcon().setTint(color);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tab.getIcon().setTint(color2);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            binding.tabLayout.setTabTextColors(Color.BLACK, color);
            binding.tabLayout.setSelectedTabIndicatorColor(color);
            binding.tabLayout.getTabAt(0).getIcon().setTint(color);

            long delayInTask = System.currentTimeMillis() - start;
            if(delayInTask < 2000)
            {
                try{
                    Thread.sleep(2000 - delayInTask);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.post(() -> setContentView(binding.getRoot()));
        };

        new Thread(runnable).start();
    }

    void onOpenTabCart(CartFragment_Old fragmentOld)
    {
        if(fragmentOld.getContext() == null){return;}
        List<Pair<Integer, Integer>> list = CartDataController.getAllData(fragmentOld.getContext());
        if(list.size() > 0) {
            fragmentOld.requestLoadData(list);
        }
    }

    public static List<Pair<Integer, Integer>> cartData = new ArrayList<>();
    public MainFragmentAdapter createAdapter()
    {
        CartFragment_Old cartFragment = new CartFragment_Old(){
           /* @Override
            public void onItemDeleted(ProductBuyInfo productBuyInfo) {
                super.onItemDeleted(productBuyInfo);
                if(getContext() == null) return;
                CartDataController.removeProduct(getContext(), productBuyInfo.product.id);
            }

            @Override
            public void onItemAmountChanged(ProductBuyInfo productBuyInfo) {
                super.onItemAmountChanged(productBuyInfo);
                if(getContext() == null) return;
                CartDataController.setProduct(getContext(), productBuyInfo.product.id, productBuyInfo.Amount);
            }*/
/*
            @Override
            public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                if(getContext() == null){return;}
                List<Pair<Integer, Integer>> list = CartDataController.getAllData(getContext());
                ProductBuyInfo[] arr = new ProductBuyInfo[list.size()];
                for(int i = 0; i<arr.length; i++)
                {
                    arr[i] = new ProductBuyInfo(list.get(i).first, list.get(i).second);
                }
                setData(arr);
            }*/
        };

        Fragment[] fragments = new Fragment[]{
                new HomepageFragment(),
                new CartFragment_Old(),
                new ShopFragment(),
                new NotificationFragment(),
                new PersonFragment(this)
        };

        CartFragment_Old fragmentOld = (CartFragment_Old) fragments[1];
        fragmentOld.setOnItemDeleted(view -> {
            Card_ItemView_New item = (Card_ItemView_New)view;
            ProductBuyInfo productBuyInfo = (ProductBuyInfo)item.Tag;
            if(view.getContext() == null) return;
            CartDataController.removeProduct(view.getContext(), productBuyInfo.product.id);
        });
        fragmentOld.setOnItemAmountChanged(view -> {
            Card_ItemView_New item = (Card_ItemView_New)view;
            ProductBuyInfo productBuyInfo = (ProductBuyInfo)item.Tag;
            if(view.getContext() == null) return;
            CartDataController.setProduct(view.getContext(), productBuyInfo.product.id, productBuyInfo.Amount);
        });

        Resources resources = getResources();
        String[] titles = new String[]{
                resources.getString(R.string.menu_home_page),
                resources.getString(R.string.menu_cart),
                resources.getString(R.string.menu_shop),
                resources.getString(R.string.menu_notification),
                resources.getString(R.string.menu_person)
        };
        return new MainFragmentAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        , fragments, titles);
    }

    public void setIcons(TabLayout tabLayout)
    {
        int[] icons = new int[]{
                R.drawable.ic_baseline_home_24,
                R.drawable.ic_baseline_local_grocery_store_24,
                R.drawable.ic_icon_kho,
                R.drawable.ic_baseline_notifications_24,
                R.drawable.ic_baseline_person_24
        };
        for(int i = 0; i<icons.length; i++){
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            Resources res = getResources();
            String itemTitle = item.getTitle().toString();
            int id = item.getItemId();
            if (id == R.id.menu_home_page) {
                fragment = new HomepageFragment();
            }
            else if (id == R.id.menu_personal) {

                fragment = new PersonFragment(MainActivity.this);
            }
            else if(id == R.id.menu_cart)
            {
                fragment = new CartFragment();
            }
            else if(id == R.id.menu_shop)
            {
                fragment = new ShopFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            } else {
                Toast.makeText(MainActivity.this, "Unavailable option", Toast.LENGTH_LONG).show();
            }

            return fragment != null;
        }
    };

}
