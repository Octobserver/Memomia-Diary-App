package com.example.memomia;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class JournalSearchAdapter extends RecyclerView.Adapter<JournalSearchAdapter.JournalViewHolder>{

    private LayoutInflater inflater;
    //private NewActivity na;
    protected List<JournalEntry> journalEntryList;

    public JournalSearchAdapter(Context context, ArrayList<JournalEntry> searchResult) {
        inflater = LayoutInflater.from(context);
        this.journalEntryList = searchResult;
    }

    @Override
    public JournalSearchAdapter.JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.journal_layout, parent, false);
        JournalSearchAdapter.JournalViewHolder holder = new JournalSearchAdapter.JournalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(JournalSearchAdapter.JournalViewHolder holder, int position) {
        JournalEntry curr = journalEntryList.get(position);
        holder.title.setText(curr.getTitle());
        holder.dateTime.setText(curr.getDateTime());

        switch (curr.getMood()) {
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


        if (curr.getImages() != null && !(curr.getImages().isEmpty())) {
            String url = curr.getImages().get("0_key");
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
                intent.putExtra("year", curr.getYear());
                intent.putExtra("month", curr.getMonth());
                intent.putExtra("day",curr.getDay());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return journalEntryList.size();
    }

    class JournalViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, dateTime;
        ImageView moodIcon, bg;

        public JournalViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.journal_title);
            dateTime = itemView.findViewById(R.id.datetime);
            moodIcon = itemView.findViewById(R.id.mood_icon);
            bg = itemView.findViewById(R.id.bg);
        }
    }
}
