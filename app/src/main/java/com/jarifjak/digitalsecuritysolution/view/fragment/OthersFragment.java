package com.jarifjak.digitalsecuritysolution.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.utility.Constants;
import com.jarifjak.digitalsecuritysolution.view.activity.InsertActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class OthersFragment extends Fragment {


    public OthersFragment() {

    }

    public static OthersFragment newInstance() {

        return new OthersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActivity().setTitle("Others");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_others, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.addEmployee, R.id.addBranch})
    public void onViewClicked(View view) {

        Intent intent = new Intent(getActivity(), InsertActivity.class);
        intent.putExtra(Constants.ACTIVITY_TYPE, view.getId() == R.id.addEmployee ? 1 : 2);

        startActivity(intent);
    }
}