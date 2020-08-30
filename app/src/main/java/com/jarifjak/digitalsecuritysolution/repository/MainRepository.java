package com.jarifjak.digitalsecuritysolution.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jarifjak.digitalsecuritysolution.database.AppDatabase;
import com.jarifjak.digitalsecuritysolution.database.BranchDao;
import com.jarifjak.digitalsecuritysolution.database.EmployeeDao;
import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class MainRepository {

    private static MainRepository repository;

    private DatabaseReference reference;
    private LiveData<List<Employee>> employeeLiveData;
    private LiveData<List<Branch>> branchLiveData;
    private List<Employee> employees;
    private List<Branch> branches;

    private EmployeeDao employeeDao;
    private BranchDao branchDao;

    public static MainRepository getInstance(Application application) {

        if (repository == null) {

            repository = new MainRepository(application);
        }

        return repository;
    }

    public MainRepository(Application application) {

        AppDatabase database = AppDatabase.getInstance(application);

        employeeDao = database.employeeDao();
        branchDao = database.branchDao();

        employeeLiveData = employeeDao.getEmployees();
        branchLiveData = branchDao.getBranches();

    }

    public void listenForEmployees() {

        employees = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Employees").addValueEventListener(employeeListener);
    }

    public void listenForBranches() {

        branches = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Branches").addValueEventListener(branchListener);
    }


    public LiveData<List<Employee>> getEmployeeFromRoom() {

        return employeeLiveData;
    }

    public LiveData<List<Employee>> getEmployeesByBranch(String branchName) {

        return employeeDao.getEmployeesByBranch(branchName);
    }

    public LiveData<List<Employee>> getEmployeesByAll(String search, String selectedBranch) {

        return employeeDao.getEmployeesByAll(search, selectedBranch);
    }

    public void insertEmployeeInRoom(List<Employee> list) {

        new InsertEmployeeAT(employeeDao).execute(list);
    }

    public void deleteAllEmployees() {

        new DeleteEmployeesAT(employeeDao).execute();
    }

    public LiveData<List<Branch>> getBranchFromRoom() {

        return branchLiveData;
    }

    public void insertBranchInRoom(List<Branch> list) {

        new InsertBranchAT(branchDao).execute(list);
    }

    public void deleteAllBranches() {

        new DeleteBranchesAT(branchDao).execute();
    }

    ValueEventListener employeeListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.hasChildren()) {

                for (DataSnapshot d : snapshot.getChildren()) {

                    Employee employee = d.getValue(Employee.class);
                    employees.add(employee);
                }
            }

            deleteAllEmployees();
            insertEmployeeInRoom(employees);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Log.d("EmployeeListenerFailed", "onCancelled: " + error.getMessage());
        }

    };

    ValueEventListener branchListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.hasChildren()) {

                for (DataSnapshot d : snapshot.getChildren()) {

                    Branch branch = d.getValue(Branch.class);
                    branches.add(branch);
                }
            }

            deleteAllBranches();
            insertBranchInRoom(branches);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Log.d("BranchListenerFailed", "onCancelled: " + error.getMessage());
        }

    };

    public static class InsertEmployeeAT extends AsyncTask<List<Employee>, Void, Void> {


        private EmployeeDao employeeDao;

        public InsertEmployeeAT(EmployeeDao employeeDao) {

            this.employeeDao = employeeDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Employee>... lists) {

            employeeDao.insertEmployees(lists[0]);

            return null;
        }
    }

    public static class DeleteEmployeesAT extends AsyncTask<Void, Void, Void> {

        private EmployeeDao employeeDao;

        public DeleteEmployeesAT(EmployeeDao employeeDao) {

            this.employeeDao = employeeDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            employeeDao.deleteAllEmployees();
            return null;
        }
    }

    public static class InsertBranchAT extends AsyncTask<List<Branch>, Void, Void> {


        private BranchDao branchDao;

        public InsertBranchAT(BranchDao branchDao) {

            this.branchDao = branchDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Branch>... lists) {

            branchDao.insertBranches(lists[0]);

            return null;
        }
    }

    public static class DeleteBranchesAT extends AsyncTask<Void, Void, Void> {

        private BranchDao branchDao;

        public DeleteBranchesAT(BranchDao branchDao) {

            this.branchDao = branchDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            branchDao.deleteAllBranches();

            return null;
        }
    }
}
