package com.jarifjak.digitalsecuritysolution.service;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.jarifjak.digitalsecuritysolution.repository.MainRepository;

import java.util.Objects;

public class SyncService extends JobIntentService {


    public static void enqueueWork(Context context, Intent work) {

        enqueueWork(context, SyncService.class, 123, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        String jobType = intent.getStringExtra("jobType");

        MainRepository repository = MainRepository.getInstance(getApplication());

        if (Objects.equals(jobType, "employee")) {

            repository.listenForEmployees();

        } else if (Objects.equals(jobType, "branch")) {

            repository.listenForBranches();

        } else if (Objects.equals(jobType, "all")) {

            repository.listenForEmployees();
            repository.listenForBranches();
        }

    }

}
