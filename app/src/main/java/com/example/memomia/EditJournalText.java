package com.example.memomia;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class EditJournalText extends Fragment {

    protected int mood;
    protected EditText editText;
    private ImageButton save;
    private ImageButton upload;
    private ImageButton happy;
    private ImageButton sad;
    private ImageButton normal;
    private ImageButton angry;
    private ImageButton sick;

    protected NewActivity parent;
    private FragmentTransaction transaction;

    private static final int HAPPY = 1;
    private static final int SAD = 2;
    private static final int NORMAL = 3;
    private static final int ANGRY = 4;
    private static final int SICK = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_journal_text, container, false);


        editText = view.findViewById(R.id.display_text);
        save = view.findViewById(R.id.close);
        upload = view.findViewById(R.id.upload);
        parent = (NewActivity) getActivity();

        //editText.setText(parent.entry.getText());

        happy = view.findViewById(R.id.happy);
        sad = view.findViewById(R.id.sad);
        normal = view.findViewById(R.id.normal);
        angry = view.findViewById(R.id.angry);
        sick = view.findViewById(R.id.sick);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send data to Dbase
                String txt = editText.getText().toString();
                if (txt.trim().equals("") || mood == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "No text written and/or mood chosen!", LENGTH_SHORT).show();
                    return;
                }
                parent.entry.setText(txt);
                parent.entry.setMood(mood);
                Log.d("text", parent.entry.getText());
                Log.d("id", parent.entry.getEntryId());
                addDatatoFirebase();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to Upload Image fragment
                transaction = parent.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.new_activity_frag_container, parent.uploadImage);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mood = HAPPY;
                Toast.makeText(getActivity().getApplicationContext(), "happy", LENGTH_SHORT).show();
            }
        });
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mood = SAD;
                Toast.makeText(getActivity().getApplicationContext(), "sad", LENGTH_SHORT).show();
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mood = NORMAL;
                Toast.makeText(getActivity().getApplicationContext(), "normal", LENGTH_SHORT).show();
            }
        });
        angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mood = ANGRY;
                Toast.makeText(getActivity().getApplicationContext(), "angry", LENGTH_SHORT).show();
            }
        });
        sick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mood = SICK;
                Toast.makeText(getActivity().getApplicationContext(), "sick", LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void addDatatoFirebase() {
        // we use add value event listener method
        // which is called with database reference.
        parent.dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                parent.dbr.child("JournalEntry").child(parent.entry.getEntryId()).setValue(parent.entry);

                // after adding this data we are showing toast message.
                Toast.makeText(getActivity().getApplicationContext(), "entry added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(getActivity().getApplicationContext(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}