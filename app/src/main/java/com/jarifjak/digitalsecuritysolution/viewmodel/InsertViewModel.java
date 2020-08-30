package com.jarifjak.digitalsecuritysolution.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jarifjak.digitalsecuritysolution.listener.FirebaseListener;
import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.model.Employee;
import com.jarifjak.digitalsecuritysolution.repository.InsertRepository;

import java.util.List;

public class InsertViewModel extends AndroidViewModel {

    private InsertRepository repository;
    private MutableLiveData<Boolean> isSuccessful;

    public InsertViewModel(@NonNull Application application) {

        super(application);
        repository = new InsertRepository(application);
        isSuccessful = new MutableLiveData<>();
    }

    public LiveData<Integer> getMaxIdOfEmployee() {

        return repository.getMaxIdOfEmployee();
    }

    public LiveData<Integer> getMaxIdOfBranch() {

        return repository.getMaxIdOfBranch();
    }

    public LiveData<List<Branch>> getBranches() {

        return repository.getBranches();
    }

    public LiveData<Branch> getBranchById(int id) {

        return repository.getBranchById(id);
    }

    public LiveData<Branch> getBranchByName(String branchName) {

        return repository.getBranchByName(branchName);
    }

    public LiveData<Employee> getEmployeeById(int id) {

        return repository.getEmployeeById(id);
    }

    public LiveData<Boolean> insertEmployee(Employee employee) {

        repository.insertEmployee(employee, new FirebaseListener() {

            @Override
            public void onOperationComplete(boolean successStatus) {

                isSuccessful.setValue(successStatus);

            }
        });

        return isSuccessful;
    }

    public LiveData<Boolean> updateEmployee(Employee employee) {

        repository.updateEmployee(employee, new FirebaseListener() {

            @Override
            public void onOperationComplete(boolean successStatus) {

                isSuccessful.setValue(successStatus);

            }
        });

        return isSuccessful;
    }

    public LiveData<Boolean> deleteEmployee(String key) {

        repository.deleteEmployee(key, new FirebaseListener() {

            @Override
            public void onOperationComplete(boolean successStatus) {

                isSuccessful.setValue(successStatus);

            }
        });

        return isSuccessful;
    }

    public LiveData<Boolean> insertBranch(Branch branch) {

        repository.insertBranch(branch, new FirebaseListener() {

            @Override
            public void onOperationComplete(boolean successStatus) {

                isSuccessful.setValue(successStatus);

            }
        });

        return isSuccessful;
    }

    public LiveData<Boolean> updateBranch(Branch branch) {

        repository.updateBranch(branch, new FirebaseListener() {

            @Override
            public void onOperationComplete(boolean successStatus) {

                isSuccessful.setValue(successStatus);

            }
        });

        return isSuccessful;
    }

    public LiveData<Boolean> deleteBranch(String key) {

        repository.deleteBranch(key, new FirebaseListener() {

            @Override
            public void onOperationComplete(boolean successStatus) {

                isSuccessful.setValue(successStatus);

            }
        });

        return isSuccessful;
    }
}
