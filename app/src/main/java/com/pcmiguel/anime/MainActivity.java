package com.pcmiguel.anime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private Fragment fragment;

    public static boolean ANIME = true;
    public static String HOST_URL = "https://animeapiplus.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateApp();

        navigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new SearchFragment()).commit();
        navigationView.setSelectedItemId(R.id.nav_search);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                fragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_search:
                        item.setChecked(true);
                        fragment = new SearchFragment();
                        break;

                    case R.id.nav_list:
                        item.setChecked(true);
                        fragment = new ListFragment();
                        break;

                    case R.id.nav_profile:
                        item.setChecked(true);
                        fragment = new ProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();

                return false;
            }
        });

    }

    private void updateApp() {

        String url = "https://github.com/pcmiguel1/animeapp/raw/master/app-debug.apk";

        DownloadApk downloadApk = new DownloadApk(MainActivity.this);

        downloadApk.startDownloadingApk(url);
    }
}