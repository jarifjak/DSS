package com.jarifjak.digitalsecuritysolution.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.dialog.CustomDialog;
import com.jarifjak.digitalsecuritysolution.listener.FirebaseListener;
import com.jarifjak.digitalsecuritysolution.listener.FragmentListener;
import com.jarifjak.digitalsecuritysolution.model.DialogExtra;
import com.jarifjak.digitalsecuritysolution.utility.Constants;
import com.jarifjak.digitalsecuritysolution.utility.SharedPrefs;
import com.jarifjak.digitalsecuritysolution.view.activity.MainActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    @BindView(R.id.emailET)
    AppCompatEditText emailET;
    @BindView(R.id.passwordET)
    AppCompatEditText passwordET;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    private FirebaseAuth firebaseAuth;
    private FragmentListener listener;
    private DialogFragment dialog;
    private SharedPrefs prefs;
    private volatile boolean busy = false;


    public LoginFragment() {

    }

    public static LoginFragment newInstance() {

        return new LoginFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        this.listener = (FragmentListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        dialog = new DialogFragment();   // avoid null exception
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        prefs = listener.getPrefs();

        return view;
    }

    private void processLogin(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), task -> {

            if (task.isSuccessful()) {

                prefs.setLoggedIn(true);

                requireActivity().finishAffinity();
                startActivity(new Intent(requireActivity(), MainActivity.class));

            } else {

                showLoginFailedDialog();

                progressbar.setVisibility(View.GONE);
                busy = false;

            }
        });

    }

    private void showLoginFailedDialog() {

        if (dialog.isVisible()) {

            return;
        }

        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        Fragment prevFragment = requireActivity().getSupportFragmentManager().findFragmentByTag(Constants.DIALOG_TAG);

        if (prevFragment != null) {

            ft.remove(prevFragment);
        }

        ft.addToBackStack(null);

        DialogExtra dialogExtra = new DialogExtra("Authentication Failed", "Wrong email or password");

        dialog = CustomDialog.newInstance(Constants.DIALOG_ONE_BUTTON, 2, new Gson().toJson(dialogExtra));
        dialog.show(ft, Constants.DIALOG_TAG);
    }


    @OnClick(R.id.loginButton)
    public void onViewClicked() {

        if (busy) {

            return;

        } else {

            progressbar.setVisibility(View.VISIBLE);
            busy = true;

        }

        String email = Objects.requireNonNull(emailET.getText()).toString();
        String password = Objects.requireNonNull(passwordET.getText()).toString();

        if (email.isEmpty() || password.isEmpty()) {

            if (email.isEmpty()) {

                emailET.setError("Please enter email");
            }

            if (password.isEmpty()) {

                passwordET.setError("Please enter password");
            }

            progressbar.setVisibility(View.GONE);
            busy = false;

            return;
        }

        processLogin(email, password);

    }

}