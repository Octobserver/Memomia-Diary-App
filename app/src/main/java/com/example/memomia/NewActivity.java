package com.example.memomia;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;



public class NewActivity extends AppCompatActivity {

    //Elements
    protected ImageView bookmarkClosed;
    protected RelativeLayout bookmarkOpen;
    protected RecyclerView tagList;
    protected ImageButton addTag;

    //Adapter
    protected TagAdapter ta;

    //Fragments
    protected EditJournalText editJournalText;
    protected UploadImageFragment uploadImage;

    //Journal Entry
    //protected or public?
    protected JournalEntry entry;
    protected String entryId;

    //Dbase
    protected FirebaseAuth auth;
    protected DatabaseReference dbr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new);

        //Get entryId
        this.entryId = getIntent().getStringExtra("entryId");

        //Initialize views
        bookmarkClosed = findViewById(R.id.bookmark_closed);
        bookmarkOpen = findViewById(R.id.relative_bookmark_open);
        addTag = findViewById(R.id.add_tag);

        //initialize fragments
        editJournalText = new EditJournalText();
        //tags = new TagsFragment();
        uploadImage = new UploadImageFragment(this);

        //Initialize Dbase
        auth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference();

        //Handle bookmark view switch
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

        //Check if it is New or Edit Entry.
        if (this.entryId.equals("0")) {

            //initialize JournalEntry
            Date date = new Date();
            LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            int year  = localDate.getYear();
            int month = localDate.getMonthValue();
            int day   = localDate.getDayOfMonth();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);

            String dateTime = dtf.format(localDate);
            FirebaseUser currentUser = auth.getCurrentUser();
            entry = new JournalEntry(currentUser.getEmail(), dateTime, year, month, day);
            ta = new TagAdapter(getApplicationContext(), this, entry.getTags());
            tagList = findViewById(R.id.tags);
            tagList.setLayoutManager(new LinearLayoutManager(NewActivity.this));
            tagList.setAdapter(ta);

        }

    }

    @Override
    public void onStart() {

        super.onStart();

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewActivity.this);
                builder.setTitle("New Tag");

                // Set up the input
                final EditText input = new EditText(NewActivity.this);
                // Specify the type of input expected
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tag = input.getText().toString();
                        boolean result = entry.addTag(tag);
                        if (result) {
                            ta.tags.add(tag);
                            Log.d("added", tag);
                            ta.notifyItemInserted(ta.tags.size() - 1);
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.new_activity_frag_container, editJournalText).commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewActivity.this, MainActivity.class);
        startActivity(intent);
        //super.onBackPressed();
    }


}