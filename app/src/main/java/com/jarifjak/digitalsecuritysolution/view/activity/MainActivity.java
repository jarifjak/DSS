package com.jarifjak.digitalsecuritysolution.view.activity;

import android.content.Intent;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.dialog.CustomDialog;
import com.jarifjak.digitalsecuritysolution.listener.FragmentListener;
import com.jarifjak.digitalsecuritysolution.model.DialogExtra;
import com.jarifjak.digitalsecuritysolution.utility.Constants;
import com.jarifjak.digitalsecuritysolution.utility.SharedPrefs;
import com.jarifjak.digitalsecuritysolution.view.fragment.BranchFragment;
import com.jarifjak.digitalsecuritysolution.view.fragment.HomeFragment;
import com.jarifjak.digitalsecuritysolution.view.fragment.OthersFragment;
import com.jarifjak.digitalsecuritysolution.viewmodel.MainViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentListener, AHBottomNavigation.OnTabSelectedListener, NavigationView.OnNavigationItemSelectedListener,
        CustomDialog.DialogListener {

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
    private DialogFragment dialog;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupDrawerAndToolbar();

        dialog = new DialogFragment();  // for avoiding null exception

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

    private void showCustomDialog(int dialogType, DialogExtra dialogExtra) {

        if (dialog.isVisible()) {

            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag(Constants.DIALOG_TAG);

        if (prevFragment != null) {

            ft.remove(prevFragment);
        }

        ft.addToBackStack(null);

        if (dialogType == Constants.DIALOG_ONE_BUTTON) {

            dialog = CustomDialog.newInstance(Constants.DIALOG_ONE_BUTTON, 3, new Gson().toJson(dialogExtra));

        } else {

            dialog = CustomDialog.newInstance(Constants.DIALOG_TWO_BUTTON, dialogExtra.getTitle().equals("Exit") ? 5 : 3, new Gson().toJson(dialogExtra));

        }

        dialog.show(ft, Constants.DIALOG_TAG);
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

            showCustomDialog(Constants.DIALOG_ONE_BUTTON, new DialogExtra("About", "This Application is created for maintaining " +
                                                                                                    "employee data and influence better control over them.\n\nAuthorized by DSS"));

        } else if (item.getItemId() == R.id.nav_developer_info) {

            showCustomDialog(Constants.DIALOG_ONE_BUTTON, new DialogExtra("Developer Info","This application is developed by\n\nJarif Alam Khan\n" +
                                                                                                            "Software Engineer\n\n" +
                                                                                                             "Email : jarifjak@gmail.com"));

        } else if (item.getItemId() == R.id.nav_logout) {

            showCustomDialog(Constants.DIALOG_TWO_BUTTON, new DialogExtra("Logout", "Do you want to logout?"));

        } else if (item.getItemId() == R.id.nav_exit) {

            showCustomDialog(Constants.DIALOG_TWO_BUTTON, new DialogExtra("Exit", "Do you want to exit?"));

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfirmClick(int flag) {

        if (flag == 3) {

            viewModel = new ViewModelProvider(MainActivity.this).get(MainViewModel.class);

            viewModel.deleteAllEmployees();
            viewModel.deleteAllBranches();
            prefs.setLoggedIn(false);

            finishAffinity();
            startActivity(new Intent(MainActivity.this, StartingActivity.class));

        } else if (flag == 5) {

            finishAndRemoveTask();
        }
    }
}