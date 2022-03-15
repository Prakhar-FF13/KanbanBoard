package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class NewWorkSpaceDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText newWorkspaceName = new EditText(getActivity().getApplicationContext());
        final EditText newWorkspaceMemberCount = new EditText(getActivity().getApplicationContext());

        builder.setMessage("New WorkSpace")
                .setTitle("New WorkSpace")
                .setMessage("Enter New WorkSpace Name")
                .setView(newWorkspaceName)
                .setMessage("Enter New WorkSpace Member Count")
                .setView(newWorkspaceMemberCount)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!


                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

