package com.example.thebeastnotesofworld.core.notes;

import com.example.thebeastnotesofworld.core.notes.Note;

public class CompletedToDoNote extends Note {
    private final String dateOfCompleted;
    private final int importance;

    public CompletedToDoNote(
            int id, String title, String text, String dateOfCreate, int importance, String dateOfCompleted) {
        super(id, title, text, dateOfCreate);
        this.importance = importance;
        this.dateOfCompleted = dateOfCompleted;
    }

    public String getDateOfCompleted() {
        return dateOfCompleted;
    }

    public int getImportance() {
        return importance;
    }
}
