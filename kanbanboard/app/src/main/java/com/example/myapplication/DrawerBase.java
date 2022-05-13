package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class DrawerBase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    String s=null;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activitycontainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername =  headerView.findViewById(R.id.nav_username);
        navUsername.setText(WorkSpace.un);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        s = this.getClass().getSimpleName();
        switch (item.getItemId()) {
            case  R.id.nav_home:
                if(s.equals("WorkSpace")==false){
                    Intent intent = new Intent(getApplicationContext(),WorkSpace.class);
                    intent.putExtra("user", WorkSpace.user.toString());
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                }
                    break;
            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(DrawerBase.this);
                builder.setTitle("Do You Really Want To Logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                        startActivity(new Intent(DrawerBase.this, LoginActivity.class));
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();

                break;
            case  R.id.nav_help:
                if(s.equals("HelpUser")==false){
                    startActivity(new Intent(this, HelpUser.class));
                    overridePendingTransition(0,0);
                    break;
                }
                    break;
        }
        return false;
    }

    protected void allocateActivityTitle(String titlestr){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(titlestr);
        }
    }

}
