package com.example.thebeastnotesofworld.core;

public abstract class Note {
    private final int id;
    private final String title;
    private final String text;
    private final String dateOfCreate;


    public Note(int id, String title, String text, String dateOfCreate) {
        this.id = id;
        this.title = title;
        this.text = text;
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

    public String getDateOfCreate() {
        return dateOfCreate;
    }
}
