package com.jarifjak.digitalsecuritysolution.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.model.Branch;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.ViewHolder> {


    private List<Branch> branches;
    private MyListener listener;

    public BranchAdapter(MyListener listener, List<Branch> branches) {

        this.listener = listener;
        this.branches = branches;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_branch, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Branch branch = branches.get(position);

        holder.nameTV.setText(branch.getId() + ". " + branch.getName() + " (" + branch.getBankCode() + ")");
        holder.addressTV.setText(branch.getAddress());
        holder.firstMNameTV.setText(branch.getFirstManagerName());
        holder.firstMNumberTV.setText(branch.getFirstManagerNumber());
        holder.secondMNameTV.setText(branch.getSecondManagerName());
        holder.secondMNumberTV.setText(branch.getSecondManagerNumber());

        holder.extendedLayout.setVisibility(branch.isExtended() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {

        return branches == null ? 0 : branches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameTV)
        AppCompatTextView nameTV;
        @BindView(R.id.addressTV)
        AppCompatTextView addressTV;
        @BindView(R.id.firstMNameTV)
        AppCompatTextView firstMNameTV;
        @BindView(R.id.firstMNumberTV)
        AppCompatTextView firstMNumberTV;
        @BindView(R.id.secondMNameTV)
        AppCompatTextView secondMNameTV;
        @BindView(R.id.secondMNumberTV)
        AppCompatTextView secondMNumberTV;
        @BindView(R.id.extendedLayout)
        RelativeLayout extendedLayout;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.editIV, R.id.cardMainContent})
        public void onViewClicked(View view) {

            int id = view.getId();

            if (id == R.id.editIV) {

                listener.onEditClick(branches.get(getAbsoluteAdapterPosition()).getKey());

            } else if (id == R.id.cardMainContent) {

                listener.onCardClick(getAbsoluteAdapterPosition());

            }
        }
    }

    public interface MyListener{

        void onEditClick(String key);
        void onCardClick(int position);
    }
}
