package com.example.thebeastnotesofworld.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CompletedToDoNote extends Note{
    private final String dateOfCompleted;
    private final int importance;

    public CompletedToDoNote(
            int id, String title, String text, String dateOfCreate, int importance) {
        super(id, title, text, dateOfCreate);
        this.importance = importance;
        this.dateOfCompleted = new SimpleDateFormat
                ("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
    }

    public String getDateOfCompleted() {
        return dateOfCompleted;
    }

    public int getImportance() {
        return importance;
    }
}
