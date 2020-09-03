package com.jarifjak.digitalsecuritysolution.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarifjak.digitalsecuritysolution.R;

import butterknife.ButterKnife;

public class SplashFragment extends Fragment {

    public static SplashFragment newInstance() {

        return new SplashFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}