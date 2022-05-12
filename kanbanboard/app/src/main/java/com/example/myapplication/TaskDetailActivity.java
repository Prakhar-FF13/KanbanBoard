package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.CommentModel;
import com.example.myapplication.Model.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText titleET,descriptionET,assigneeET,createdOnET,commentET;
    private RadioGroup priorityRadioGroup, statusRadioGroup;
    private TaskModel taskModel;
    private Button updateTaskBtn;
    public static TextView commentCountTV;
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
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;
    String messageToSend ="",receiverContactNo="";

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

                    postComment(id,currentTimeMilli,username,comment);
//                    sendNotification();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendNotification();
                        }
                    });
                }
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void postComment(int id, long currentTimeMilli, String username, String comment) {
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
                                commentCountTV.setText("Comments   "+commentModelArrayList.size());
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
                                commentCountTV.setText("Comments   "+commentModelArrayList.size());
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

    private void sendNotification() {
        //send notification to current assignee of task
        Log.i("test","check");
        final String senderEmail = "02dixitaditya@gmail.com";
        final String senderPassword = "drfaztvzgkjgbpdw";
        messageToSend = username+" commented on the task assigned to you. Please have a look!";
        String subject = "Kanban Board";
        //String receiverEmail = findEmailFromAuthor();     //from taskmodel.getAuthor(), find email
        receiverContactNo = findContactFromAuthor();      //from taskmodel.getAuthor(), find contact
        String receiverEmail = "dixitadi02@gmail.com";
        receiverContactNo = "8376920458";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
            message.setSubject(subject);
            message.setText(messageToSend);
            Transport.send(message);
            Log.i("test","check2");
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }

        try {
            Log.i("Send SMS", "");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        catch (Exception e){
            e.printStackTrace();
            String message = e.getMessage();
            Log.i("in sms", message);
        }
    }

    private String findEmailFromAuthor() {
        String email = "";
        //find email of taskmodel.getAuthor()
        return email;
    }

    private String findContactFromAuthor() {
        String contact = "";
        //find contact of taskmodel.getAuthor()
        return contact;
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    if(receiverContactNo != null){
                        smsManager.sendTextMessage(receiverContactNo, "KanbanBoard",messageToSend , null, null);
                    }
                    Toast.makeText(getApplicationContext(), "SMS notification sent!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS notification sending failed!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

}