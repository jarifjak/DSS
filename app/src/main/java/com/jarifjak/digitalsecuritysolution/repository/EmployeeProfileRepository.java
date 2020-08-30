package com.jarifjak.digitalsecuritysolution.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.jarifjak.digitalsecuritysolution.database.AppDatabase;
import com.jarifjak.digitalsecuritysolution.database.BranchDao;
import com.jarifjak.digitalsecuritysolution.database.EmployeeDao;
import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.model.Employee;

public class EmployeeProfileRepository {

    private static EmployeeProfileRepository repository;

    private EmployeeDao employeeDao;
    private BranchDao branchDao;

    public static EmployeeProfileRepository getInstance(Application application) {

        if (repository == null) {

            repository = new EmployeeProfileRepository(application);
        }

        return repository;
    }

    public EmployeeProfileRepository(Application application) {

        AppDatabase database = AppDatabase.getInstance(application);

        employeeDao = database.employeeDao();
        branchDao = database.branchDao();
    }

    public LiveData<Employee> getEmployeeById(int id) {

        return employeeDao.getEmployeeById(id);
    }

    public LiveData<Branch> getBranchByName(String branchName) {

        return branchDao.getBranchByName(branchName);
    }

}
