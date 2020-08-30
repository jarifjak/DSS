package com.jarifjak.digitalsecuritysolution.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.model.Employee;
import com.jarifjak.digitalsecuritysolution.repository.EmployeeProfileRepository;

public class EmployeeProfileViewModel extends AndroidViewModel {

    private EmployeeProfileRepository repository;

    public EmployeeProfileViewModel(@NonNull Application application) {

        super(application);
        repository = EmployeeProfileRepository.getInstance(application);

    }

    public LiveData<Employee> getEmployeeById(int id) {

        return repository.getEmployeeById(id);
    }

    public LiveData<Branch> getBranchByName(String branchName) {

        return repository.getBranchByName(branchName);
    }

}
