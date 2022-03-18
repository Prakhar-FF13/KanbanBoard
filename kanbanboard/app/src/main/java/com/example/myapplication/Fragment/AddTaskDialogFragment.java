package com.example.myapplication.Fragment;

import static com.example.myapplication.Fragment.StatusCloseFragment.closeTaskModelArrayList;
import static com.example.myapplication.Fragment.StatusInProgressFragment.inProgressTaskModelArrayList;
import static com.example.myapplication.Fragment.StatusOpenFragment.openTaskModelArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Model.TaskModel;
import com.example.myapplication.R;
import com.example.myapplication.ServerURL;
import com.example.myapplication.WorkspaceDetailActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddTaskDialogFragment extends DialogFragment {

    private EditText titleET,descriptionET,priorityET,assigneeET;
    private RadioGroup radioGroupRG;
    private static final String open = "open";
    private static final String inProgress = "inProgress";
    private static final String close = "close";
    String status = null;



    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Intent intent = new Intent(getContext(), WorkspaceDetailActivity.class);
        intent.putExtra("wid", getArguments().getInt("wid"));
        intent.putExtra("status",status);
        startActivity(intent);
        super.onDismiss(dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        build.setTitle("Add Task").setView(inflater.inflate(R.layout.dialog_add_task,null))
                .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(titleET.getText().toString().isEmpty() || descriptionET.getText().toString().isEmpty() ||
                                    priorityET.getText().toString().isEmpty() || assigneeET.getText().toString().isEmpty() || radioGroupRG.getCheckedRadioButtonId()==-1){
                            Toast.makeText(getContext(), "Please enter all details!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String title = titleET.getText().toString();
                            String description = descriptionET.getText().toString();
                            String priority = priorityET.getText().toString();
                            String assignee = assigneeET.getText().toString();
                            switch(radioGroupRG.getCheckedRadioButtonId()) {
                                case R.id.idRBDialogOpen:
                                    status = open;
//                                    StatusOpenFragment statusOpenFragment = (StatusOpenFragment) getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.status_open_fragment));
//                                    statusOpenFragment.notifyUpdateOpenArrayList();
                                    break;
                                case R.id.idRBDialogInProgress:
                                    status = inProgress;
//                                    StatusInProgressFragment statusInProgressFragment = (StatusInProgressFragment) getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.status_inprogress_fragment));
//                                    statusInProgressFragment.notifyUpdateInProgressArrayList();
                                    break;
                                case R.id.idRBDialogClose:
                                    status = close;
//                                    StatusCloseFragment statusCloseFragment = (StatusCloseFragment) getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.status_close_fragment));
//                                    statusCloseFragment.notifyUpdateCloseArrayList();
                                    break;
                            }
                            createTask(title,description,priority,assignee,status);
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        AddTaskDialogFragment.this.getDialog().cancel();
                    }
                });
        return build.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        titleET = getDialog().findViewById(R.id.idETTitle);
        descriptionET = getDialog().findViewById(R.id.idETDescription);
        priorityET = getDialog().findViewById(R.id.idETPriority);
        assigneeET = getDialog().findViewById(R.id.idETAssignee);
        radioGroupRG = getDialog().findViewById(R.id.idRGDialog);
    }

    public void loadFragment(Fragment fragment, String tag){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.idFrameLayout,fragment,tag);
        fragmentTransaction.commit();
    }

    private void createTask(String title, String description, String priority, String assignee, String status) {
        try {
            JSONObject x = new JSONObject();
            x.put("wid", getArguments() != null ? getArguments().getInt("wid"): -1);
            x.put("title", title);
            x.put("description", description);
            x.put("priority", priority);
            x.put("assignee", assignee);
            x.put("status", status);
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
                        int id = x.getInt("id");
                        Bundle b = new Bundle();
                        b.putInt("wid", getArguments() != null ? getArguments().getInt("wid"): -1);
                        if (status.equals(open)) {
                            openTaskModelArrayList.add(new TaskModel(id, title, description, priority, assignee, status));
                        }
                        else if (status.equals(inProgress)) {
                            inProgressTaskModelArrayList.add(new TaskModel(id, title, description, priority, assignee, status));
                        }
                        else {
                            closeTaskModelArrayList.add(new TaskModel(id, title, description, priority, assignee, status));
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
