package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragment.AddTaskDialogFragment;
import com.example.myapplication.Fragment.StatusCloseFragment;
import com.example.myapplication.Fragment.StatusInProgressFragment;
import com.example.myapplication.Fragment.StatusOpenFragment;

public class WorkspaceDetailActivity extends AppCompatActivity {

    private Button openBtn,inProgressBtn,closeBtn,addTaskBtn;
    private int wid;

    /*
    *   Assume you have data in the format:
    *  [ob1, obj2 , ....]
    *   ob1 = {
    *   id, //of task
    *   title,
    *   description,
    *   priority,
    *   status,
    *   assignee
    * }
    * */

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

//    @Override
//    protected void onRestart() {
//        Bundle b = new Bundle();
//        b.putInt("wid", wid);
//
//        StatusOpenFragment statusOpenFragment = new StatusOpenFragment();
//        statusOpenFragment.setArguments(b);
//        loadFragment(statusOpenFragment,getString(R.string.status_open_fragment));
//        super.onRestart();
//    }

    @Override
    protected void onResume() {
        Bundle b = new Bundle();
        b.putInt("wid", wid);

        StatusOpenFragment statusOpenFragment = new StatusOpenFragment();
        statusOpenFragment.setArguments(b);
        loadFragment(statusOpenFragment, getString(R.string.status_open_fragment));

        try {
            String status = getIntent().getStringExtra("status");
            Log.i("status",status);
            if (status.equals("open")) {
                 statusOpenFragment = new StatusOpenFragment();
                statusOpenFragment.setArguments(b);
                loadFragment(statusOpenFragment, getString(R.string.status_open_fragment));
            } else if (status.equals("inProgress")) {
                StatusInProgressFragment statusInProgressFragment = new StatusInProgressFragment();
                statusInProgressFragment.setArguments(b);
                loadFragment(statusInProgressFragment, getString(R.string.status_inprogress_fragment));
            } else if (status.equals("close")){
                StatusCloseFragment statusCloseFragment = new StatusCloseFragment();
                statusCloseFragment.setArguments(b);
                loadFragment(statusCloseFragment, getString(R.string.status_close_fragment));
            }
        }catch (Exception e){
             statusOpenFragment = new StatusOpenFragment();
            statusOpenFragment.setArguments(b);
            loadFragment(statusOpenFragment, getString(R.string.status_open_fragment));
        }
//        StatusInProgressFragment statusInProgressFragment = new StatusInProgressFragment();
//        statusInProgressFragment.setArguments(b);
//        loadFragment(statusInProgressFragment,getString(R.string.status_inprogress_fragment));
//
//        StatusCloseFragment statusCloseFragment = new StatusCloseFragment();
//        statusCloseFragment.setArguments(b);
//        loadFragment(statusCloseFragment,getString(R.string.status_close_fragment));

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_detail);
        openBtn = findViewById(R.id.idBtnOpen);
        inProgressBtn = findViewById(R.id.idBtnInProgress);
        closeBtn = findViewById(R.id.idBtnClose);
        addTaskBtn = findViewById(R.id.idBtnAddTask);
        wid = getIntent().getIntExtra("wid", -1);

        Bundle b = new Bundle();
        b.putInt("wid", wid);
        StatusOpenFragment statusOpenFragment = new StatusOpenFragment();
        statusOpenFragment.setArguments(b);
        loadFragment(statusOpenFragment, getString(R.string.status_open_fragment));
//
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBtn.setBackgroundColor(Color.RED);
                inProgressBtn.setBackgroundColor(getColor(R.color.purple_700));
                closeBtn.setBackgroundColor(getColor(R.color.purple_700));
                Log.i("wid", String.valueOf(wid));
                StatusOpenFragment statusOpenFragment = new StatusOpenFragment();
                statusOpenFragment.setArguments(b);
                loadFragment(statusOpenFragment,getString(R.string.status_open_fragment));
            }
        });

        inProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inProgressBtn.setBackgroundColor(Color.RED);
                openBtn.setBackgroundColor(getColor(R.color.purple_700));
                closeBtn.setBackgroundColor(getColor(R.color.purple_700));
                StatusInProgressFragment statusInProgressFragment = new StatusInProgressFragment();
                statusInProgressFragment.setArguments(b);
                loadFragment(statusInProgressFragment,getString(R.string.status_inprogress_fragment));
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeBtn.setBackgroundColor(Color.RED);
                openBtn.setBackgroundColor(getColor(R.color.purple_700));
                inProgressBtn.setBackgroundColor(getColor(R.color.purple_700));
                StatusCloseFragment statusCloseFragment = new StatusCloseFragment();
                statusCloseFragment.setArguments(b);
                loadFragment(statusCloseFragment,getString(R.string.status_close_fragment));
            }
        });

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        AddTaskDialogFragment addTaskDialogFragment = new AddTaskDialogFragment();
        Bundle b = new Bundle();
        b.putInt("wid", wid);
        addTaskDialogFragment.setArguments(b);
        addTaskDialogFragment.show(getSupportFragmentManager(),getString(R.string.add_task_dialog_fragment));
    }

    public void loadFragment(Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.idFrameLayout,fragment,tag);
        fragmentTransaction.commit();
    }

    public int getWid() {
        return this.wid;
    }
}