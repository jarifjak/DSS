package com.jarifjak.digitalsecuritysolution.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.model.Employee;

@Database(entities = {Employee.class, Branch.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase database;
    public abstract EmployeeDao employeeDao();
    public abstract BranchDao branchDao();

    public static AppDatabase getInstance(Context context) {

        if (database == null) {

            database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database")
                           .fallbackToDestructiveMigration()
                           .build();
        }

        return database;
    }

}
