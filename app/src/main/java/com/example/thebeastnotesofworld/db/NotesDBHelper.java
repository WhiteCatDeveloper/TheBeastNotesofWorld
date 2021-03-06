package com.example.thebeastnotesofworld.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NotesDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 4;

    public NotesDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(NotesContract.ToDoNotesEntry.CREATE_TABLE);
        sqLiteDatabase.execSQL(NotesContract.SimpleNoteEntry.CREATE_TABLE);
        sqLiteDatabase.execSQL(NotesContract.CompletedToDoNotesEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(NotesContract.ToDoNotesEntry.DROP_TABLE);
        sqLiteDatabase.execSQL(NotesContract.SimpleNoteEntry.DROP_TABLE);
        sqLiteDatabase.execSQL(NotesContract.CompletedToDoNotesEntry.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
