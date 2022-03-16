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

public class StatusOpenFragment extends Fragment implements RecyclerViewAdapter.itemClickListener{

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    public static ArrayList<TaskModel> openTaskModelArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_open, container, false);
        loadData();
        recyclerView = view.findViewById(R.id.idRVOpen);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new RecyclerViewAdapter(openTaskModelArrayList,this);
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
        transaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.status_open_fragment)));
        transaction.add(R.id.idFrameLayout,taskDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //load tasks from server with status as Open
    private void loadData() {
        openTaskModelArrayList.add(new TaskModel("title1","title 1 description...","high","Aditya","open"));
        openTaskModelArrayList.add(new TaskModel("title2","title 2 description...","low","Shubham","open"));
        openTaskModelArrayList.add(new TaskModel("title3","title 3 description...","high","Shivang","open"));
        openTaskModelArrayList.add(new TaskModel("title4","title 4 description...","medium","Prakhar","open"));
        openTaskModelArrayList.add(new TaskModel("title5","title 5 description...","high","Rishabh","open"));
        openTaskModelArrayList.add(new TaskModel("title6","title 6 description...","high","Shivang","open"));
        openTaskModelArrayList.add(new TaskModel("title7","title 7 description...","medium","Prakhar","open"));
        openTaskModelArrayList.add(new TaskModel("title8","title 8 description...","high","Rishabh","open"));
    }

    public void notifyUpdateOpenArrayList(){
        recyclerViewAdapter.notifyItemInserted(openTaskModelArrayList.size()-1);
        recyclerView.scrollToPosition(openTaskModelArrayList.size()-1);
    }
}