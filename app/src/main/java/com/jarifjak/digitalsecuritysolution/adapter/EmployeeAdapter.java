package com.jarifjak.digitalsecuritysolution.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.model.Employee;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {


    private List<Employee> employees;
    private MyListener listener;

    public EmployeeAdapter(MyListener listener, List<Employee> employees) {

        this.listener = listener;
        this.employees = employees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_employee, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nameTV.setText(employees.get(position).getName());
        holder.accountNumberTV.setText(employees.get(position).getAccountNumber());
        holder.serialTV.setText("#" + employees.get(position).getId());
        holder.branchNameTV.setText(employees.get(position).getBranchName());
        holder.bankCodeTV.setText(employees.get(position).getBankCode());
    }

    @Override
    public int getItemCount() {

        return employees == null ? 0 : employees.size();
    }

    public void updateFilteredList(List<Employee> filteredEmployees) {

        this.employees = filteredEmployees;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameTV)
        AppCompatTextView nameTV;
        @BindView(R.id.accountNumberTV)
        AppCompatTextView accountNumberTV;
        @BindView(R.id.serialTV)
        AppCompatTextView serialTV;
        @BindView(R.id.branchNameTV)
        AppCompatTextView branchNameTV;
        @BindView(R.id.bankCodeTV)
        AppCompatTextView bankCodeTV;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.callIV, R.id.cardRoot})
        public void onViewClicked(View view) {

            switch (view.getId()) {

                case R.id.callIV:
                    listener.onCallClick(employees.get(getAbsoluteAdapterPosition()).getId());
                    break;

                case R.id.cardRoot:
                    listener.onCardClick(employees.get(getAbsoluteAdapterPosition()).getId());
                    break;
            }
        }
    }

    public interface MyListener {

        void onCardClick(int id);
        void onCallClick(int id);
    }
}
