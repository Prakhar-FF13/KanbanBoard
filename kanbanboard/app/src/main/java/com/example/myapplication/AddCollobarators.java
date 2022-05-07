package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private EditText mcollaboratorusername;
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

        maddcollaborator.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String collabname = mcollaboratorusername.getText().toString();
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
                            Log.i("NetworkCall", res);
//                            mcollaboratorusername.setText("");
                            try {
                                Toast.makeText(getApplicationContext(),"Collaborator added succesfully,",Toast.LENGTH_SHORT).show();
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
        });


    }
}