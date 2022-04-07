package com.example.thebeastnotesofworld.view.adapters;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;

import com.example.thebeastnotesofworld.core.ToDoNote;

import java.util.List;

public class MyDiffUtil extends DiffUtil.Callback {

    private final List<ToDoNote> oldList;
    private final List<ToDoNote> newList;

    public MyDiffUtil(List<ToDoNote> oldList, List<ToDoNote> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        ToDoNote oldToDoNote = oldList.get(oldItemPosition);
        ToDoNote newToDoNote = newList.get(newItemPosition);
        return oldToDoNote.getId() == newToDoNote.getId();
    }


    // Адаптер ничего не знает о полях text и dateOfCreate, их можно не сравнивать
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ToDoNote oldToDoNote = oldList.get(oldItemPosition);
        ToDoNote newToDoNote = newList.get(newItemPosition);
        return oldToDoNote.getTitle().equals(newToDoNote.getTitle()) &&
                oldToDoNote.getImportance() == newToDoNote.getImportance() &&
                oldToDoNote.getDayToDeadLine() == newToDoNote.getDayToDeadLine();
    }
}
