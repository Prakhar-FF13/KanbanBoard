package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.TaskModel;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViewAdapter;
import com.example.myapplication.ServerURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        try {
            JSONObject x = new JSONObject();
            x.put("wid", getArguments() != null ? getArguments().get("wid") : -1);
            x.put("status", "close");
            // client to send request.
            OkHttpClient client = new OkHttpClient();
            // media type to json, to inform the data is in json format
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // request body.
            RequestBody data = RequestBody.create(x.toString(), JSON);
            // create request.
            Request rq = new Request.Builder().url(ServerURL.getWorkspaceTasks).post(data).build();

            client.newCall(rq).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i("AddTaskInWorkspace", "Failed to create task.");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body().string();
                    try {
                        JSONObject x = new JSONObject(res);
                        JSONArray tasks = x.getJSONArray("workspaceTasks");
                        closeTaskModelArrayList = new ArrayList<>();
                        for(int i = 0; i < tasks.length(); i++) {
                            JSONObject obj = tasks.getJSONObject(i);
                            closeTaskModelArrayList.add(new TaskModel(
                                    obj.getInt("id"),
                                    obj.getString("title"),
                                    obj.getString("description"),
                                    obj.getString("priority"),
                                    obj.getString("assignee"),
                                    obj.getString("status")
                            ));
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerViewAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        Log.i("CloseFragment", "Error in onResponse of add task");
                        Log.i("CloseFragment", e.getMessage());
                    }
                }
            });
        } catch (Exception e ) {
            Log.i("CloseFragment", "Error loading data from server");
            Log.i("CloseFragment", e.getMessage());
        }
    }

    public void notifyUpdateCloseArrayList(){
        recyclerViewAdapter.notifyItemInserted(closeTaskModelArrayList.size()-1);
        recyclerView.scrollToPosition(closeTaskModelArrayList.size()-1);
    }
}