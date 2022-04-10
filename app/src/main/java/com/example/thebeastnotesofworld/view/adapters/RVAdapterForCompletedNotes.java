package com.example.thebeastnotesofworld.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebeastnotesofworld.core.CompletedToDoNote;

import java.util.List;

public class RVAdapterForCompletedNotes extends
        RecyclerView.Adapter<RVAdapterForCompletedNotes.ViewHolderCompleted> {
    private final LayoutInflater layoutInflater;
    private final List<CompletedToDoNote> list;

    public RVAdapterForCompletedNotes(LayoutInflater layoutInflater, List<CompletedToDoNote> list) {
        this.layoutInflater = layoutInflater;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderCompleted onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCompleted holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderCompleted extends RecyclerView.ViewHolder {

        public ViewHolderCompleted(@NonNull View itemView) {
            super(itemView);
        }
    }}
