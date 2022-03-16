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

public class StatusCloseFragment extends Fragment implements RecyclerViewAdapter.itemClickListener{

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    public static ArrayList<TaskModel> closeTaskModelArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_close, container, false);
        loadData();
        recyclerView = view.findViewById(R.id.idRVClose);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new RecyclerViewAdapter(closeTaskModelArrayList,this);
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
        transaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.status_close_fragment)));
        transaction.add(R.id.idFrameLayout,taskDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //load tasks from server with status as Open
    private void loadData() {
        closeTaskModelArrayList.add(new TaskModel("title1close","title 1 description...","low","Prakhar","close"));
        closeTaskModelArrayList.add(new TaskModel("title2close","title 2 description...","medium","Rishabh","close"));
        closeTaskModelArrayList.add(new TaskModel("title3close","title 3 description...","high","Shubham","close"));
        closeTaskModelArrayList.add(new TaskModel("title4close","title 4 description...","low","Shivang","close"));
        closeTaskModelArrayList.add(new TaskModel("title5close","title 5 description...","medium","Aditya","close"));
        closeTaskModelArrayList.add(new TaskModel("title6close","title 6 description...","low","Prakhar","close"));
        closeTaskModelArrayList.add(new TaskModel("title7close","title 7 description...","medium","Rishabh","close"));
        closeTaskModelArrayList.add(new TaskModel("title8close","title 8 description...","high","Shubham","close"));
        closeTaskModelArrayList.add(new TaskModel("title9close","title 9 description...","low","Shivang","close"));
        closeTaskModelArrayList.add(new TaskModel("title0close","title 0 description...","medium","Aditya","close"));
    }

    public void notifyUpdateCloseArrayList(){
        recyclerViewAdapter.notifyItemInserted(closeTaskModelArrayList.size()-1);
        recyclerView.scrollToPosition(closeTaskModelArrayList.size()-1);
    }
}