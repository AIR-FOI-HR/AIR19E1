package com.cbd.teammate;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cbd.maps.LocationProvider;
import com.cbd.teammate.fragments.MyActivitiesFragment;
import com.cbd.teammate.fragments.NearbyFragment;
import com.cbd.teammate.fragments.ProfileFragment;
import com.cbd.teammate.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SignedActivity extends AppCompatActivity {

    private final static int ALL_PERMISSIONS_RESULT = 101;
    public LocationProvider lp;
    private TextView nameofuser;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "This app requires location permissions to be granted",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed);

        lp = new LocationProvider();
        lp.setup(this, this);

        BottomNavigationView navbar = findViewById(R.id.bottom_navigation);
        createNavigationListener(navbar);

        navbar.setSelectedItemId(R.id.nav_near);
    }

    private void createNavigationListener(BottomNavigationView navbar) {
        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selected = null;

                switch (menuItem.getItemId()) {
                    case R.id.nav_near:
                        selected = new NearbyFragment(lp);
                        break;
                    case R.id.nav_profile:
                        selected = new ProfileFragment(lp);
                        break;
                    case R.id.nav_search:
                        selected = new SearchFragment(lp);
                        break;
                    case R.id.nav_mine:
                        selected = new MyActivitiesFragment(getSupportFragmentManager());
                        break;
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_above_nav, selected).commit();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        lp.resumeLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();

        lp.pauseLocationUpdates();
    }
}
