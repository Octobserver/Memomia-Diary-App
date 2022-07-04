package com.example.memomia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment recent;
    private Fragment myDiary;
    private Fragment myTags;
    private Fragment stats;

    private FloatingActionButton fab;
    private ArrayList<JournalEntry> catalog;
    //protected JournalAdapter ja;

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recent = new Recent();

        myTags = new MyTags();
        stats = new Stats();

        fab =findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, NewActivity.class);
                myIntent.putExtra("entryId", "0");
                startActivity(myIntent);
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, recent).commit();

        NavigationView navi = (NavigationView) findViewById(R.id.nav_view);
        navi.setNavigationItemSelectedListener(this);
        View head = navi.getHeaderView(0);
        TextView name = (TextView) head.findViewById(R.id.ownerID);
        //TODO: change Owner to Username
        String id = getIntent().getStringExtra("userId");
        name.setText(id);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.recent1) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, this.recent);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.my_diary) {
            myDiary = new MyDiary();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, this.myDiary);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.user_stats) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, this.stats);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }







}
