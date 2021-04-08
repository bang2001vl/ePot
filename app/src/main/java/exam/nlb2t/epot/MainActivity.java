package exam.nlb2t.epot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView main_bottom_navigation = findViewById(R.id.bottom_navigation_menu);
        main_bottom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            Resources res = getResources();
            String itemTitle = item.getTitle().toString();

            if (itemTitle == res.getString(R.string.menu_home_page)) {
                fragment = new fragment_ProItem_Container();
            } else if (itemTitle == res.getString(R.string.menu_person)) {

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

}