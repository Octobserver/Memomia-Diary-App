package com.example.memomia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class EditEntry extends NewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_entry);
        entry = new JournalEntry();
        if (!entryId.equals("0")) {
            DatabaseReference ref = dbr.child("JournalEntry").child(entryId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    JournalEntry je = dataSnapshot.getValue(JournalEntry.class);
                    if (je == null) {
                        Log.d("Error", "Cannot read entry from data base");
                        return;
                    }
                    setEntry(je);
                    Log.d("Tags", je.getTags());
                    ta = new TagAdapter(getApplicationContext(), EditEntry.this, je.getTags());
                    tagList = findViewById(R.id.tags);
                    tagList.setLayoutManager(new LinearLayoutManager(EditEntry.this));
                    tagList.setAdapter(ta);
                    editJournalText.editText.setText(je.getText());
                    editJournalText.mood = je.getMood();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditEntry.this, "Unable to load entry", Toast.LENGTH_SHORT).show();
                }

            });

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setEntry(JournalEntry je) {

        this.entry.setText(je.getText());
        this.entry.setMood(je.getMood());
        this.entry.setDateTime(je.getDateTime());
        this.entry.setDay(je.getDay());
        this.entry.setMonth(je.getMonth());
        this.entry.setYear(je.getYear());
        this.entry.setTitle(je.getTitle());
        this.entry.setUserId(je.getUserId());
        this.entry.setEntryId(je.getEntryId());
        this.entry.setUserId(je.getUserId());
        this.entry.setTags(je.getTags());
        this.entry.setImages(je.getImages());
        this.entry.setImgIndex(je.getImgIndex());
    }

}