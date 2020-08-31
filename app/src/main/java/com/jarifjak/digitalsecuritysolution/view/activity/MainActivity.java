package com.jarifjak.digitalsecuritysolution.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.navigation.NavigationView;
import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.listener.FragmentListener;
import com.jarifjak.digitalsecuritysolution.utility.SharedPrefs;
import com.jarifjak.digitalsecuritysolution.view.fragment.BranchFragment;
import com.jarifjak.digitalsecuritysolution.view.fragment.HomeFragment;
import com.jarifjak.digitalsecuritysolution.view.fragment.OthersFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentListener, AHBottomNavigation.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    private boolean doubleBackPressed = false;
    private SharedPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupDrawerAndToolbar();

        initialize();
    }

    private void setupDrawerAndToolbar() {

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

    }

    private void loadFragment(int position) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment fragment = new Fragment();

        if (position == 0) {

            fragment = HomeFragment.newInstance();

        } else if (position == 1) {

            fragment = BranchFragment.newInstance();

        } else if (position == 2) {

            fragment = OthersFragment.newInstance();

        }

        transaction.replace(R.id.fragment_container, fragment).commit();
    }

    private void initialize() {

        prefs = new SharedPrefs(this);

        loadFragment(0);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Branch", R.drawable.ic_branch_nav);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Others", R.drawable.ic_others);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setOnTabSelectedListener(this);

        bottomNavigation.setAccentColor(Color.parseColor("#4D4D87"));
        bottomNavigation.setInactiveColor(Color.parseColor("#858699"));
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else {

            if (doubleBackPressed) {

                finishAndRemoveTask();
                return;
            }

            Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            doubleBackPressed = true;

            new Handler().postDelayed(() -> doubleBackPressed = false, 2000);

        }
    }


    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {

        loadFragment(position);

        return true;
    }

    @Override
    public SharedPrefs getPrefs() {

        return prefs;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_about_app) {

            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.nav_developer_info) {

            Toast.makeText(this, "Developer Info", Toast.LENGTH_SHORT).show();

        }  else if (item.getItemId() == R.id.nav_logout) {

            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();

        }   else if (item.getItemId() == R.id.nav_exit) {

            Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}