package com.example.thebeastnotesofworld.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.SimpleNote;

import java.util.List;

public class RVAdapterForSimpleNotes extends RecyclerView.Adapter<RVAdapterForSimpleNotes.SimpleNoteViewHolder> {
    private final List<SimpleNote> list;
    private final LayoutInflater layoutInflater;

    public RVAdapterForSimpleNotes(Context context, List<SimpleNote> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SimpleNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_simple_note, parent, false);
        return new SimpleNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleNoteViewHolder holder, int position) {
        SimpleNote note = list.get(position);
        holder.textViewTitle.setText(note.getTitle());
        holder.textViewText.setText(note.getText());
        holder.textViewDateOfCreate.setText(note.getDateOfCreate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SimpleNoteViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewTitle;
        final TextView textViewText;
        final TextView textViewDateOfCreate;
        public SimpleNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.textViewSimpleNoteTitle);
            this.textViewText = itemView.findViewById(R.id.textViewSimpleNoteText);
            this.textViewDateOfCreate = itemView.findViewById(R.id.textViewSimpleNoteDateOfCreate);
        }
    }
 }
