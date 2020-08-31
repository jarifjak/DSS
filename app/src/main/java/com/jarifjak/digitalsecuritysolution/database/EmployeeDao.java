package com.jarifjak.digitalsecuritysolution.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jarifjak.digitalsecuritysolution.model.Employee;

import java.util.List;

@Dao
public interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEmployees(List<Employee> employees);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEmployee(Employee employee);

    @Update
    void update(Employee employee);

    @Delete
    void delete(Employee employee);

    @Query("DELETE FROM employee_table")
    void deleteAllEmployees();

    @Query("SELECT * FROM employee_table")
    LiveData<List<Employee>> getEmployees();

    @Query("SELECT * FROM employee_table WHERE " + "branchName" + " = :selectedBranch ORDER BY " + "id" + " ASC")
    LiveData<List<Employee>> getEmployeesByBranch(String selectedBranch);

    @Query("SELECT * FROM employee_table WHERE " + "id" + "= :id")
    LiveData<Employee> getEmployeeById(int id);

    @Query("SELECT * FROM employee_table WHERE (" + "name" + " LIKE :search OR "
            + "bankCode" + " LIKE :search OR "
            + "number" + " LIKE :search OR "
            + "accountNumber" + " LIKE :search)" + " AND "
            + "branchName LIKE :selectedBranch")
    LiveData<List<Employee>> getEmployeesByAll(String search, String selectedBranch);

    @Query("SELECT MAX(id) FROM employee_table")
    LiveData<Integer> getMaxId();
}
