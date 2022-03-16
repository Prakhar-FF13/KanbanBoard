package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername,etPassword;
    Button btnLogin;
    TextView tvForgotPassword,tvRegister;
    public static HashMap<String ,String > credential = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.inputUsername);
        etPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.forgotPassword);
        tvRegister = findViewById(R.id.gotoRegister);

        if(savedInstanceState != null)
        {
            etUsername.setText(savedInstanceState.getString("username"));
            etPassword.setText(savedInstanceState.getString("password"));
            credential = (HashMap<String, String>) savedInstanceState.getSerializable("CREDENTIAL_MAP");
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etUsername.getText() == null || etUsername.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Enter username and Login again", Toast.LENGTH_SHORT).show();
                }
                else if(etPassword.getText() == null || etPassword.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter password and Login again", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    credential.put("username", etUsername.getText().toString());
                    credential.put("password", etPassword.getText().toString());

                    // create json object to pass as body of request. (from hashmap/map)
                    JSONObject creds = new JSONObject(credential);
                    // client to send request.
                    OkHttpClient client = new OkHttpClient();
                    // media type to json, to inform the data is in json format
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    // request body.
                    RequestBody data = RequestBody.create(creds.toString(), JSON);
                    // create request.
                    Request rq = new Request.Builder().url(ServerURL.login).post(data).build();
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
                                Intent workspaceintent = new Intent(getApplicationContext(), WorkSpace.class);
                                startActivity(workspaceintent);
                            } catch (Exception e) {
                                Log.i("NetworkCall", e.getMessage());
                                Log.i("NetworkCall", "Error converting response from string to JSON");
                            }
                        }
                    });
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openRegisterDialog();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgotPassDialog();
            }
        });

    }

    public  void openRegisterDialog()
    {
        RegisterDialog regDialog = new RegisterDialog();
        regDialog.show(getSupportFragmentManager(), "register dialog");
    }

    public  void openForgotPassDialog()
    {
        forgotPassDialog fpDialog = new forgotPassDialog();
        fpDialog.show(getSupportFragmentManager(), "forgot password dialog");
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        etUsername.setText(savedInstanceState.getString("username"));
        etPassword.setText(savedInstanceState.getString("password"));
        credential = (HashMap<String, String>) savedInstanceState.getSerializable("CREDENTIAL_MAP");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("username",etUsername.getText().toString());
        outState.putString("password",etPassword.getText().toString());
        outState.putSerializable("CREDENTIAL_MAP",credential);
        super.onSaveInstanceState(outState);
    }
}
