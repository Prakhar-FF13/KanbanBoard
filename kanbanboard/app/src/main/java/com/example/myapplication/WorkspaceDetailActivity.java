package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Fragment.StatusCloseFragment;
import com.example.myapplication.Fragment.StatusInProgressFragment;
import com.example.myapplication.Fragment.StatusOpenFragment;
import com.google.android.material.tabs.TabLayout;

public class WorkspaceDetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private int wid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_detail);
        wid = getIntent().getIntExtra("wid", -1);
        Bundle b = new Bundle();
        b.putInt("wid", wid);

        tabLayout = findViewById(R.id.idTabLayout);
        viewPager = findViewById(R.id.idViewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        StatusOpenFragment statusOpenFragment = new StatusOpenFragment();
        statusOpenFragment.setArguments(b);
        StatusInProgressFragment statusInProgressFragment = new StatusInProgressFragment();
        statusInProgressFragment.setArguments(b);
        StatusCloseFragment statusCloseFragment = new StatusCloseFragment();
        statusCloseFragment.setArguments(b);
        pagerAdapter.addFragment(statusOpenFragment, "Open");
        pagerAdapter.addFragment(statusInProgressFragment, "InProgress");
        pagerAdapter.addFragment(statusCloseFragment,"Closed");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public int getWid() {
        return this.wid;
    }
}