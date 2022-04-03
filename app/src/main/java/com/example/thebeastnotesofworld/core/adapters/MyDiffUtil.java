package com.example.thebeastnotesofworld.core.adapters;

import androidx.recyclerview.widget.DiffUtil;

import com.example.thebeastnotesofworld.core.Note;

import java.util.List;

public class MyDiffUtil extends DiffUtil.Callback {

    private final List<Note> oldList;
    private final List<Note> newList;

    public MyDiffUtil(List<Note> oldList, List<Note> newList) {
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

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = oldList.get(oldItemPosition);
        Note newNote = newList.get(newItemPosition);
        return oldNote.getId() == newNote.getId();
    }


    // Адаптер ничего не знает о полях text и dateOfCreate, их можно не сравнивать
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = oldList.get(oldItemPosition);
        Note newNote = newList.get(newItemPosition);
        return oldNote.getTitle().equals(newNote.getTitle()) &&
                oldNote.getImportance() == newNote.getImportance() &&
                oldNote.getDayToDeadLine() == newNote.getDayToDeadLine();
    }
}
