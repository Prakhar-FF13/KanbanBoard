package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.TaskModel;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViewAdapter;

import java.util.ArrayList;

public class StatusInProgressFragment extends Fragment implements RecyclerViewAdapter.itemClickListener{

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    public static ArrayList<TaskModel> inProgressTaskModelArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_in_progress, container, false);
        loadData();
        recyclerView = view.findViewById(R.id.idRVInProgress);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new RecyclerViewAdapter(inProgressTaskModelArrayList,this);
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onItemClick(TaskModel taskModel) {
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.task_model_object),taskModel);
        taskDetailFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.status_inprogress_fragment)));
        transaction.add(R.id.idFrameLayout,taskDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //load tasks from server with status as Open
    private void loadData() {
        inProgressTaskModelArrayList.add(new TaskModel("title1prog","title 1 description...","high","Rishabh","inProgress"));
        inProgressTaskModelArrayList.add(new TaskModel("title2prog","title 2 description...","medium","Aditya","inProgress"));
        inProgressTaskModelArrayList.add(new TaskModel("title3prog","title 3 description...","low","Shubham","inProgress"));
        inProgressTaskModelArrayList.add(new TaskModel("title4prog","title 4 description...","low","Prakhar","inProgress"));
        inProgressTaskModelArrayList.add(new TaskModel("title5prog","title 5 description...","high","Shivang","inProgress"));
    }

    public void notifyUpdateInProgressArrayList(){
        recyclerViewAdapter.notifyItemInserted(inProgressTaskModelArrayList.size()-1);
        recyclerView.scrollToPosition(inProgressTaskModelArrayList.size()-1);
    }
}