package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

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
                            LoginActivity.credential.put(username, password);
                            Toast.makeText(getContext(), "Registration is successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        editTextUsername = view.findViewById(R.id.edit_registeredusername);
        editTextPassword = view.findViewById(R.id.edit_registeredpassword);

        return builder.create();
    }
}

