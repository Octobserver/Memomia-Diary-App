package com.example.memomia;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class Recent extends Fragment{

    private MainActivity myact;
    Context cntx;
    private RecyclerView recyclerView;
    JournalAdapter adapter; // Create Object of the Adapter class
    JournalSearchAdapter searchAdapter;
    DatabaseReference dbr; // Create object of the Firebase Realtime Database



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_recent, container, false);

        cntx = getActivity().getApplicationContext();
        myact = (MainActivity) getActivity();
        myact.getSupportActionBar().setTitle("Recent");

        dbr = FirebaseDatabase.getInstance().getReference();

        recyclerView = myview.findViewById(R.id.recycler1);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(cntx));

       Query query = dbr.child("JournalEntry").orderByChild("userId");
        FirebaseRecyclerOptions<JournalEntry> options
                = new FirebaseRecyclerOptions.Builder<JournalEntry>()
                .setQuery(query.equalTo(Access.userEmail).limitToLast(10), JournalEntry.class)
                .build();
        adapter = new JournalAdapter(options);

        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);
        adapter.notifyDataSetChanged();
        SearchView sv = myview.findViewById(R.id.searchView);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dbr = FirebaseDatabase.getInstance().getReference("JournalEntry");
                String[] tags = query.split(" ");

                dbr.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            ArrayList<JournalEntry> result = new ArrayList<JournalEntry>();
                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                JournalEntry j = ds.getValue(JournalEntry.class);
                                if (j != null){
                                    String id = j.getUserId();
                                    if (id != null && id.equals(Access.userEmail)) {
                                        String s = j.getTags();
                                        boolean match = true;
                                        for (String t:tags){
                                            if (!s.contains(t)){
                                                match = false;
                                                break;
                                            }
                                        }
                                        if (match) { result.add(j);}

                                    }
                                }
                            }
                            searchAdapter = new JournalSearchAdapter(getContext(), result);
                            recyclerView.setAdapter(searchAdapter);
                        }
                    }
                });

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return myview;
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override public void onStart()
    {
        super.onStart();
        adapter.startListening();




    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }






}