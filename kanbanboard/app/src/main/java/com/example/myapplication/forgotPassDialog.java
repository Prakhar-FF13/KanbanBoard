package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class forgotPassDialog extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_forgotpassword, null);

        builder.setView(view)
                .setTitle("Change Password")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("Update Password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = editTextUsername.getText().toString();
                        String password = editTextPassword.getText().toString();
                        String confirmPassword = editTextConfirmPassword.getText().toString();


                        if(username == null || username.isEmpty())
                        {
                            Toast.makeText(getContext(), "Username field can't be empty.", Toast.LENGTH_SHORT).show();
                        }
                        else if (password == null || password.isEmpty())
                        {
                            Toast.makeText(getContext(), "Password field can't be empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (confirmPassword == null || confirmPassword.isEmpty())
                        {
                            Toast.makeText(getContext(), "Confirm password field can't be empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(LoginActivity.credential.containsKey(username)) {
                                if (password.equals(confirmPassword)) {
                                    LoginActivity.credential.put(username, password);
                                    Toast.makeText(getContext(), "Password update successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Password not matched with confirm password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getContext(), "User not found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        editTextUsername = view.findViewById(R.id.edit_forgotusername);
        editTextPassword = view.findViewById(R.id.edit_forgotpassword);
        editTextConfirmPassword = view.findViewById(R.id.edit_forgotconfirmpassword);
        return builder.create();
    }
}


