package com.example.thebeastnotesofworld.core.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.MyCalendar;
import com.example.thebeastnotesofworld.core.Note;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Note> noteList;
    private OnNoteClickListener onNoteClickListener;

    public RVAdapter(Context context, List<Note> noteList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.noteList = noteList;
    }

    public interface OnNoteClickListener {
        void onNoteClick(int position);
        void onLongClick(int position);
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.textViewTitle.setText(note.getTitle());
        int dayToDeadline = MyCalendar.calculateDayToDeadline(note);
        if (dayToDeadline > 0) {
            holder.textViewDeadline.setText(String.valueOf(dayToDeadline));
        } else {
            holder.textViewDeadline.setText("Время вышло!");
        }
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
        holder.cardViewItemNote.setCardBackgroundColor(colorId);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textViewTitle;
        final TextView textViewDeadline;
        final CardView cardViewItemNote;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewItemNoteTitle);
            textViewDeadline = itemView.findViewById(R.id.textViewItemDeadline);
            cardViewItemNote = itemView.findViewById(R.id.cardViewItemNote);
            itemView.setOnClickListener(view -> {
                if (onNoteClickListener != null) {
                    onNoteClickListener.onNoteClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(view -> {
                if (onNoteClickListener != null) {
                    onNoteClickListener.onLongClick(getAdapterPosition());
                }
                return true;
            });
        }
    }
}
