package com.example.thebeastnotesofworld.db;

import android.provider.BaseColumns;

public class NotesContract {
    public static final String TYPE_TEXT = "TEXT";
    public static final String TYPE_INTEGER = "INTEGER";

    public static final class SimpleNoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "simple_notes";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_DATE_OF_CREATE = "date_of_create";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE +
                " " + TYPE_TEXT + ", " + COLUMN_TEXT + " " + TYPE_TEXT + ", " +
                COLUMN_DATE_OF_CREATE + " " + TYPE_TEXT + ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    public static final class ToDoNotesEntry implements BaseColumns {
        public static final String TABLE_NAME = "to_do_notes";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_IMPORTANCE = "importance";
        public static final String COLUMN_DEADLINE = "deadline";
        public static final String COLUMN_DATE_OF_CREATE = "date_of_create";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE +
                " " + TYPE_TEXT + ", " + COLUMN_TEXT + " " + TYPE_TEXT + ", " + COLUMN_IMPORTANCE +
                " " + TYPE_INTEGER + ", " + COLUMN_DEADLINE + " " + TYPE_INTEGER + ", " +
                COLUMN_DATE_OF_CREATE + " " + TYPE_TEXT + ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class CompletedToDoNotesEntry implements BaseColumns {
        public static final String TABLE_NAME = "completed_to_do_notes";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_DATE_OF_CREATE = "date_of_create";
        public static final String COLUMN_DATE_OF_COMPLETED = "date_of_completed";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE +
                " " + TYPE_TEXT + ", " + COLUMN_TEXT + " " + TYPE_TEXT + ", " +
                COLUMN_DATE_OF_CREATE + " " + TYPE_TEXT + " ," + COLUMN_DATE_OF_COMPLETED +
                " " + TYPE_TEXT + ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
