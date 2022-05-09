package com.example.thebeastnotesofworld.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.notes.CompletedToDoNote;

import java.util.List;

public class RVAdapterForCompletedNotes extends
        RecyclerView.Adapter<RVAdapterForCompletedNotes.ViewHolderCompleted> {
    private final LayoutInflater layoutInflater;
    private final List<CompletedToDoNote> list;
    private OnCompletedNoteClickListener onCompletedNoteClickListener;

    public RVAdapterForCompletedNotes(Context context, List<CompletedToDoNote> list) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    public interface OnCompletedNoteClickListener{
        void onCompletedNoteClick (int position);
    }

    public void setOnNoteClickListener(OnCompletedNoteClickListener onCompletedNoteClickListener) {
        this.onCompletedNoteClickListener = onCompletedNoteClickListener;
    }

    @NonNull
    @Override
    public ViewHolderCompleted onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_completed_note, parent, false);
        return new ViewHolderCompleted(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCompleted holder, int position){
        CompletedToDoNote note = list.get(position);
        holder.textViewTitle.setText(note.getTitle());
        holder.textViewText.setText(note.getText());
        String dateOfCreate = "Дата создания: " + note.getDateOfCreate();
        holder.textViewDateOfCreate.setText(dateOfCreate);
        String dateOfCompleted = "Дата завершения: " + note.getDateOfCompleted();
        holder.textViewDateOfCompleted.setText(dateOfCompleted);
        int colorId;
        switch (note.getImportance()) {
            case 1: colorId = ContextCompat.getColor(holder.itemView.getContext(), R.color.green);
                break;
            case 2: colorId = ContextCompat.getColor(holder.itemView.getContext(), R.color.yellow);
                break;
            case 3: colorId = ContextCompat.getColor(holder.itemView.getContext(), R.color.red);
                break;
            default: colorId = ContextCompat.getColor(holder.itemView.getContext(), R.color.white);
        }
        holder.cardViewCompleted.setCardBackgroundColor(colorId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderCompleted extends RecyclerView.ViewHolder {
        final TextView textViewTitle;
        final TextView textViewText;
        final TextView textViewDateOfCreate;
        final TextView textViewDateOfCompleted;
        final CardView cardViewCompleted;
        public ViewHolderCompleted(View itemView) {
            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.textViewCompletedTitle);
            this.textViewText = itemView.findViewById(R.id.textViewCompletedText);
            this.textViewDateOfCreate = itemView.findViewById(R.id.textViewCompletedDateOfCreate);
            this.textViewDateOfCompleted = itemView.findViewById(R.id.textViewCompletedDateOfEnd);
            this.cardViewCompleted = itemView.findViewById(R.id.cardViewItemCompletedNote);
            itemView.setOnClickListener(view -> {
                if (onCompletedNoteClickListener != null) {
                    onCompletedNoteClickListener.onCompletedNoteClick(getAdapterPosition());
                }
            });
        }


    }}
