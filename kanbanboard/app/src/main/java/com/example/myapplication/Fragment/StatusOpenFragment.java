package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.TaskModel;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViewAdapter;
import com.example.myapplication.ServerURL;
import com.example.myapplication.TaskDetailActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
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

public class StatusOpenFragment extends Fragment implements RecyclerViewAdapter.itemClickListener{

    private RecyclerView recyclerView;
    private ArrayList<TaskModel> openTaskModelArrayList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private FloatingActionButton addTaskFABtn;
    AutoCompleteTextView assigneeET;
    private int wid;
    private static final String open ="open";
    private static final String inProgress ="inProgress";
    private static final String close ="close";
    private static final String low = "low";
    private static final String medium = "medium";
    private static final String high = "high";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        wid = getArguments() != null ? (int) getArguments().get("wid") : -1;
        openTaskModelArrayList = new ArrayList<>();
        loadData();
        View view = inflater.inflate(R.layout.fragment_status_open, container, false);
        recyclerView = view.findViewById(R.id.idRVOpen);
        addTaskFABtn = view.findViewById(R.id.idAddTaskFABtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new RecyclerViewAdapter(openTaskModelArrayList,this);
        recyclerView.setAdapter(recyclerViewAdapter);

        addTaskFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view1 = inflater.inflate(R.layout.dialog_add_task, null);
        builder.setView(view1);

        EditText titleET,descriptionET;

        RadioGroup radioGroupRG;
        titleET = view1.findViewById(R.id.idETTitle);
        descriptionET = view1.findViewById(R.id.idETDescription);
        assigneeET = view1.findViewById(R.id.idETAssignee);
        radioGroupRG = view1.findViewById(R.id.idRGDialog);

        try {
            OkHttpClient client = new OkHttpClient();
            // media type to json, to inform the data is in json format
            JSONObject x = new JSONObject();
            x.put("wid", 1);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // request body.
            // create request.
            RequestBody data = RequestBody.create(x.toString(), JSON);
            Request rq = new Request.Builder().url(ServerURL.getUsers).post(data).build();
            client.newCall(rq).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("NetworkCall", e.toString());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    try {
                        JSONObject out = new JSONObject(res);
                        Log.i("message", out.getString("message"));
                        JSONArray arr = out.getJSONArray("returnusers");

                        String[] marr = new String[arr.length()];
                        for(int i=0;i< arr.length();i++){
                            JSONObject mmarr = arr.getJSONObject(i);
                            marr[i] = mmarr.getString("username");
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item, marr);
                                assigneeET.setAdapter(adapter);
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e) {
            Log.i("Workspace", "Error in create new workspace");
            Log.i("Workspace", e.toString());
        }

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(titleET.getText().toString().isEmpty() || descriptionET.getText().toString().isEmpty() ||
                        assigneeET.getText().toString().isEmpty() || radioGroupRG.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getContext(), "Please enter all details!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String title = titleET.getText().toString();
                    String description = descriptionET.getText().toString();
                    String assignee = assigneeET.getText().toString();
                    String priority = null;
                    switch(radioGroupRG.getCheckedRadioButtonId()) {
                        case R.id.idRBDialogLow:
                            priority = low;
                            break;
                        case R.id.idRBDialogMedium:
                            priority = medium;
                            break;
                        case R.id.idRBDialogHigh:
                            priority = high;
                    }
                    createTask(title,description,priority,assignee,open,java.time.LocalDate.now()+"");
                    recyclerViewAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(openTaskModelArrayList.size()-1);
                }
            }
        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        builder.create().show();
    }

    @Override
    public void onItemClick(TaskModel taskModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.task_model_object),taskModel);
        bundle.putInt("wid", wid);
        bundle.putInt("id", taskModel.getId());
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //load tasks from server with status as Open
    private void loadData() {
        try {
            JSONObject x = new JSONObject();
            x.put("wid", getArguments() != null ? getArguments().get("wid") : -1);
            x.put("status", "open");
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
                        JSONArray tasks;
                        try {
                            tasks = x.getJSONArray("workspaceTasks");
                        } catch (Exception e) {
                            Log.i("OpenFragment", "Error getting workspace tasks");
                            Log.i("OpenFragment", e.getMessage());
                            tasks = new JSONArray();
                        }
                        openTaskModelArrayList.clear();
                        for(int i = 0; i < tasks.length(); i++) {
                            JSONObject obj = tasks.getJSONObject(i);
                            openTaskModelArrayList.add(new TaskModel(
                                    obj.getInt("id"),
                                    obj.getString("title"),
                                    obj.getString("description"),
                                    obj.getString("priority"),
                                    obj.getString("assignee"),
                                    obj.getString("status"),
                                    obj.getString("date")
                            ));
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerViewAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        Log.i("OpenFragment", "Error in onResponse of add task");
                        Log.i("OpenFragment", e.getMessage());
                    }
                }
            });
        } catch (Exception e ) {
            Log.i("OpenFragment", "Error loading data from server");
            Log.i("OpenFragment", e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createTask(String title, String description, String priority, String assignee, String status,String date) {
        try {
            JSONObject x = new JSONObject();
            x.put("wid", getArguments() != null ? getArguments().getInt("wid"): -1);
            x.put("title", title);
            x.put("description", description);
            x.put("priority", priority);
            x.put("assignee", assignee);
            x.put("status", status);
            x.put("date",date);
            // client to send request.
            OkHttpClient client = new OkHttpClient();
            // media type to json, to inform the data is in json format
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // request body.
            RequestBody data = RequestBody.create(x.toString(), JSON);
            // create request.
            Request rq = new Request.Builder().url(ServerURL.createWorkspaceTask).post(data).build();

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
                        if(x.getString("message").equals("Collobarators do not exist")){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"Collobarators do not exist",Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(),"Please Add the Collaborators first",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else{
                            int id = x.getInt("id");
                            Bundle b = new Bundle();
                            b.putInt("wid", getArguments() != null ? getArguments().getInt("wid"): -1);
                            if (status.equals(open)) {
                                openTaskModelArrayList.add(new TaskModel(id, title, description, priority, assignee, status, date));
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }

                    } catch (Exception e) {
                        Log.i("AddTaskInWorkspace", "Error in onResponse of add task");
                        Log.i("AddTaskInWorkspace", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.i("CreateTask", "Failed Creating task");
            Log.i("CreateTask", e.getMessage());
        }

    }
}