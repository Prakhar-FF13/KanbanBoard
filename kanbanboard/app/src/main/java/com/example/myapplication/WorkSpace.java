package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class WorkSpace extends AppCompatActivity {

    private final ArrayList<WorkSpaceModel> workSpaceModelArrayList = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private JSONObject user;
    private WorkSpaceAdaptor adapter;
    private Handler wHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_space);
        RecyclerView workspaceRecyclerView = findViewById(R.id.workspace_view);
        adapter = new WorkSpaceAdaptor(workSpaceModelArrayList);
        workspaceRecyclerView.setAdapter(adapter);
        wHandler = new Handler(Looper.getMainLooper());
        try {
            this.user = new JSONObject(getIntent().getStringExtra("user"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Workspace", "Error in converting user object from intent.extra");
        }
        fetchData();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final EditText newWorkspaceName = new EditText(view.getContext());
                final TextView name = new TextView(view.getContext());
                name.setText("Enter New WorkSpace Name");
                LinearLayout layout = new LinearLayout(view.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(name);
                layout.addView(newWorkspaceName);
                builder.setMessage("New WorkSpace")
                        .setTitle("New WorkSpace")
                        .setView(layout)
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String name = newWorkspaceName.getText().toString();
                                try {
                                    JSONObject x = new JSONObject();
                                    x.put("name", name);
                                    x.put("createdBy", user.get("username"));
                                    OkHttpClient client = new OkHttpClient();
                                    // media type to json, to inform the data is in json format
                                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                    // request body.
                                    RequestBody data = RequestBody.create(x.toString(), JSON);
                                    // create request.
                                    Request rq = new Request.Builder().url(ServerURL.createWorkspace).post(data).build();
                                    client.newCall(rq).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Log.i("NetworkCall", e.toString());
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String res = response.body().string();
                                            Log.i("NetworkCall", res);
                                            try {
                                                JSONObject x = new JSONObject(res);
                                                workSpaceModelArrayList.add(new WorkSpaceModel(
                                                        x.getInt("wid"),
                                                        x.getString("name"),
                                                        x.getInt("members"),
                                                        x.getString("createdBy")
                                                ));
                                                wHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                });
                                            } catch (Exception e) {
                                                Log.i("NetworkCall", e.getMessage());
                                                Log.i("NetworkCall", "Error converting response from string to JSON");
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.i("Workspace", "Error in create new workspace");
                                    Log.i("Workspace", e.toString());
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
        });
    }
    private void fetchData() {
        try {
            JSONObject x = new JSONObject();
            x.put("username", this.user.getString("username"));
            OkHttpClient client = new OkHttpClient();
            // media type to json, to inform the data is in json format
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // request body.
            RequestBody data = RequestBody.create(x.toString(), JSON);
            // create request.
            Request rq = new Request.Builder().url(ServerURL.getWorkspaces).post(data).build();
            client.newCall(rq).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i("Workspace", "Error in trying to fetch all workspaces");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body().string();
                    try {
                        Log.i("Workspace", res);
                        JSONArray workspaces = new JSONObject(res).getJSONArray("workspaces");
                        for(int i =0; i < workspaces.length(); i++) {
                            JSONObject x = workspaces.getJSONObject(i);
                            workSpaceModelArrayList.add(new WorkSpaceModel(
                                    x.getInt("wid"),
                                    x.getString("name"),
                                    x.getInt("members"),
                                    x.getString("createdBy")));
                        }
                        wHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        Log.i("Workspace", e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            Log.i("Workspace", "Failed in fetching all workspaces");
            Log.i("Workspace", e.getMessage());
        }
    }
}