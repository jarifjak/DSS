package com.jarifjak.digitalsecuritysolution.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

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

public class StartingRepository {

    private static StartingRepository repository;

    DatabaseReference reference;
    EmployeeDao employeeDao;
    BranchDao branchDao;
    List<Employee> employees;
    List<Branch> branches;

    public static StartingRepository getInstance(Application application) {

        if (repository == null) {

            repository = new StartingRepository(application);
        }

        return repository;

    }

    public StartingRepository(Application application) {

        AppDatabase database = AppDatabase.getInstance(application);

        employeeDao = database.employeeDao();
        branchDao = database.branchDao();
    }

    public void listenForEmployees() {

        employees = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Employees").addListenerForSingleValueEvent(employeeListener);
    }

    public void listenForBranches() {

        branches = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Branches").addListenerForSingleValueEvent(branchListener);
    }

    public void insertEmployeesInRoom(List<Employee> list) {

        new InsertEmployeesAT(employeeDao).execute(list);
    }

    private void deleteAllEmployees() {

        new DeleteEmployeesAT(employeeDao).execute();
    }

    public void insertBranchesInRoom(List<Branch> list) {

        new InsertBranchesAT(branchDao).execute(list);
    }

    private void deleteAllBranches() {

        new DeleteBranchesAT(branchDao).execute();
    }


    ValueEventListener employeeListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            employees = new ArrayList<>();

            if (snapshot.hasChildren()) {

                for (DataSnapshot d : snapshot.getChildren()) {

                    Employee employee = d.getValue(Employee.class);
                    employees.add(employee);
                }

                deleteAllEmployees();
                insertEmployeesInRoom(employees);

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Log.d("EmployeeListenerFailed", "onCancelled: " + error.getMessage());
        }
    };

    ValueEventListener branchListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            branches = new ArrayList<>();

            if (snapshot.hasChildren()) {

                for (DataSnapshot d : snapshot.getChildren()) {

                    Branch branch = d.getValue(Branch.class);
                    branches.add(branch);
                }

                deleteAllBranches();
                insertBranchesInRoom(branches);

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Log.d("EmployeeListenerFailed", "onCancelled: " + error.getMessage());
        }

    };

    public static class InsertEmployeesAT extends AsyncTask<List<Employee>, Void, Void> {

        private EmployeeDao employeeDao;

        public InsertEmployeesAT(EmployeeDao employeeDao) {

            this.employeeDao = employeeDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Employee>... lists) {

            if (lists[0] == null || lists[0].size() == 0) {

                return null;
            }

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

    public static class InsertBranchesAT extends AsyncTask<List<Branch>, Void, Void> {

        private BranchDao branchDao;

        public InsertBranchesAT(BranchDao branchDao) {

            this.branchDao = branchDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Branch>... lists) {

            if (lists[0] == null || lists[0].size() == 0) {

                return null;
            }

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
