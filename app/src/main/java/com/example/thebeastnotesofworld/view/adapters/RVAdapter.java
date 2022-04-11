package com.example.thebeastnotesofworld.view.adapters;

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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.ToDoNote;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<ToDoNote> toDoNoteList;
    private OnNoteClickListener onNoteClickListener;

    public RVAdapter(Context context, List<ToDoNote> toDoNoteList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.toDoNoteList = toDoNoteList;
    }

    public interface OnNoteClickListener {
        void onNoteClick(int position);
        void onLongClick(int position);
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }


    public void updateList (List<ToDoNote> newList) {
        MyDiffUtil diffUtil = new MyDiffUtil(toDoNoteList, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtil);
        result.dispatchUpdatesTo(this);
        toDoNoteList.clear();
        toDoNoteList.addAll(newList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_note, parent);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoNote toDoNote = toDoNoteList.get(position);
        holder.textViewTitle.setText(toDoNote.getTitle());
        int dayToDeadline = toDoNote.calculateDayToDeadline();
        if (dayToDeadline > 0) {
            holder.textViewDeadline.setText(String.valueOf(dayToDeadline));
        } else {
            holder.textViewDeadline.setText("Время вышло!");
        }
        int colorId;
        switch (toDoNote.getImportance()) {
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
        return toDoNoteList.size();
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
