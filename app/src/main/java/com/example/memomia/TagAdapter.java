package com.example.memomia;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder>{

    private LayoutInflater inflater;
    private NewActivity na;
    protected List<String> tags;

    public TagAdapter(Context context, NewActivity na, String tags) {
        inflater = LayoutInflater.from(context);
        this.na = na;
        if (tags.equals("")) {
            this.tags = new ArrayList<>();
        } else {
            this.tags = new ArrayList<>(Arrays.asList(tags.split(" ")));
        }

    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tag_layout, parent, false);
        TagViewHolder holder = new TagViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        //String key = position + "_key";
        String tag = tags.get(position);
        holder.tagName.setText(tag);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("delete tag", "onClick: delete");
                boolean result = na.entry.deleteTag(tag);
                Log.d("delete entry tags", tag);
                Log.d("delete entry tags", na.entry.getTags());
                tags.remove(tag);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class TagViewHolder extends RecyclerView.ViewHolder
    {
        TextView tagName;
        ImageButton delete;

        public TagViewHolder(View itemView) {
            super(itemView);
            tagName = (TextView)itemView.findViewById(R.id.tag_name);
            delete = itemView.findViewById(R.id.delete);
        }
    }

}
