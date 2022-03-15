package com.example.myapplication.Fragment;

import static com.example.myapplication.Fragment.StatusCloseFragment.closeTaskModelArrayList;
import static com.example.myapplication.Fragment.StatusInProgressFragment.inProgressTaskModelArrayList;
import static com.example.myapplication.Fragment.StatusOpenFragment.openTaskModelArrayList;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.Model.TaskModel;
import com.example.myapplication.R;

public class AddTaskDialogFragment extends DialogFragment {

    private EditText titleET,descriptionET,priorityET,assigneeET;
    private RadioGroup radioGroupRG;
    private static final String open = "open";
    private static final String inProgress = "inProgress";
    private static final String close = "close";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        build.setTitle("Add Task").setView(inflater.inflate(R.layout.dialog_add_task,null))
                .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(titleET.getText().toString().isEmpty() || descriptionET.getText().toString().isEmpty() ||
                                    priorityET.getText().toString().isEmpty() || assigneeET.getText().toString().isEmpty() || radioGroupRG.getCheckedRadioButtonId()==-1){
                            Toast.makeText(getContext(), "Please enter all details!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String title = titleET.getText().toString();
                            String description = descriptionET.getText().toString();
                            String priority = priorityET.getText().toString();
                            String assignee = assigneeET.getText().toString();
                            String status = null;
                            switch(radioGroupRG.getCheckedRadioButtonId()) {
                                case R.id.idRBDialogOpen:
                                    status = open;
                                    openTaskModelArrayList.add(new TaskModel(title,description,priority,assignee,status));
//                                    StatusOpenFragment statusOpenFragment = (StatusOpenFragment) getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.status_open_fragment));
//                                    statusOpenFragment.notifyUpdateOpenArrayList();
                                    break;
                                case R.id.idRBDialogInProgress:
                                    status = inProgress;
                                    inProgressTaskModelArrayList.add(new TaskModel(title,description,priority,assignee,status));
//                                    StatusInProgressFragment statusInProgressFragment = (StatusInProgressFragment) getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.status_inprogress_fragment));
//                                    statusInProgressFragment.notifyUpdateInProgressArrayList();
                                    break;
                                case R.id.idRBDialogClose:
                                    status = close;
                                    closeTaskModelArrayList.add(new TaskModel(title,description,priority,assignee,status));
//                                    StatusCloseFragment statusCloseFragment = (StatusCloseFragment) getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.status_close_fragment));
//                                    statusCloseFragment.notifyUpdateCloseArrayList();
                                    break;
                            }

                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        AddTaskDialogFragment.this.getDialog().cancel();
                    }
                });
        return build.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        titleET = getDialog().findViewById(R.id.idETTitle);
        descriptionET = getDialog().findViewById(R.id.idETDescription);
        priorityET = getDialog().findViewById(R.id.idETPriority);
        assigneeET = getDialog().findViewById(R.id.idETAssignee);
        radioGroupRG = getDialog().findViewById(R.id.idRGDialog);
    }
}
