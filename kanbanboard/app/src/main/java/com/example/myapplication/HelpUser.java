package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityHelpUserBinding;
import com.example.myapplication.databinding.ActivityWorkSpaceBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HelpUser extends DrawerBase {

    private FloatingActionButton floatingActionButton;
    ActivityHelpUserBinding activityHelpUserBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHelpUserBinding = ActivityHelpUserBinding.inflate(getLayoutInflater());
        setContentView(activityHelpUserBinding.getRoot());
        allocateActivityTitle("Help");
    }
}