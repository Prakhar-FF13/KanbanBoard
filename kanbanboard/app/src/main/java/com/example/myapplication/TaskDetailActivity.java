package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.CommentModel;
import com.example.myapplication.Model.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText titleET,descriptionET,assigneeET,createdOnET,commentET;
    private RadioGroup priorityRadioGroup, statusRadioGroup;
    private TaskModel taskModel;
    private Button updateTaskBtn;
    private TextView commentCountTV;
    private FloatingActionButton addCommentBtn;
    private RecyclerView recyclerView;
    private ArrayList<CommentModel> commentModelArrayList;
    private CommentAdapter commentAdapter;
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
    String username="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        this.id = getIntent().getIntExtra("id", -1);
        titleET = findViewById(R.id.idETTaskTitle);
        descriptionET = findViewById(R.id.idETTaskDescription);
        assigneeET = findViewById(R.id.idETTaskAssignee);
        createdOnET = findViewById(R.id.idETTaskCreatedOn);
        commentET = findViewById(R.id.idETComment);
        priorityRadioGroup = findViewById(R.id.idRGTaskPriority);
        statusRadioGroup = findViewById(R.id.idRGTaskStatus);
        updateTaskBtn = findViewById(R.id.idBtnUpdateTask);
        addCommentBtn = findViewById(R.id.idBtnAddComment);
        commentCountTV = findViewById(R.id.idTVCommentCount);

        commentModelArrayList = new ArrayList<>();
        loadComments();
        commentCountTV.setText("Comments   "+commentModelArrayList.size());
        recyclerView = findViewById(R.id.idRVComment);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(commentModelArrayList);
        recyclerView.setAdapter(commentAdapter);

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

        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentET.getText().toString().equals("")){
                    Toast.makeText(TaskDetailActivity.this, "Enter comment!", Toast.LENGTH_SHORT).show();
                }else{
                    String comment = commentET.getText().toString();
                    Date date = new Date();
                    long currentTimeMilli = date.getTime();
                    try {
                        username = WorkSpace.user.getString("username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //-----------------add to database---------------------
                    try {
                        JSONObject x = new JSONObject();
                        x.put("id", id);
                        x.put("timestamp", currentTimeMilli+"");
                        x.put("author", username);
                        x.put("comment", comment);

                        OkHttpClient client = new OkHttpClient();
                        // media type to json, to inform the data is in json format
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        // request body.
                        RequestBody data = RequestBody.create(x.toString(), JSON);
                        // create request.
                        Request rq = new Request.Builder().url(ServerURL.addComment).post(data).build();

                        client.newCall(rq).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.i("AddCommentOnTask", "Failed to create comment.");
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String res = response.body().string();
                                try {
                                    JSONObject x = new JSONObject(res);
                                    Log.i(TAG, "Comment Added Successfully!");
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            CommentModel cmt = new CommentModel(id,comment,currentTimeMilli+"",username);
                                            try {
                                                cmt.setCid(x.getInt("cid"));
                                            } catch (Exception e) {
                                                Log.i("AddCommentOnTask", "Error adding Cid to comments array");
                                                Log.i("AddCommentOnTask", e.getMessage());
                                            }
                                            commentModelArrayList.add(cmt);
                                            commentAdapter.notifyItemInserted(commentModelArrayList.size() - 1);
                                            recyclerView.scrollToPosition(commentModelArrayList.size()-1);
                                            commentET.setText("");
                                            Toast.makeText(TaskDetailActivity.this, "Comment posted successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.i("AddCommentOnTask", "Error in onResponse of add comment");
                                    Log.i("AddCommentOnTask", e.getMessage());
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.i("AddCommentOnTask", "Error preparing data for adding comment");
                    }
                }
            }
        });
    }

    private void loadComments() {
        //-----------------load comments with taskId=id from database---------------------
        try {
            JSONObject x = new JSONObject();
            x.put("id", id);

            OkHttpClient client = new OkHttpClient();
            // media type to json, to inform the data is in json format
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // request body.
            RequestBody data = RequestBody.create(x.toString(), JSON);
            // create request.
            Request rq = new Request.Builder().url(ServerURL.fetchComments).post(data).build();

            client.newCall(rq).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i("LoadComments", "Failed to load comments.");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body().string();
                    try {
                        JSONObject x = new JSONObject(res);
                        JSONArray cmts = x.getJSONArray("comments");
                        for(int i =0; i < cmts.length(); i++) {
                            JSONObject c = cmts.getJSONObject(i);
                            CommentModel cmt = new CommentModel(
                                    c.getInt("taskId"),
                                    c.getString("comment"),
                                    c.getString("timestamp"),
                                    c.getString("author")
                            );
                            cmt.setCid(c.getInt("cid"));
                            commentModelArrayList.add(cmt);
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                commentAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        Log.i("LoadComments", "Error in onResponse of load comment");
                        Log.i("LoadComments", e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            Log.i("LoadComments", "Error preparing data for fetching comments from DB");
        }
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
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TaskDetailActivity.this, "Task Updated Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });
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