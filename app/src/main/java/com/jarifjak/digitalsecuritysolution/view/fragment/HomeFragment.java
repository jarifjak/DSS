package com.jarifjak.digitalsecuritysolution.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.adapter.EmployeeAdapter;
import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.model.Employee;
import com.jarifjak.digitalsecuritysolution.service.SyncService;
import com.jarifjak.digitalsecuritysolution.utility.Constants;
import com.jarifjak.digitalsecuritysolution.view.activity.EmployeeProfileActivity;
import com.jarifjak.digitalsecuritysolution.view.activity.MainActivity;
import com.jarifjak.digitalsecuritysolution.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements EmployeeAdapter.MyListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.branchSpinner)
    SmartMaterialSpinner branchSpinner;
    @BindView(R.id.branchTV)
    AppCompatTextView branchTV;
    @BindView(R.id.totalEmployeeTV)
    AppCompatTextView totalEmployeeTV;
    @BindView(R.id.searchET)
    AppCompatEditText searchET;

    private MainViewModel viewModel;
    private EmployeeAdapter adapter;
    private List<Employee> employees;
    private List<Branch> branches;
    private List<String> branchNames = new ArrayList<>();

    private String selectedBranch = "All";

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requireActivity().setTitle("Home");
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }


    @Override
    public void onResume() {

        super.onResume();

        syncData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        initialize();

        return view;
    }

    private void initialize() {

        setupSpinner();

        viewModel.getEmployeeFromRoom().observe(getViewLifecycleOwner(), employees -> {

            this.employees = employees;
            setupRecyclerView();
            adapter.notifyDataSetChanged();
            totalEmployeeTV.setText(String.valueOf(adapter.getItemCount()));

        });

        viewModel.getBranchesFromRoom().observe(getViewLifecycleOwner(), branches -> {

            this.branches = branches;

            if (branches.size() != 0) {

                initSpinnerDataSource();
            }

        });

        searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchByAll("%" + s.toString() + "%");
            }
        });
    }

    private void syncData() {

        Intent serviceIntent = new Intent(getContext(), SyncService.class);
        serviceIntent.putExtra("jobType", "all");

        SyncService.enqueueWork(getContext(), serviceIntent);
    }

    private void initSpinnerDataSource() {

        branchNames.clear();
        branchNames.add("All");

        for (Branch branch : branches) {

            branchNames.add(branch.getName());
        }

        branchSpinner.setItem(branchNames);
    }

    private void setupSpinner() {

        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                selectedBranch = branchNames.get(position);
                branchTV.setText(selectedBranch);
                searchByBranch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void setupRecyclerView() {

        adapter = new EmployeeAdapter(this, employees);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void searchByBranch() {

        if (selectedBranch.equals("All")) {

            adapter.updateFilteredList(employees);
            totalEmployeeTV.setText(String.valueOf(adapter.getItemCount()));

        } else {

            viewModel.getEmployeesByBranch(selectedBranch).observe(getViewLifecycleOwner(), new Observer<List<Employee>>() {

                @Override
                public void onChanged(List<Employee> employees) {

                    adapter.updateFilteredList(employees);
                    totalEmployeeTV.setText(String.valueOf(adapter.getItemCount()));

                }
            });

        }
    }

    private void searchByAll(String search) {

        String branch = selectedBranch.equals("All") ? "%%" : selectedBranch;

        viewModel.getEmployeesByAll(search, branch).observe(getViewLifecycleOwner(), new Observer<List<Employee>>() {

            @Override
            public void onChanged(List<Employee> employees) {

                adapter.updateFilteredList(employees);
                viewModel.getEmployeesByAll(search, selectedBranch).removeObserver(this);

            }
        });

    }

    @Override
    public void onCardClick(String key) {

        Intent intent = new Intent(getActivity(), EmployeeProfileActivity.class);

        intent.putExtra(Constants.KEY, key);

        startActivity(intent);
    }

    @Override
    public void onCallClick(String number) {

        Uri u = Uri.parse("tel:" + number);
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, u);

        try {

            startActivity(dialIntent);

        } catch (SecurityException s) {

            Log.d(TAG, "onCallClick: " + s.getMessage());
        }

    }

}