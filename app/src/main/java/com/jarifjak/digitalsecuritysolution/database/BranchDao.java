package com.jarifjak.digitalsecuritysolution.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jarifjak.digitalsecuritysolution.model.Branch;

import java.util.List;

@Dao
public interface BranchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBranches(List<Branch> branches);

    @Update
    void update(Branch branch);

    @Query("DELETE FROM branch_table")
    void deleteAllBranches();

    @Query("SELECT * FROM branch_table")
    LiveData<List<Branch>> getBranches();

    @Query("SELECT MAX(id) FROM branch_table")
    LiveData<Integer> getMaxId();

    @Query("SELECT * FROM branch_table WHERE name LIKE :branchName")
    LiveData<Branch> getBranchByName(String branchName);

}
