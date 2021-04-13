package exam.nlb2t.epot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dragnell.android.ButtonNumberNotification;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import exam.nlb2t.epot.Activities.CartActivity;
import exam.nlb2t.epot.ClassInformation.ProductBuyInfo;
import exam.nlb2t.epot.ClassInformation.ProductBuyInfoParcel;

public class MainActivity extends AppCompatActivity {

    ButtonNumberNotification icon_card;
    ButtonNumberNotification icon_notification;

    List<ProductBuyInfo> buyInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView main_bottom_navigation = findViewById(R.id.bottom_navigation_menu);
        main_bottom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        icon_card = findViewById(R.id.icon_card);
        icon_card.setOnClickListener(onClickIconCard);

        icon_notification = findViewById(R.id.icon_notification);
        icon_notification.setOnClickListener(onClickIconNotification);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            Resources res = getResources();
            String itemTitle = item.getTitle().toString();

            if (Objects.equals(itemTitle, res.getString(R.string.menu_home_page))) {
                fragment = new fragment_ProItem_Container();
            } else if (Objects.equals(itemTitle, res.getString(R.string.menu_person))) {

                // TODO: Not have user-fragment yet
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment);
            } else {
                Toast.makeText(MainActivity.this, "Unavailable option", Toast.LENGTH_LONG).show();
            }

            return fragment != null;
        }
    };

    View.OnClickListener onClickIconCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<ProductBuyInfoParcel> parcels = new ArrayList<>(buyInfoList.size());
            for(ProductBuyInfo buyInfo: buyInfoList)
            {
                ProductBuyInfoParcel parcel = new ProductBuyInfoParcel(buyInfo);
                parcels.add(parcel);
            }

            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra(CartActivity.NAME_PARCEL, parcels);
            startActivity(intent);
        }
    };

    View.OnClickListener onClickIconNotification = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: fragment or activity?
        }
    };
}