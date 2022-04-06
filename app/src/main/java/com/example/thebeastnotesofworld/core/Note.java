package com.example.thebeastnotesofworld.core;

import java.util.Comparator;

public class Note {
    private final int id;
    private final String title;
    private final String text;
    private final int importance;
    private final int dayToDeadLine;
    private final String dateOfCreate;
    private int currentValueDayToDeadline;


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

    public int getCurrentValueDayToDeadline() {
        return currentValueDayToDeadline;
    }

    public void setCurrentValueDayToDeadline(int currentValueDayToDeadline) {
        this.currentValueDayToDeadline = currentValueDayToDeadline;
    }

    // Компаратор для сортировки по текущим оставшимся дням
    public static final Comparator<Note> COMPARE_BY_CURRENT_DAY_TO_DEADLINE = (o1, o2) -> {
        int result = 0;
        if (o1.getCurrentValueDayToDeadline() < o2.getCurrentValueDayToDeadline()) {
            result = -1 ;
        } else if (o1.getCurrentValueDayToDeadline() > o2.getCurrentValueDayToDeadline()) {
            result = 1;
        }
        return result;
    };
}
