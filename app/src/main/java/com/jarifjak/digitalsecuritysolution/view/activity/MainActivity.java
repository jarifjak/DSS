package com.jarifjak.digitalsecuritysolution.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.listener.FragmentListener;
import com.jarifjak.digitalsecuritysolution.utility.SharedPrefs;
import com.jarifjak.digitalsecuritysolution.view.fragment.HomeFragment;
import com.jarifjak.digitalsecuritysolution.view.fragment.OthersFragment;
import com.jarifjak.digitalsecuritysolution.viewmodel.MainViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentListener, AHBottomNavigation.OnTabSelectedListener {

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    private boolean doubleBackPressed = false;
    private SharedPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialize();
    }

    private void loadFragment(int position) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment fragment = new Fragment();

        if (position == 0) {

            fragment = HomeFragment.newInstance();

        } else if (position == 1) {

            fragment = OthersFragment.newInstance();

        }

        transaction.replace(R.id.fragment_container, fragment).commit();
    }

    private void initialize() {

        prefs = new SharedPrefs(this);

        loadFragment(0);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Others", R.drawable.ic_others);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);

        bottomNavigation.setOnTabSelectedListener(this);

        bottomNavigation.setAccentColor(Color.parseColor("#4D4D87"));
        bottomNavigation.setInactiveColor(Color.parseColor("#858699"));
    }

    @Override
    public void onBackPressed() {

        if (doubleBackPressed) {

            finishAndRemoveTask();
            return;
        }

        Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        doubleBackPressed = true;

        new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
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
}