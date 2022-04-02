package com.example.thebeastnotesofworld.core;

public class Note {
    private final int id;
    private final String title;
    private final String text;
    private final int importance;
    private final int dayToDeadLine;
    private final String dateOfCreate;


    public Note(int id, String title, String text, int importance, int dayToDeadLine, String dateOfCreate) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.importance = importance;
        this.dayToDeadLine = dayToDeadLine;
        this.dateOfCreate = dateOfCreate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getImportance() {
        return importance;
    }

    public int getDayToDeadLine() {
        return dayToDeadLine;
    }

    public String getDateOfCreate() {
        return dateOfCreate;
    }
}
