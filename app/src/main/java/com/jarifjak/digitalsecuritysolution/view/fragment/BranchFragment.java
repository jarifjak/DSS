package com.jarifjak.digitalsecuritysolution.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.adapter.BranchAdapter;
import com.jarifjak.digitalsecuritysolution.model.Branch;
import com.jarifjak.digitalsecuritysolution.utility.Constants;
import com.jarifjak.digitalsecuritysolution.view.activity.InsertActivity;
import com.jarifjak.digitalsecuritysolution.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BranchFragment extends Fragment implements BranchAdapter.MyListener {

    @BindView(R.id.totalBranchTV)
    AppCompatTextView totalBranchTV;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Branch> branches;
    private BranchAdapter adapter;
    private MainViewModel viewModel;

    public static BranchFragment newInstance() {

        return new BranchFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requireActivity().setTitle("Branch");

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        branches = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_branch, container, false);

        ButterKnife.bind(this, view);

        initialize();

        return view;
    }

    private void initialize() {

        viewModel.getBranchesFromRoom().observe(getViewLifecycleOwner(), new Observer<List<Branch>>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<Branch> branches) {

                if (branches == null) {

                    return;
                }

                BranchFragment.this.branches = branches;

                totalBranchTV.setText("Total Branch: "+ branches.size());

                setupRecyclerView();
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void setupRecyclerView() {

        adapter = new BranchAdapter(this, branches);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEditClick(int id) {

        Intent intent = new Intent(getActivity(), InsertActivity.class);
        intent.putExtra(Constants.ACTIVITY_TYPE, 5);
        intent.putExtra(Constants.ID, id);

        startActivity(intent);
    }

    @Override
    public void onCardClick(int position) {

        branches.get(position).setExtended(!branches.get(position).isExtended());
        adapter.notifyItemChanged(position);
    }

}