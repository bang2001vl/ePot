package exam.nlb2t.epot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dragnell.android.ButtonNumberNotification;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import exam.nlb2t.epot.Activities.SplashActivity;
import exam.nlb2t.epot.Database.DBControllerNotification;
import exam.nlb2t.epot.Fragments.LoadingDialogFragment;
import exam.nlb2t.epot.ProductDetail.ProductBuyInfo;
import exam.nlb2t.epot.Fragments.CartFragment;
import exam.nlb2t.epot.Fragments.CartFragment_Old;
import exam.nlb2t.epot.Fragments.HomepageFragment;
import exam.nlb2t.epot.Fragments.NotificationFragment;
import exam.nlb2t.epot.Fragments.PersonFragment;
import exam.nlb2t.epot.MyShop.ShopFragment;
import exam.nlb2t.epot.Views.Card_ItemView_New;
import exam.nlb2t.epot.Views.LoadingView;
import exam.nlb2t.epot.Views.Login.LoginScreen;
import exam.nlb2t.epot.databinding.ActivityMainBinding;
import exam.nlb2t.epot.databinding.TabIconLayoutBinding;
import exam.nlb2t.epot.singleton.Authenticator;
import exam.nlb2t.epot.singleton.CartDataController;
import exam.nlb2t.epot.singleton.Helper;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    Thread notiThread;
    boolean isRunning = false;

    int color;
    int color2;

    int countNoti;
    int userID;

    public MainActivity(){
        userID = Authenticator.getCurrentUser().id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("MY_TAG", "b");
                MainFragmentAdapter adapter = createAdapter();
                binding.viewPaperMain.setAdapter(adapter);

                // Code-line of GOD. I spend 3 hours to find it on Stack Overflow
                // App would be terrible-lagging without it
                binding.viewPaperMain.setOffscreenPageLimit(5);

                binding.viewPaperMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {           }

                    @Override
                    public void onPageSelected(int position) {
                        if (position == 1) {
                            onOpenTabCart((CartFragment_Old) adapter.getItem(position));
                        }

                        if (position == 2) {
                            onOpenTabMyShop((ShopFragment) adapter.getItem(position));
                        } else {
                            ((ShopFragment) adapter.getItem(2)).releaseAdapter();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) { }
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
                        TabIconLayoutBinding iconLayoutBinding = (TabIconLayoutBinding) tab.getTag();
                        iconLayoutBinding.iconImg.getDrawable().setTint(color);
                        iconLayoutBinding.txtName.setTextColor(color);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        TabIconLayoutBinding iconLayoutBinding = (TabIconLayoutBinding) tab.getTag();
                        iconLayoutBinding.iconImg.getDrawable().setTint(color2);
                        iconLayoutBinding.txtName.setTextColor(color2);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                binding.tabLayout.setSelectedTabIndicatorColor(color);

                TabIconLayoutBinding iconLayoutBinding = (TabIconLayoutBinding) binding.tabLayout.getTabAt(0).getTag();
                iconLayoutBinding.iconImg.getDrawable().setTint(color);
                iconLayoutBinding.txtName.setTextColor(color);
            }
        });
        Log.d("MY_TAG", "a");
    }

    void onOpenTabCart(CartFragment_Old fragmentOld)
    {
        if(fragmentOld.getContext() == null){return;}
        List<Pair<Integer, Integer>> list = CartDataController.getAllData(fragmentOld.getContext());
        if(list.size() > 0) {
            fragmentOld.requestLoadData(list);
        }
    }

    void onOpenTabMyShop(ShopFragment fragment)
    {
        //fragment.reload();
    }

    public MainFragmentAdapter createAdapter()
    {
        /*Fragment[] fragments = new Fragment[]{
            new HomepageFragment(),
            new EmptyBagFragment(), new EmptyBagFragment(), new EmptyBagFragment(), new EmptyBagFragment()
        };*/

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

        int[] titles = new int[]{
                R.string.menu_home_page,
                R.string.menu_cart,
                R.string.menu_shop,
                R.string.menu_notification,
                R.string.menu_person
        };

        for(int i = 0; i<icons.length; i++){
            TabIconLayoutBinding iconLayoutBinding = TabIconLayoutBinding.inflate(getLayoutInflater());
            iconLayoutBinding.iconImg.setImageResource(icons[i]);
            iconLayoutBinding.txtName.setText(titles[i]);

            iconLayoutBinding.tvNumberRequest.setVisibility(View.GONE);

            tabLayout.getTabAt(i).setCustomView(iconLayoutBinding.getRoot());
            tabLayout.getTabAt(i).setTag(iconLayoutBinding);
        }
    }

    void startingCheckNotification(){
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(!isRunning) return;
                if(notiThread == null || !notiThread.isAlive()) {
                    notiThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            checkNotification();
                        }
                    });
                    notiThread.start();
                }
                // It already running, sol wait next loop
                mainHandler.postDelayed(this, 10000);
            }
        });
    }

    void checkNotification(){
        Log.d("MY_TAG", "Looper: Count unread notification");

        if(!Helper.checkConnectionToServer()){
            runOnUiThread(()->{
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Lỗi")
                        .setMessage("Không thể kết nối đến server")
                        .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.show();
            });
            isRunning = false;
            return;
        }

        DBControllerNotification db = new DBControllerNotification();
        int countUnreadNoti = db.countUnreadNoti(this.userID);
        db.closeConnection();
        if(MainActivity.this.binding != null) {
            MainActivity.this.runOnUiThread(() -> {
                if(countNoti != countUnreadNoti) {
                    countNoti = countUnreadNoti;
                    MainActivity.this.setNumberNotification(countNoti, 3);
                    if(this.binding != null && this.isRunning){
                        // Update notification fragment
                        MainFragmentAdapter adapter = (MainFragmentAdapter) binding.viewPaperMain.getAdapter();
                        if(adapter == null) return;
                        NotificationFragment fragment = (NotificationFragment) adapter.getItem(3);
                        fragment.reload();

                        // Update store's bill
                        ShopFragment shopFragment = (ShopFragment) adapter.getItem(2);
                        shopFragment.reloadBill_Inshipping();
                    }
                }
            });
        }
    }

    public void setNumberNotification(int num, int tabIndex){
        TabIconLayoutBinding iconLayoutBinding = (TabIconLayoutBinding) binding.tabLayout.getTabAt(3).getTag();
        if(num > 0){
            iconLayoutBinding.tvNumberRequest.setVisibility(View.VISIBLE);
            if(num < 100) {
                iconLayoutBinding.tvNumberRequest.setText(String.valueOf(num));
            }
            else {
                iconLayoutBinding.tvNumberRequest.setText("99+");
            }
        }
        else {
            iconLayoutBinding.tvNumberRequest.setVisibility(View.GONE);
        }
    }

    public void decreaseNumberNotification(){
        countNoti--;
        setNumberNotification(countNoti, 3);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(notiThread != null){
            Handler handler = new Handler();
            new Thread(()->checkNotification()).start();
            handler.postDelayed(()->{
                isRunning = true;
                startingCheckNotification();
            }, 10000);
        }
        else {
            isRunning = true;
            startingCheckNotification();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MY_TAG", "onDetroy");
    }
}
