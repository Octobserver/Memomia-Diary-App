package com.example.memomia;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Display_Frag extends Fragment {

    protected Display_activity parent;
    private FragmentTransaction transaction;
    protected FirebaseAuth auth;
    protected DatabaseReference dbr;
    private TextView display_text;
    private TextView entry_title;
    private ImageView mood;
    private TextView date;
    private ImageView image;
    private ImageView image2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_, container, false);
        parent = (Display_activity) getActivity();
        display_text = view.findViewById(R.id.indisplay_text);
        entry_title = view.findViewById(R.id.entry_title);

        auth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference();
        String userId = Access.userEmail;

        Display_activity activity = (Display_activity) getActivity();
        int year = activity.getYear();
        int month = activity.getMonth();
        int day = activity.getDay();
        String entry_id = String.format("memomia-entry-%d-%04d-%02d-%02d", userId.hashCode(), year, month, day);


        DatabaseReference refText = dbr.child("JournalEntry").child(entry_id).child("text");
        refText.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String textinput = (String) dataSnapshot.getValue();
                display_text.setText(textinput);
            }
            public void onCancelled(DatabaseError firebaseError) {
                Log.e(TAG, "Failed to read text from database", firebaseError.toException());
            }
        });

        DatabaseReference refTitle = dbr.child("JournalEntry").child(entry_id).child("title");
        refTitle.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.getValue();
                entry_title.setText(title);
            }
            public void onCancelled(DatabaseError firebaseError) {
                Log.e(TAG, "Failed to read text from database", firebaseError.toException());
            }
        });




        mood = view.findViewById(R.id.mood);
        DatabaseReference refmood = dbr.child("JournalEntry").child(entry_id);
        refmood.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String moodinput = dataSnapshot.child("mood").getValue().toString();
                switch (moodinput) {
                    case "1":
                        mood.setBackgroundResource(R.drawable.happy);
                        break;
                    case "2":
                        mood.setBackgroundResource(R.drawable.sad);
                        break;
                    case "3":
                        mood.setBackgroundResource(R.drawable.neutral);
                        break;
                    case "4":
                        mood.setBackgroundResource(R.drawable.angry);
                        break;
                    case "5":
                        mood.setBackgroundResource(R.drawable.sick);
                        break;
                }
            }
            public void onCancelled(DatabaseError firebaseError) {
                Log.e(TAG, "Failed to read mood from database", firebaseError.toException());
            }
        });





        date = view.findViewById(R.id.date);
        DatabaseReference refdate = dbr.child("JournalEntry").child(entry_id).child("dateTime");
        refdate.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dateinput = (String) dataSnapshot.getValue();
                date.setText(dateinput);
            }
            public void onCancelled(DatabaseError firebaseError) {
                Log.e(TAG, "Failed to read date from database", firebaseError.toException());
            }
        });





        image = view.findViewById(R.id.imageButton_1);
        image2 = view.findViewById(R.id.imageButton_2);
        DatabaseReference refimage = dbr.child("JournalEntry").child(entry_id);
        refimage.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("images").exists()) {
                    int i = 0;
                    for (DataSnapshot issue: dataSnapshot.child("images").getChildren()) {
                        String u = (String) issue.getValue();
                        Log.d("uri", u);
                        if (i == 0) {
                            Display_activity.listUris.add(u);
                            Picasso.get().load(u).centerCrop().fit().into(image);
                            i++;
                        } else if (i == 1) {
                            Display_activity.listUris.add(u);
                            Picasso.get().load(u).centerCrop().fit().into(image2);
                            i++;
                        } else {
                            Display_activity.listUris.add(u);
                            //break;
                        }
                    }
                }
            }
            public void onCancelled(DatabaseError firebaseError) {
                Log.e(TAG, "Failed to read image from database", firebaseError.toException());
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = parent.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.display_activity_frag_container, parent.uploadImage);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = parent.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.display_activity_frag_container, parent.uploadImage);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }
}