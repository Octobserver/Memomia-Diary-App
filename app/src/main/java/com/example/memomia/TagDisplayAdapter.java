package com.example.memomia;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TagDisplayAdapter extends RecyclerView.Adapter<TagDisplayAdapter.TagViewHolder>{

    private LayoutInflater inflater;
    private List<String> tags;

    public TagDisplayAdapter(Context context, String tags) {
        inflater = LayoutInflater.from(context);
        this.tags = Arrays.asList(tags.split(" "));
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tag_display_layout, parent, false);
        TagViewHolder holder = new TagViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.tagName.setText(tags.get(position));

    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class TagViewHolder extends RecyclerView.ViewHolder
    {
        TextView tagName;

        public TagViewHolder(View itemView) {
            super(itemView);
            tagName = (TextView)itemView.findViewById(R.id.tag_name);

        }
    }

}

