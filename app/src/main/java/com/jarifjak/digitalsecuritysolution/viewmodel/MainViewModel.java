package com.jarifjak.digitalsecuritysolution.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.model.Employee;
import com.jarifjak.digitalsecuritysolution.repository.MainRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MainRepository repository;

    public MainViewModel(@NonNull Application application) {

        super(application);

        repository = MainRepository.getInstance(application);

    }


    public LiveData<List<Employee>> getEmployeeFromRoom() {

        return repository.getEmployeeFromRoom();
    }

    public void insertEmployeeInRoom(List<Employee> employees) {

        repository.insertEmployeeInRoom(employees);
    }

    public void deleteAllEmployees() {

        repository.deleteAllEmployees();
    }

    public LiveData<List<Branch>> getBranchesFromRoom() {

        return repository.getBranchFromRoom();
    }

    public void insertBranchInRoom(List<Branch> branches) {

        repository.insertBranchInRoom(branches);
    }

    public void deleteAllBranches() {

        repository.deleteAllBranches();
    }

    public LiveData<List<Employee>> getEmployeesByBranch(String branchName) {

        return repository.getEmployeesByBranch(branchName);
    }

    public LiveData<List<Employee>> getEmployeesByAll(String search, String selectedBranch) {

        return repository.getEmployeesByAll(search, selectedBranch);
    }

}
