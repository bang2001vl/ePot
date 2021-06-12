package exam.nlb2t.epot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.ViewGroup;
import android.widget.Toast;

import com.dragnell.android.ButtonNumberNotification;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.Fragments.CartFragment;
import exam.nlb2t.epot.Fragments.HomepageFragment;
import exam.nlb2t.epot.Fragments.LoadingDialogFragment;
import exam.nlb2t.epot.Fragments.NotificationFragment;
import exam.nlb2t.epot.Fragments.PersonFragment;
import exam.nlb2t.epot.MyShop.ShopFragment;
import exam.nlb2t.epot.Views.LoadingView;
import exam.nlb2t.epot.databinding.ActivityMainBinding;

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
            });

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

    public MainFragmentAdapter createAdapter()
    {
        Fragment[] fragments = new Fragment[]{
                new HomepageFragment(),
                new CartFragment(),
                new ShopFragment(),
                new NotificationFragment(),
                new PersonFragment()
        };
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

                fragment = new PersonFragment();
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
