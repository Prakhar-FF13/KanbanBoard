package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterDialog extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private EditText editTextPassword;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_registerdialog, null);

        builder.setView(view)
                .setTitle("Registration")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            dismiss();
                    }
                })
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = editTextUsername.getText().toString();
                        String password = editTextPassword.getText().toString();

                        if(username == null || username.isEmpty())
                        {
                            Toast.makeText(getContext(), "Username field can't be empty.", Toast.LENGTH_SHORT).show();
                        }
                        else if (password == null || password.isEmpty())
                        {
                            Toast.makeText(getContext(), "Password field can't be empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            LoginActivity.credential.put("username", username);
                            LoginActivity.credential.put("password", password);
                            // create json object to pass as body of request. (from hashmap/map)
                            JSONObject creds = new JSONObject(LoginActivity.credential);
                            // client to send request.
                            OkHttpClient client = new OkHttpClient();
                            // media type to json, to inform the data is in json format
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            // request body.
                            RequestBody data = RequestBody.create(creds.toString(), JSON);
                            // create request.
                            Request rq = new Request.Builder().url("http://10.0.2.2:5000/register").post(data).build();
                            client.newCall(rq).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.i("NetworkCall", e.toString());
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.i("NetworkCall", response.body().string());
                                }
                            });
                        }
                    }
                });

        editTextUsername = view.findViewById(R.id.edit_registeredusername);
        editTextPassword = view.findViewById(R.id.edit_registeredpassword);

        return builder.create();
    }
}

