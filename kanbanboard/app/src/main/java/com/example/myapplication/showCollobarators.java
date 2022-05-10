package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

public class showCollobarators extends AppCompatActivity {

    private final ArrayList<collabListModel> collablist = new ArrayList<>();
    private  int wid;
    private showCollobaratorsAdapter collobaratorsAdapter;
    private Handler wHandler;

    @Override
    protected void onStart() {
//        wHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                fetchData();
//                collobaratorsAdapter.notifyDataSetChanged();
//            }
//        });
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_collobarators);
        wid = getIntent().getIntExtra("wid",-1);
        wHandler = new Handler(Looper.getMainLooper());
        RecyclerView collabspacerecyvlerview = findViewById(R.id.collab_view);
        collobaratorsAdapter = new showCollobaratorsAdapter(collablist);
        collabspacerecyvlerview.setAdapter(collobaratorsAdapter);
        fetchData();



    }

    private void fetchData() {
        try {
//            collablist.clear();
            JSONObject x = new JSONObject();
            x.put("wid", wid);
            OkHttpClient client = new OkHttpClient();
            // media type to json, to inform the data is in json format
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // request body.
            RequestBody data = RequestBody.create(x.toString(), JSON);
            // create request.
            Request rq = new Request.Builder().url(ServerURL.showcollaborators).post(data).build();
            client.newCall(rq).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i("Workspace", "Error in trying to fetch all workspaces");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body().string();
                    try {
                        Log.i("collabs", res);
                        JSONArray workspaces = new JSONObject(res).getJSONArray("collabs");
                        for(int i =0; i < workspaces.length(); i++) {
                            JSONObject x = workspaces.getJSONObject(i);
                            collablist.add(new collabListModel(
                                    x.getString("collab_name"),
                                    x.getString("leader"),
                                    x.getInt("wid")));
                            Log.i("collab", collablist.toString());
                        }
                        wHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                collobaratorsAdapter.notifyDataSetChanged();
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