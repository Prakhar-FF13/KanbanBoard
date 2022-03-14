package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
            etUsername.setText(savedInstanceState.getString("USERNAME"));
            etPassword.setText(savedInstanceState.getString("PASSWORD"));
            credential = (HashMap<String, String>) savedInstanceState.getSerializable("CREDENTIAL_MAP");
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etUsername.getText() == null || etUsername.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Enter username and Login again", Toast.LENGTH_SHORT).show();
                }
                else if(etPassword.getText() == null || etPassword.getText().toString() == "")
                {
                    Toast.makeText(getApplicationContext(),"Please Enter password and Login again", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(credential.containsKey(etUsername.getText().toString().toLowerCase()))
                   {
                       if(etPassword.getText().toString().toLowerCase().equals(credential.get(etUsername.getText().toString().toLowerCase())))
                            Toast.makeText(getApplicationContext(),etUsername.getText().toString().toLowerCase() +" login successfully", Toast.LENGTH_SHORT).show();
                       else
                           Toast.makeText(getApplicationContext(),"Password not matched", Toast.LENGTH_SHORT).show();
                   }
                   else {
                       Toast.makeText(getApplicationContext(),"User not found", Toast.LENGTH_SHORT).show();
                   }
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
        etUsername.setText(savedInstanceState.getString("USERNAME"));
        etPassword.setText(savedInstanceState.getString("PASSWORD"));
        credential = (HashMap<String, String>) savedInstanceState.getSerializable("CREDENTIAL_MAP");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("USERNAME",etUsername.getText().toString());
        outState.putString("PASSWORD",etPassword.getText().toString());
        outState.putSerializable("CREDENTIAL_MAP",credential);
        super.onSaveInstanceState(outState);
    }
}
