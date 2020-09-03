package com.jarifjak.digitalsecuritysolution.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.dialog.CustomDialog;
import com.jarifjak.digitalsecuritysolution.listener.FragmentListener;
import com.jarifjak.digitalsecuritysolution.service.PreloadService;
import com.jarifjak.digitalsecuritysolution.utility.Constants;
import com.jarifjak.digitalsecuritysolution.utility.SharedPrefs;
import com.jarifjak.digitalsecuritysolution.view.fragment.LoginFragment;
import com.jarifjak.digitalsecuritysolution.view.fragment.SplashFragment;
import com.jarifjak.digitalsecuritysolution.viewmodel.StartingViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartingActivity extends AppCompatActivity implements FragmentListener, CustomDialog.DialogListener {

    @BindView(R.id.starting_root)
    RelativeLayout startingRoot;
    private StartingViewModel viewModel;
    private SharedPrefs prefs;

    private FragmentTransaction transaction;

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).hide();

        prefs = new SharedPrefs(this);

        if (prefs.isLoggedIn()) {

            Intent work = new Intent(this, PreloadService.class);
            PreloadService.enqueueWork(this, work);
        }

        initialize();
    }

    private void initialize() {

        viewModel = new ViewModelProvider(this).get(StartingViewModel.class);
        prefs = new SharedPrefs(this);

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_starting, SplashFragment.newInstance()).commit();

        loadNext(2000);
    }

    private void loadNext(long delay) {

        new Handler().postDelayed(() -> {

            if (prefs.isLoggedIn()) {

                loadDashboard();

            } else {

                loadLoginFragment();
            }

        }, delay);

    }

    public void loadLoginFragment() {

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_container_starting, LoginFragment.newInstance()).commit();
    }

    public void loadDashboard() {

        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        finishAffinity();

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public SharedPrefs getPrefs() {

        return prefs;
    }


    @Override
    public void onConfirmClick(int flag) {

    }
}