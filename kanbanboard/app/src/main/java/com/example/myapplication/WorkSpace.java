package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WorkSpace extends AppCompatActivity {

    private final ArrayList<WorkSpaceModel> workSpaceModelArrayList = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_space);
        RecyclerView workspaceRecyclerView = findViewById(R.id.workspace_view);
        workspaceRecyclerView.setAdapter(new WorkSpaceAdaptor(workSpaceModelArrayList));
        floatingActionButton = findViewById(R.id.floatingActionButton);
        setWorkspace();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final EditText newWorkspaceName = new EditText(view.getContext());
                final EditText newWorkspaceMemberCount = new EditText(view.getContext());
                final TextView name = new TextView(view.getContext());
                name.setText("Enter New WorkSpace Name");
                final TextView memeber = new TextView(view.getContext());
                memeber.setText("Enter New WorkSpace Member Count");
                LinearLayout layout = new LinearLayout(view.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(name);
                layout.addView(newWorkspaceName);
                layout.addView(memeber);
                layout.addView(newWorkspaceMemberCount);
                builder.setMessage("New WorkSpace")
                        .setTitle("New WorkSpace")
                        .setView(layout)
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String name = newWorkspaceName.getText().toString();
                                int member = Integer.parseInt(newWorkspaceMemberCount.getText().toString());
                                workSpaceModelArrayList.add(new WorkSpaceModel(name,member));
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

    private void  setWorkspace(){
        workSpaceModelArrayList.add(new WorkSpaceModel("WorkSpace 1",10));
        workSpaceModelArrayList.add(new WorkSpaceModel("WorkSpace 2",10));
        workSpaceModelArrayList.add(new WorkSpaceModel("WorkSpace 3",10));
        workSpaceModelArrayList.add(new WorkSpaceModel("WorkSpace 4",10));
        workSpaceModelArrayList.add(new WorkSpaceModel("WorkSpace 5",10));
        workSpaceModelArrayList.add(new WorkSpaceModel("WorkSpace 6",10));
        workSpaceModelArrayList.add(new WorkSpaceModel("WorkSpace 7",10));
        workSpaceModelArrayList.add(new WorkSpaceModel("WorkSpace 8",10));
        workSpaceModelArrayList.add(new WorkSpaceModel("WorkSpace 9",10));
        workSpaceModelArrayList.add(new WorkSpaceModel("WorkSpace 10",10));

    }


}