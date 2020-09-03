package com.jarifjak.digitalsecuritysolution.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;

import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.utility.Constants;
import com.jarifjak.digitalsecuritysolution.viewmodel.EmployeeProfileViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeProfileActivity extends AppCompatActivity {

    @BindView(R.id.idTV)
    AppCompatTextView idTV;
    @BindView(R.id.nameTV)
    AppCompatTextView nameTV;
    @BindView(R.id.numberTV)
    AppCompatTextView numberTV;
    @BindView(R.id.accountNumberTV)
    AppCompatTextView accountNumberTV;
    @BindView(R.id.bankCodeTV)
    AppCompatTextView bankCodeTV;
    @BindView(R.id.branchTV)
    AppCompatTextView branchTV;
    @BindView(R.id.branchHeadingTV)
    AppCompatTextView branchHeadingTV;
    @BindView(R.id.firstMNameTV)
    AppCompatTextView firstMNameTV;
    @BindView(R.id.firstMNumberTV)
    AppCompatTextView firstMNumberTV;
    @BindView(R.id.secondMNameTV)
    AppCompatTextView secondMNameTV;
    @BindView(R.id.secondMNumberTV)
    AppCompatTextView secondMNumberTV;

    private static EmployeeProfileActivity activity;
    private EmployeeProfileViewModel viewmodel;
    private String employeeKey;

    public static EmployeeProfileActivity getInstance() {
        return activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.activity = this;

        setContentView(R.layout.activity_employee_profile);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Employee Profile");

        employeeKey = getIntent().getStringExtra(Constants.KEY);

        initialize();
    }

    @SuppressLint("SetTextI18n")
    private void initialize() {

        viewmodel = new ViewModelProvider(this).get(EmployeeProfileViewModel.class);

        viewmodel.getEmployeeByKey(employeeKey).observe(this, employee -> {

            if (employee == null) {

                return;
            }

            idTV.setText("#" + employee.getId());
            nameTV.setText(employee.getName());
            numberTV.setText(employee.getNumber());
            accountNumberTV.setText(employee.getAccountNumber());
            bankCodeTV.setText(employee.getBankCode());
            branchTV.setText(employee.getBranchName());
            branchHeadingTV.setText(employee.getBranchName());

            viewmodel.getBranchByName(employee.getBranchName()).observe(EmployeeProfileActivity.this, branch -> {

                if (branch == null) {

                    return;
                }

                firstMNameTV.setText(branch.getFirstManagerName());
                firstMNumberTV.setText(branch.getFirstManagerNumber());
                secondMNameTV.setText(branch.getSecondManagerName());
                secondMNumberTV.setText(branch.getSecondManagerNumber());
            });
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.editFAB)
    public void onViewClicked() {

        Intent intent = new Intent(this, InsertActivity.class);
        intent.putExtra(Constants.ACTIVITY_TYPE, 4);
        intent.putExtra(Constants.KEY, employeeKey);

        startActivity(intent);
    }
}