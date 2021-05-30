package exam.nlb2t.epot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.dragnell.android.ButtonNumberNotification;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.Fragments.CartFragment;
import exam.nlb2t.epot.Fragments.HomepageFragment;
import exam.nlb2t.epot.Fragments.PersonFragment;
import exam.nlb2t.epot.MyShop.ShopFragment;
import exam.nlb2t.epot.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    ButtonNumberNotification icon_card;
    ButtonNumberNotification icon_notification;

    List<ProductBuyInfo> buyInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView main_bottom_navigation = findViewById(R.id.bottom_navigation_menu);
        main_bottom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomepageFragment()).commit();
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