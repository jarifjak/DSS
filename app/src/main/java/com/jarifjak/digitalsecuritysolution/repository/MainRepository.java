package com.jarifjak.digitalsecuritysolution.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
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
        //reference.child("Employees").addValueEventListener(employeeListener);
        reference.child("Employees").addChildEventListener(employeeChildListener);
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

    public void insertEmployeesInRoom(List<Employee> list) {

        new InsertEmployeeAT(employeeDao).execute(list);
    }

    public void insertEmployeeInRoom(Employee employee) {

        new InsertOneEmployeeAT(employeeDao).execute(employee);
    }

    public void updateEmployee(Employee employee) {

        new UpdateEmployeeAt(employeeDao).execute(employee);
    }


    public void deleteEmployee(Employee employee) {

        employeeDao.delete(employee);
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
            insertEmployeesInRoom(employees);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Log.d("EmployeeListenerFailed", "onCancelled: " + error.getMessage());
        }

    };


    ChildEventListener employeeChildListener = new ChildEventListener() {

        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            Employee employee = snapshot.getValue(Employee.class);
            repository.insertEmployeeInRoom(employee);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            Employee employee = snapshot.getValue(Employee.class);
            repository.updateEmployee(employee);

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            Employee employee = snapshot.getValue(Employee.class);
            repository.deleteEmployee(employee);
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

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

            if (lists == null) {

                return null;
            }

            if (lists[0] == null || lists[0].size() == 0) {

                return null;
            }

            employeeDao.insertEmployees(lists[0]);

            return null;
        }
    }

    public static class InsertOneEmployeeAT extends AsyncTask<Employee, Void, Void> {

        private EmployeeDao employeeDao;

        public InsertOneEmployeeAT(EmployeeDao employeeDao) {

            this.employeeDao = employeeDao;
        }


        @Override
        protected Void doInBackground(Employee... employees) {

            employeeDao.insertEmployee(employees[0]);
            return null;
        }
    }

    public static class UpdateEmployeeAt extends AsyncTask<Employee, Void, Void> {

        private EmployeeDao employeeDao;

        public UpdateEmployeeAt(EmployeeDao employeeDao) {

            this.employeeDao = employeeDao;
        }


        @Override
        protected Void doInBackground(Employee... employees) {

            employeeDao.update(employees[0]);
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
