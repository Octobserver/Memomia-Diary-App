package com.example.memomia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class JournalAdapter extends FirebaseRecyclerAdapter<
        JournalEntry, JournalAdapter.journalsViewholder> {

    public JournalAdapter(@NonNull FirebaseRecyclerOptions<JournalEntry> options) {
        super(options);
    }

    /*public JournalAdapter(ArrayList<JournalEntry> result) {

    }*/

    // Function to bind the view in Card view(here
    // "journal_layout.xml") with data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull journalsViewholder holder, int position, @NonNull JournalEntry model) {

        holder.title.setText(model.getTitle());
        holder.dateTime.setText(model.getDateTime());

        switch (model.getMood()) {
            case 1:
                holder.moodIcon.setImageResource(R.drawable.happy);
                break;
            case 2:
                holder.moodIcon.setImageResource(R.drawable.sad);
                break;
            case 3:
                holder.moodIcon.setImageResource(R.drawable.neutral);
                break;
            case 4:
                holder.moodIcon.setImageResource(R.drawable.angry);
                break;
            case 5:
                holder.moodIcon.setImageResource(R.drawable.sick);
            default:
                break;
        }


        if (model.getImages() != null && !(model.getImages().isEmpty())) {
            String url = model.getImages().get("0_key");
            if (url != null) {
                Log.d("url", url);
                Picasso.get().load(url).centerCrop().fit().into(holder.bg);
                holder.bg.setAlpha(0.6f);
            }
        }
        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Display_activity.class);
                intent.putExtra("year", model.getYear());
                intent.putExtra("month", model.getMonth());
                intent.putExtra("day",model.getDay());
                view.getContext().startActivity(intent);
            }
        });

    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public journalsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_layout, parent, false);
        return new JournalAdapter.journalsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class journalsViewholder extends RecyclerView.ViewHolder {
        TextView title, dateTime;
        ImageView moodIcon, bg;
        public journalsViewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.journal_title);
            dateTime = itemView.findViewById(R.id.datetime);
            moodIcon = itemView.findViewById(R.id.mood_icon);
            bg = itemView.findViewById(R.id.bg);
        }
    }


}