package com.jarifjak.digitalsecuritysolution.service;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.jarifjak.digitalsecuritysolution.repository.StartingRepository;

public class PreloadService extends JobIntentService {

    public static void enqueueWork(Context context, Intent work) {

        enqueueWork(context, PreloadService.class, 1234, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        StartingRepository repository = StartingRepository.getInstance(getApplication());

        repository.listenForEmployees();
        repository.listenForBranches();
    }
}
