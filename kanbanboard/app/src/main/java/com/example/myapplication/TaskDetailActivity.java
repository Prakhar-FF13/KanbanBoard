package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.TaskModel;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText titleET,descriptionET,assigneeET,createdOnET;
    private RadioGroup priorityRadioGroup, statusRadioGroup;
    private TaskModel taskModel;
    private Button updateTaskBtn;
    private static final String TAG = "TaskDetailActivity";
    private static final String low = "low";
    private static final String medium = "medium";
    private static final String high = "high";
    private static final String open = "open";
    private static final String inProgress = "inProgress";
    private static final String close = "close";
    private String title,description,priority,assignee,createdOn,status;
    private int wid;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        this.id = getIntent().getIntExtra("id", -1);
        titleET = findViewById(R.id.idETTaskTitle);
        descriptionET = findViewById(R.id.idETTaskDescription);
        assigneeET = findViewById(R.id.idETTaskAssignee);
        createdOnET = findViewById(R.id.idETTaskCreatedOn);
        priorityRadioGroup = findViewById(R.id.idRGTaskPriority);
        statusRadioGroup = findViewById(R.id.idRGTaskStatus);
        updateTaskBtn = findViewById(R.id.idBtnUpdateTask);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            taskModel = (TaskModel) bundle.getSerializable(getString(R.string.task_model_object));
            wid = bundle.getInt("wid");
        }
        titleET.setText(taskModel.getTitle());
        descriptionET.setText(taskModel.getDescription());
        assigneeET.setText(taskModel.getAssignee());
        createdOnET.setText("23 Mar");
        priority = taskModel.getPriority();
        switch (priority){
            case low:
                RadioButton lowRB = findViewById(R.id.idRBLow);
                lowRB.setChecked(true);
                break;
            case medium:
                RadioButton mediumRB = findViewById(R.id.idRBMedium);
                mediumRB.setChecked(true);
                break;
            case high:
                RadioButton highRB = findViewById(R.id.idRBHigh);
                highRB.setChecked(true);
        }
        status = taskModel.getStatus();
        switch(status){
            case open:
                RadioButton openRB = findViewById(R.id.idRBOpen);
                openRB.setChecked(true);
                break;
            case inProgress:
                RadioButton inProgressRB = findViewById(R.id.idRBInProgress);
                inProgressRB.setChecked(true);
                break;
            case close:
                RadioButton closeRB= findViewById(R.id.idRBClose);
                closeRB.setChecked(true);
                break;
        }
        priorityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.idRBLow:
                        priority = low;
                        break;
                    case R.id.idRBMedium:
                        priority = medium;
                        break;
                    case R.id.idRBHigh:
                        priority = high;
                }
            }
        });

        statusRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.idRBOpen:
                        status = open;
                        break;
                    case R.id.idRBInProgress:
                        status = inProgress;
                        break;
                    case R.id.idRBClose:
                        status = close;
                }
            }
        });

        updateTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleET.getText().toString();
                description = descriptionET.getText().toString();
                assignee = assigneeET.getText().toString();
                updateTask(title,description,priority,assignee,status);
            }
        });
    }

    private void updateTask(String title, String description, String priority, String assignee, String status) {
        try {
            JSONObject x = new JSONObject();
            x.put("wid", wid);
            x.put("id", this.id);
            x.put("title", title);
            x.put("description", description);
            x.put("priority", priority);
            x.put("assignee", assignee);
            x.put("status", status);

            Log.i("UpdateTask", x.toString());
            // client to send request.
            OkHttpClient client = new OkHttpClient();
            // media type to json, to inform the data is in json format
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // request body.
            RequestBody data = RequestBody.create(x.toString(), JSON);
            // create request.
            Request rq = new Request.Builder().url(ServerURL.updateWorkspaceTask).post(data).build();

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
                        Log.i(TAG, "Task Updated Successfully!");
                    } catch (Exception e) {
                        Log.i("UpdateTask", "Error in onResponse of update task");
                        Log.i("UpdateTask", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.i("UpdateTask", "Failed Updating task");
            Log.i("UpdateTask", e.getMessage());
        }
    }


}