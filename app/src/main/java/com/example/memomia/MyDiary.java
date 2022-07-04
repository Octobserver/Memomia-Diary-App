package com.example.memomia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class MyDiary extends Fragment {

    private MainActivity myact;
    Context cntx;
    private TabLayout tl;
    MCalendarView myCal;
    private DatabaseReference database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_my_diary, container, false);


        myact = (MainActivity) getActivity();
        myact.getSupportActionBar().setTitle("My Diary");

        tl = myview.findViewById(R.id.cal_tab);
        tl.bringToFront();
        TabLayout.Tab all = tl.newTab();
        all.setText("ALL");
        tl.addTab(all, true);
        TabLayout.Tab happy = tl.newTab();
        happy.setText("HAPPY");
        tl.addTab(happy);
        TabLayout.Tab idea = tl.newTab();
        idea.setText("IDEA");
        tl.addTab(idea);
        TabLayout.Tab trips = tl.newTab();
        trips.setText("TRIPS");
        tl.addTab(trips);

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


        myCal = myview.findViewById(R.id.calendar);
        myCal.setMarkedStyle(MarkStyle.DEFAULT, Color.rgb(162,187,231));
        myCal.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                if (myCal.getMarkedDates().getAll().contains(date) && date.getYear() != 0) {
                    Intent intent = new Intent(getActivity(), Display_activity.class);
                    Log.d("year", Integer.toString(date.getYear()));
                    intent.putExtra("year", date.getYear());
                    intent.putExtra("month", date.getMonth());
                    intent.putExtra("day", date.getDay());

                    startActivity(intent);
                }

            }
        });
        //myCal.markDate(2022,4,20);
        //myCal.markDate(2022,4,19);


        database = FirebaseDatabase.getInstance().getReference();
        database.child("JournalEntry").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        JournalEntry j = ds.getValue(JournalEntry.class);
                        if (j != null){
                            String id = j.getUserId();
                            if (id != null && id.equals(Access.userEmail)) {
                                int y = j.getYear();
                                int m = j.getMonth();
                                int d = j.getDay();
                                myCal.markDate(y,m,d);
                            }

                        }
                    }
                }
            }
        });


        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                JournalEntry j = dataSnapshot.getValue(JournalEntry.class);
                if (j != null){
                    String id = j.getUserId();
                    if (id != null && id.equals(Access.userEmail)) {
                        int y = j.getYear();
                        int m = j.getMonth();
                        int d = j.getDay();
                        myCal.markDate(y,m,d);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        };
        database.addValueEventListener(listener);


        return myview;
    }








}