package com.example.memomia;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Display_activity extends AppCompatActivity {


    //Views
    private ImageView bookmarkClosed;
    private RelativeLayout bookmarkOpen;
    protected Display_Frag displayText;
    protected Display_image_Frag uploadImage;
    private RecyclerView tagList;
    private ImageButton edit;

    //Adapter
    private TagDisplayAdapter ta;

    //Data
    protected int year;
    protected int month;
    protected int day;
    protected String entry_id;
    protected static ArrayList<String> listUris = new ArrayList<>();

    //DBase
    protected DatabaseReference dbr;

    //Generic
    private static final GenericTypeIndicator<String> t = new GenericTypeIndicator<String> () {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        dbr = FirebaseDatabase.getInstance().getReference();


        Intent intent = getIntent();
        String userId = Access.userEmail;
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        day = intent.getIntExtra("day",0);
        entry_id = String.format("memomia-entry-%d-%04d-%02d-%02d", userId.hashCode(), year, month, day);

        bookmarkClosed = findViewById(R.id.bookmark_closed_display);
        bookmarkOpen = findViewById(R.id.relative_bookmark_open_display);

        bookmarkOpen.setVisibility(View.INVISIBLE);
        bookmarkClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.INVISIBLE);
                bookmarkOpen.setVisibility(View.VISIBLE);
            }
        });

        bookmarkOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.INVISIBLE);
                bookmarkClosed.setVisibility(View.VISIBLE);
            }
        });

        tagList = findViewById(R.id.display_tag_list);
        DatabaseReference ref = dbr.child("JournalEntry").child(entry_id).child("tags");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("tag", "onDataChange: ");
                String tags = dataSnapshot.getValue(t);
                if (tags != null) {
                    Log.d("tag", "not null");
                    ta = new TagDisplayAdapter(getApplicationContext(), tags);
                    tagList.setLayoutManager(new LinearLayoutManager(Display_activity.this));
                    tagList.setAdapter(ta);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Display_activity.this, "Unable to load tags", Toast.LENGTH_SHORT).show();
            }

        });

        edit = findViewById(R.id.display_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Display_activity.this, EditEntry.class);
                intent.putExtra("entryId", entry_id);
                startActivity(intent);
            }
        });

        displayText = new Display_Frag();
        uploadImage = new Display_image_Frag(Display_activity.this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.display_activity_frag_container, displayText).commit();


        ImageButton close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listUris.clear();
                //finish();
                //For temporary suppression of recycler view exception
                Intent intent = new Intent(Display_activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public int getYear(){
        return year;
    }
    public int getMonth(){
        return month;
    }
    public int getDay(){
        return day;
    }

}