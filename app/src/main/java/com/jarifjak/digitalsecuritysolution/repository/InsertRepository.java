package com.jarifjak.digitalsecuritysolution.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jarifjak.digitalsecuritysolution.database.AppDatabase;
import com.jarifjak.digitalsecuritysolution.database.BranchDao;
import com.jarifjak.digitalsecuritysolution.database.EmployeeDao;
import com.jarifjak.digitalsecuritysolution.listener.FirebaseListener;
import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.model.Employee;

import java.util.List;

public class InsertRepository {

    private static InsertRepository repository;

    private DatabaseReference reference;
    private FirebaseListener listener;
    private BranchDao branchDao;
    private EmployeeDao employeeDao;


    public static InsertRepository getInstance(Application application) {

        if (repository == null) {

            repository = new InsertRepository(application);
        }

        return repository;
    }

    public InsertRepository(Application application) {

        AppDatabase database = AppDatabase.getInstance(application);

        employeeDao = database.employeeDao();
        branchDao = database.branchDao();
    }


    public LiveData<List<Branch>> getBranches() {

        return branchDao.getBranches();
    }


    public LiveData<Branch> getBranchByKey(String key) {

        return branchDao.getBranchByKey(key);
    }

    public LiveData<Integer> getMaxIdOfBranch() {

        return branchDao.getMaxId();
    }

    public LiveData<Integer> getMaxIdOfEmployee() {

        return employeeDao.getMaxId();
    }

    public LiveData<Branch> getBranchByName(String branchName) {

        return branchDao.getBranchByName(branchName);
    }

    public LiveData<Employee> getEmployeeByKey(String key) {

        return employeeDao.getEmployeeByKey(key);
    }

    public void insertEmployee(Employee employee, FirebaseListener listener) {

        this.listener = listener;

        reference = FirebaseDatabase.getInstance().getReference().child("Employees");

        String key = reference.push().getKey();

        if (key == null || key.isEmpty()) {

            listener.onOperationComplete(false);
            return;
        }

        employee.setKey(key);

        reference.child(key).setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                listener.onOperationComplete(task.isSuccessful());

            }
        });
    }

    public void updateEmployee(Employee employee, FirebaseListener listener) {

        this.listener = listener;

        reference = FirebaseDatabase.getInstance().getReference().child("Employees");

        reference.child(employee.getKey()).setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                listener.onOperationComplete(task.isSuccessful());

            }
        });
    }

    public void deleteEmployee(String key, FirebaseListener listener) {

        this.listener = listener;

        reference = FirebaseDatabase.getInstance().getReference().child("Employees");

        reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                listener.onOperationComplete(task.isSuccessful());

            }
        });
    }

    public void insertBranch(Branch branch, FirebaseListener listener) {

        this.listener = listener;

        reference = FirebaseDatabase.getInstance().getReference().child("Branches");

        String key = reference.push().getKey();

        if (key == null || key.isEmpty()) {

            listener.onOperationComplete(false);
            return;
        }

        branch.setKey(key);

        reference.child(key).setValue(branch).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                listener.onOperationComplete(task.isSuccessful());

            }
        });

    }

    public void updateBranch(Branch branch, FirebaseListener listener) {

        this.listener = listener;

        reference = FirebaseDatabase.getInstance().getReference().child("Branches");

        reference.child(branch.getKey()).setValue(branch).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                listener.onOperationComplete(task.isSuccessful());

            }
        });

    }

    public void deleteBranch(String key, FirebaseListener listener) {

        this.listener = listener;

        reference = FirebaseDatabase.getInstance().getReference().child("Branches");

        reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                listener.onOperationComplete(task.isSuccessful());

            }
        });

    }




}
