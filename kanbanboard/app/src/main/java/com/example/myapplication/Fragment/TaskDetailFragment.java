package com.example.myapplication.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.myapplication.Model.TaskModel;
import com.example.myapplication.R;

public class TaskDetailFragment extends Fragment {

    private TextView titleTV,descriptionTV,priorityTV,assigneeTV;
    private RadioGroup radioGroup;
    private TaskModel taskModel;
    private static final String open = "open";
    private static final String inProgress = "inProgress";
    private static final String close = "close";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        titleTV = view.findViewById(R.id.idTVTaskTitle);
        descriptionTV = view.findViewById(R.id.idTVTaskDescription);
        priorityTV = view.findViewById(R.id.idTVTaskPriority);
        assigneeTV = view.findViewById(R.id.idTVTaskAssignee);
        radioGroup = view.findViewById(R.id.idRG);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            taskModel = (TaskModel) bundle.getSerializable(getString(R.string.task_model_object));
        }
        titleTV.setText(taskModel.getTitle());
        descriptionTV.setText(taskModel.getDescription());
        priorityTV.setText(taskModel.getPriority());
        assigneeTV.setText(taskModel.getAssignee());
        String status = taskModel.getStatus();
        switch(status){
            case open:
                RadioButton openRB = view.findViewById(R.id.idRBOpen);
                openRB.setChecked(true);
                break;
            case inProgress:
                RadioButton inProgressRB = view.findViewById(R.id.idRBInProgress);
                inProgressRB.setChecked(true);
                break;
            case close:
                RadioButton closeRB= view.findViewById(R.id.idRBClose);
                closeRB.setChecked(true);
                break;
        }

        return view;
    }
}