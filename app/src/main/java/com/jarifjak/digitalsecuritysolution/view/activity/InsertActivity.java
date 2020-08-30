package com.jarifjak.digitalsecuritysolution.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.GoalRow;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.gson.Gson;
import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.dialog.CustomDialog;
import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.model.DialogExtra;
import com.jarifjak.digitalsecuritysolution.model.Employee;
import com.jarifjak.digitalsecuritysolution.utility.Constants;
import com.jarifjak.digitalsecuritysolution.viewmodel.InsertViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertActivity extends AppCompatActivity implements CustomDialog.DialogListener {

    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.eIdET)
    AppCompatEditText eIdET;
    @BindView(R.id.nameET)
    AppCompatEditText nameET;
    @BindView(R.id.phoneET)
    AppCompatEditText phoneET;
    @BindView(R.id.accountNumberET)
    AppCompatEditText accountNumberET;
    @BindView(R.id.branchTV)
    AppCompatTextView branchTV;
    @BindView(R.id.branchSpinner)
    SmartMaterialSpinner branchSpinner;
    @BindView(R.id.employeeCV)
    CardView employeeCV;
    @BindView(R.id.branchOneCV)
    CardView branchOneCV;
    @BindView(R.id.branchTwoCV)
    CardView branchTwoCV;


    @BindView(R.id.idBranchET)
    AppCompatEditText idBranchET;
    @BindView(R.id.branchNameET)
    AppCompatEditText branchNameET;
    @BindView(R.id.bankCodeET)
    AppCompatEditText bankCodeET;
    @BindView(R.id.branchAddressET)
    AppCompatEditText branchAddressET;

    @BindView(R.id.firstMNameET)
    AppCompatEditText firstMNameET;
    @BindView(R.id.firstMNumberET)
    AppCompatEditText firstMNumberET;
    @BindView(R.id.secondMNameET)
    AppCompatEditText secondMNameET;
    @BindView(R.id.secondMNumberET)
    AppCompatEditText secondMNumberET;
    @BindView(R.id.progressbarOfBranch)
    ProgressBar progressbarOfBranch;

    @BindView(R.id.deleteEmployeeBTN)
    AppCompatButton deleteEmployeeBTN;
    @BindView(R.id.insertBTN)
    AppCompatButton insertBTN;
    @BindView(R.id.deleteBranchBTN)
    AppCompatButton deleteBranchBTN;
    @BindView(R.id.confirmBTN)
    AppCompatButton confirmBTN;
    @BindView(R.id.employeeButtonLayout)
    LinearLayout employeeButtonLayout;
    @BindView(R.id.branchOneButtonLayout)
    LinearLayout branchOneButtonLayout;
    @BindView(R.id.branchTwoButtonLayout)
    LinearLayout branchTwoButtonLayout;

    private InsertViewModel viewModel;

    private static int activityType;
    private static int id;
    private String key;                    //store for updating
    private List<String> branchNames;
    private static Branch branch;
    private DialogFragment dialog;
    private boolean busy;
    private boolean dialogBusy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        activityType = getIntent().getIntExtra(Constants.ACTIVITY_TYPE, 1);

        if (activityType < 4) {

            setTitle(activityType == 1 ? "Insert Employee" : "Insert Branch");

        } else {

            setTitle(activityType == 4 ? "Update Employee" : "Update Branch");

            id = getIntent().getIntExtra(Constants.ID, 0);

        }

        dialog = new DialogFragment(); //for avoiding null exception

        viewModel = new ViewModelProvider(this).get(InsertViewModel.class);

        initialize();
    }

    private void initialize() {

        if (activityType == 1) {

            branchNames = new ArrayList<>();
            setupBranchSpinner();

            viewModel.getMaxIdOfEmployee().observe(this, id -> {

                if (id != null) {

                    eIdET.setText(String.valueOf(id + 1));
                }


            });

            viewModel.getBranches().observe(this, branches -> {

                branchNames.clear();

                for (Branch b : branches) {

                    branchNames.add(b.getName());
                }

                branchSpinner.setItem(branchNames);

            });

        } else if (activityType == 2) {

            viewModel.getMaxIdOfBranch().observe(this, new Observer<Integer>() {

                @Override
                public void onChanged(Integer id) {

                    if (id != null) {

                        idBranchET.setText(String.valueOf(id + 1));
                    }

                }

            });

        } else if (activityType == 4) {

            branchNames = new ArrayList<>();
            setupBranchSpinner();

            viewModel.getEmployeeById(id).observe(this, new Observer<Employee>() {

                @Override
                public void onChanged(Employee employee) {

                    if (employee == null) {

                        return;
                    }

                    key = employee.getKey();

                    viewModel.getBranchByName(employee.getBranchName()).observe(InsertActivity.this, new Observer<Branch>() {

                        @Override
                        public void onChanged(Branch branch) {

                            if (branch == null) {

                                return;
                            }

                            InsertActivity.this.branch = branch;

                            setEmployeeInfo(employee);
                        }

                    });
                }
            });

            viewModel.getBranches().observe(this, new Observer<List<Branch>>() {

                @Override
                public void onChanged(List<Branch> branches) {

                    if (branches == null) {

                        return;
                    }

                    branchNames.clear();

                    for (Branch b : branches) {

                        branchNames.add(b.getName());
                    }

                    branchSpinner.setItem(branchNames);

                }
            });

        } else if (activityType == 5) {

            viewModel.getBranchById(id).observe(this, new Observer<Branch>() {

                @Override
                public void onChanged(Branch branch) {

                    key = branch.getKey();

                    setBranchInfo(branch);
                }
            });
        }

        setViewVisibility(activityType);
    }

    @SuppressLint("SetTextI18n")
    private void setViewVisibility(int viewType) {

        if (viewType == 1) {

            //For employee insert

            employeeCV.setVisibility(View.VISIBLE);
            employeeButtonLayout.setVisibility(View.VISIBLE);

            deleteEmployeeBTN.setVisibility(View.GONE);

        } else if (viewType == 2) {

            //For branch insert 1st page

            employeeCV.setVisibility(View.GONE);
            employeeButtonLayout.setVisibility(View.GONE);

            branchTwoCV.setVisibility(View.GONE);
            branchTwoButtonLayout.setVisibility(View.GONE);

            branchOneCV.setVisibility(View.VISIBLE);
            branchOneButtonLayout.setVisibility(View.VISIBLE);
            deleteBranchBTN.setVisibility(View.GONE);

        } else if (viewType == 3) {

            //For branch insert 2nd page

            branchOneCV.setVisibility(View.GONE);
            branchOneButtonLayout.setVisibility(View.GONE);

            branchTwoCV.setVisibility(View.VISIBLE);
            branchTwoButtonLayout.setVisibility(View.VISIBLE);

        } else if (viewType == 4) {

            //For employee update

            employeeCV.setVisibility(View.VISIBLE);
            insertBTN.setText("UPDATE");

            employeeButtonLayout.setVisibility(View.VISIBLE);

        } else if (viewType == 5) {

            //For branch update

            employeeCV.setVisibility(View.GONE);
            employeeButtonLayout.setVisibility(View.GONE);

            branchTwoCV.setVisibility(View.GONE);
            branchTwoButtonLayout.setVisibility(View.GONE);

            branchOneCV.setVisibility(View.VISIBLE);
            branchOneButtonLayout.setVisibility(View.VISIBLE);

            confirmBTN.setText("UPDATE");
        }
    }

    private void setupBranchSpinner() {

        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String name = branchNames.get(position);

                branchTV.setText(name);

                viewModel.getBranchByName(name).observe(InsertActivity.this, new Observer<Branch>() {

                    @Override
                    public void onChanged(Branch branch) {

                        InsertActivity.this.branch = branch;
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void insertOrUpdateEmployee() {

        if (busy) {

            return;

        } else {

            busy = true;
        }

        String id = eIdET.getText().toString();
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();
        String accountNumber = accountNumberET.getText().toString();

        if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || accountNumber.isEmpty() || branch == null) {

            if (id.isEmpty()) {

                eIdET.setError("Insert correct id");
            }

            if (name.isEmpty()) {

                nameET.setError("Insert name");
            }

            if (phone.isEmpty()) {

                phoneET.setError("Insert number");
            }

            if (accountNumber.isEmpty()) {

                accountNumberET.setError("Insert account number");
            }

            if (branch == null) {

                Toast.makeText(this, "Branch Not Selected", Toast.LENGTH_SHORT).show();
            }

            progressbar.setVisibility(View.GONE);
            busy = false;
            return;
        }

        Employee employee = new Employee(Integer.parseInt(id), name, accountNumber, branch.getBankCode(), branch.getName(), phone);

        Observer<Boolean> observer = new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean aBoolean) {

                progressbar.setVisibility(View.GONE);

                busy = false;

                if (activityType == 4) {

                    Toast.makeText(InsertActivity.this, aBoolean ? "Successful" : "Failed", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    showCustomDialog(1);

                }

            }
        };

        if (activityType == 1) {

            viewModel.insertEmployee(employee).observe(this, observer);

        } else if (activityType == 4) {

            employee.setKey(key);

            viewModel.updateEmployee(employee).observe(this, observer);
        }
    }


    private void deleteEmployee() {

        viewModel.deleteEmployee(key).observe(InsertActivity.this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean == null) {

                    return;
                }

                Toast.makeText(InsertActivity.this, aBoolean ? "Successful" : "Failed", Toast.LENGTH_SHORT).show();

                if (aBoolean) {

                    EmployeeProfileActivity.getInstance().finish();
                    finish();

                }
            }
        });

    }

    private boolean isFirstPageCorrect() {

        String id = idBranchET.getText().toString();
        String name = branchNameET.getText().toString();
        String bankCode = bankCodeET.getText().toString();
        String branchAddress = branchAddressET.getText().toString();

        if (id.isEmpty() || name.isEmpty() || bankCode.isEmpty() || branchAddress.isEmpty()) {

            if (id.isEmpty()) {

                idBranchET.setError("Insert correct Id");
            }

            if (name.isEmpty()) {

                branchNameET.setError("Insert name");
            }

            if (bankCode.isEmpty()) {

                bankCodeET.setError("Insert bank code");
            }

            if (branchAddress.isEmpty()) {

                branchAddressET.setError("Insert address");
            }

            return false;
        }

        branch = new Branch(Integer.parseInt(id), branchAddress, bankCode, name);
        return true;
    }

    private boolean isSecondPageCorrect() {

        String firstManagerName = firstMNameET.getText().toString();
        String firstManagerNumber = firstMNumberET.getText().toString();
        String secondManagerName = secondMNameET.getText().toString();
        String secondManagerNumber = secondMNumberET.getText().toString();

        if (firstManagerName.isEmpty() || firstManagerNumber.isEmpty() || secondManagerName.isEmpty() || secondManagerNumber.isEmpty()) {

            if (firstManagerName.isEmpty()) {

                firstMNameET.setError("Type here");
            }

            if (firstManagerNumber.isEmpty()) {

                firstMNumberET.setError("Type here");
            }

            if (secondManagerName.isEmpty()) {

                secondMNameET.setError("Type here");
            }

            if (secondManagerNumber.isEmpty()) {

                secondMNumberET.setError("Type here");
            }

            return false;
        }

        branch.setFirstManagerName(firstManagerName);
        branch.setFirstManagerName(firstManagerNumber);
        branch.setSecondManagerName(secondManagerName);
        branch.setFirstManagerName(secondManagerNumber);

        return true;
    }

    private void insertOrUpdateBranch() {

        if (busy) {

            return;

        } else {

            busy = true;
        }

        if (!isFirstPageCorrect()) {

            setViewVisibility(2);

            progressbarOfBranch.setVisibility(View.GONE);
            busy = false;
            return;

        } else if (!isSecondPageCorrect()) {

            progressbarOfBranch.setVisibility(View.GONE);
            busy = false;
            return;
        }

        Observer<Boolean> observer = new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean aBoolean) {

                progressbarOfBranch.setVisibility(View.GONE);

                busy = false;

                if (activityType == 5) {

                    Toast.makeText(InsertActivity.this, aBoolean ? "Successful" : "Failed", Toast.LENGTH_SHORT).show();
                    finish();

                } else {

                    showCustomDialog(1);

                }
            }
        };

        if (activityType == 2) {

            viewModel.insertBranch(branch).observe(this, observer);

        } else if (activityType == 5) {

            branch.setKey(key);
            viewModel.updateBranch(branch).observe(this, observer);

        }
    }

    private void deleteBranch() {

        viewModel.deleteBranch(key).observe(InsertActivity.this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean == null) {

                    return;
                }

                Toast.makeText(InsertActivity.this, aBoolean ? "Successful" : "Failed", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    private void setEmployeeInfo(Employee employee) {

        eIdET.setText(String.valueOf(employee.getId()));
        nameET.setText(employee.getName());
        phoneET.setText(employee.getNumber());
        accountNumberET.setText(employee.getAccountNumber());
        branchTV.setText(branch.getName());
    }

    private void setBranchInfo(Branch branch) {

        idBranchET.setText(branch.getId());
        branchNameET.setText(branch.getName());
        bankCodeET.setText(branch.getBankCode());
        branchAddressET.setText(branch.getAddress());

        firstMNameET.setText(branch.getFirstManagerName());
        firstMNumberET.setText(branch.getFirstManagerNumber());
        secondMNameET.setText(branch.getSecondManagerName());
        secondMNumberET.setText(branch.getSecondManagerNumber());

    }

    private void showCustomDialog(int flag) {

        if (dialog.isVisible()) {

            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prevFragment = getSupportFragmentManager().findFragmentByTag(Constants.DIALOG_TAG);

        if (prevFragment != null) {

            ft.remove(prevFragment);
        }

        ft.addToBackStack(null);

        if (flag == 4) {

            String object = activityType == 4 ? "employee" : "branch";

            String jsonObject = new Gson().toJson(new DialogExtra("Delete", "Do you want to delete this " + object + " ?"));
            dialog = CustomDialog.newInstance(Constants.DIALOG_TWO_BUTTON, flag, jsonObject);

        } else if (flag == 1) {

            String object = activityType == 1 ? "Employee" : "Branch";

            String jsonObject = new Gson().toJson(new DialogExtra("Inserted", object + " has been inserted successfully"));
            dialog = CustomDialog.newInstance(Constants.DIALOG_ONE_BUTTON, flag, jsonObject);

        }

        dialog.show(ft, Constants.DIALOG_TAG);
    }

    @OnClick({R.id.circleTwoIV, R.id.mCircleOneIV, R.id.deleteEmployeeBTN, R.id.insertBTN, R.id.deleteBranchBTN, R.id.nextBTN, R.id.backBranchBTN, R.id.confirmBTN})
    public void onViewClicked(View view) {

        int id = view.getId();

        if (id == R.id.circleTwoIV) {

            setViewVisibility(3);

        } else if (id == R.id.mCircleOneIV) {

            setViewVisibility(2);

        } else if (id == R.id.insertBTN) {

            progressbar.setVisibility(View.VISIBLE);
            insertOrUpdateEmployee();

        } else if (id == R.id.nextBTN) {

            setViewVisibility(3);

        } else if (id == R.id.confirmBTN) {

            progressbarOfBranch.setVisibility(View.VISIBLE);
            insertOrUpdateBranch();

        } else if (id == R.id.backBranchBTN) {

            setViewVisibility(2);

        } else if (id == R.id.deleteEmployeeBTN) {

            showCustomDialog(4);

        } else if (id == R.id.deleteBranchBTN) {

            deleteBranch();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfirmClick(int flag) {

        if (flag == 4) {

            deleteEmployee();

        } else if (flag == 5) {

            deleteBranch();
        }
    }
}