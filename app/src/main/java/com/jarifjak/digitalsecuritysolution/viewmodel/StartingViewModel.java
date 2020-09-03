package com.jarifjak.digitalsecuritysolution.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.jarifjak.digitalsecuritysolution.repository.StartingRepository;

public class StartingViewModel extends AndroidViewModel {

    private StartingRepository repository;

    public StartingViewModel(@NonNull Application application) {

        super(application);
        repository = StartingRepository.getInstance(application);
    }

}
