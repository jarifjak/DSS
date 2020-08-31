package com.jarifjak.digitalsecuritysolution.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.model.Employee;

import java.util.List;

@Dao
public interface BranchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBranches(List<Branch> branches);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBranch(Branch branch);

    @Update
    void update(Branch branch);

    @Delete
    void delete(Branch branch);

    @Query("DELETE FROM branch_table")
    void deleteAllBranches();

    @Query("SELECT * FROM branch_table")
    LiveData<List<Branch>> getBranches();

    @Query("SELECT MAX(id) FROM branch_table")
    LiveData<Integer> getMaxId();

    @Query("SELECT * FROM branch_table WHERE name LIKE :branchName")
    LiveData<Branch> getBranchByName(String branchName);

    @Query("SELECT * FROM branch_table WHERE id = :id")
    LiveData<Branch> getBranchById(int id);



}
