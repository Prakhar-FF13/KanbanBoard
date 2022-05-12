package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddCollobarators extends AppCompatActivity {

    private TextView mprojectname;
    private AutoCompleteTextView mcollaboratorusername;
    private Button maddcollaborator;
    private  int wid;
    private String leader, projectname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collobarators);
        mprojectname = findViewById(R.id.projectname);
        mcollaboratorusername = findViewById(R.id.collaborator_username);
        maddcollaborator = findViewById(R.id.add_collaborators);

        wid = getIntent().getIntExtra("wid",-1);
        leader = getIntent().getStringExtra("leader");
        projectname = getIntent().getStringExtra("projectname");

        mprojectname.setText(projectname);
//        final String[][] marr = new String[1][1];
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
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddCollobarators.this,android.R.layout.select_dialog_item, marr);
                                mcollaboratorusername.setAdapter(adapter);
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




        maddcollaborator.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String collabname = mcollaboratorusername.getText().toString();
                 boolean isExist = false;
                 try {

                    JSONObject x = new JSONObject();
                    x.put("wid", wid);
                    x.put("leader", leader);
                    x.put("username_collaborators",collabname );
                    OkHttpClient client = new OkHttpClient();
                    // media type to json, to inform the data is in json format
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    // request body.
                    RequestBody data = RequestBody.create(x.toString(), JSON);
                    // create request.
                    Request rq = new Request.Builder().url(ServerURL.addCollaborators).post(data).build();
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
                                if(out.getString("message").equals("User do not exist")){
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),"User Do not  Exist",Toast.LENGTH_SHORT).show();
                                            mcollaboratorusername.setText("");
                                            Toast.makeText(getApplicationContext(),"Please Sign Up the User first",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }else if(out.getString("message").equals("User already exist")){
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),"User Already Exist",Toast.LENGTH_SHORT).show();
                                            mcollaboratorusername.setText("");
                                            Toast.makeText(getApplicationContext(),"Please  add another user",Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }else{ new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Collaborator added succesfully,",Toast.LENGTH_SHORT).show();
                                        mcollaboratorusername.setText("");
                                        Toast.makeText(getApplicationContext(),"You can add new collaborators now",Toast.LENGTH_SHORT).show();
                                    }
                                });


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            Log.i("NetworkCall", res);
//
//                            try {
//                            } catch (Exception e) {
//                                Log.i("NetworkCall", e.getMessage());
//                                Log.i("NetworkCall", "Error converting response from string to JSON");
//                            }
                        }
                    });
                }
                catch (Exception e) {
                    Log.i("Workspace", "Error in create new workspace");
                    Log.i("Workspace", e.toString());
                }
//                Toast.makeText(getApplicationContext(),"Collaborator added succesfully,",Toast.LENGTH_SHORT).show();
//                mcollaboratorusername.setText("");
//                Toast.makeText(getApplicationContext(),"You can add new collaborators now",Toast.LENGTH_SHORT).show();

            }
        });


    }
}