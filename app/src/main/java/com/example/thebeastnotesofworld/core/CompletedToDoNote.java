package com.example.thebeastnotesofworld.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CompletedToDoNote extends Note{
    private final String dateOfCompleted;

    public CompletedToDoNote(
            int id, String title, String text, String dateOfCreate) {
        super(id, title, text, dateOfCreate);
        this.dateOfCompleted = new SimpleDateFormat
                ("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
    }

    public String getDateOfCompleted() {
        return dateOfCompleted;
    }
}
