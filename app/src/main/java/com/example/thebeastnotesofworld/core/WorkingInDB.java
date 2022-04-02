package com.example.thebeastnotesofworld.core;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thebeastnotesofworld.db.NotesContract;
import com.example.thebeastnotesofworld.db.NotesDBHelper;

import java.util.ArrayList;
import java.util.List;

public class WorkingInDB {

    private NotesDBHelper dbHelper;
    private SQLiteDatabase database;

    public ArrayList<Note> getNotes(Context context, String sortBy){
        ArrayList<Note> notes = new ArrayList<>();
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME,
                null, null, null, null, null, sortBy);
        while (cursor.moveToNext()) {
            notes.add(getNoteFromDB(cursor));
        }
        cursor.close();
        return notes;
    }

    public void remote (Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        String where = NotesContract.NotesEntry._ID + " = ?";
        String[] whereArgs = new String[] {Integer.toString(id)};
        database.delete(NotesContract.NotesEntry.TABLE_NAME, where, whereArgs);
    }


    public Note getOneNoteByID(Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        String selection = NotesContract.NotesEntry._ID + " == ?";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, null, selection,
                selectionArgs, null, null, null);
        List<Note> notes = new ArrayList<>();
        while (cursor.moveToNext()) {
            notes.add(getNoteFromDB(cursor));
        }
        cursor.close();
        return notes.get(0);
    }

    private Note getNoteFromDB(Cursor cursor) {
        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry._ID));
        @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_TITLE));
        @SuppressLint("Range") String text = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_TEXT));
        @SuppressLint("Range") int importance = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_IMPORTANCE));
        @SuppressLint("Range") int dayToDeadLine = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DEADLINE));
        @SuppressLint("Range") String dateOfCreate = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DATE_OF_CREATE));
        return new Note(id, title, text, importance, dayToDeadLine, dateOfCreate);
    }

    public void saveNewNote(Context context, String title, String text, int importance, int dayToDeadline, String dateOfCreate) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.NotesEntry.COLUMN_TITLE, title);
        contentValues.put(NotesContract.NotesEntry.COLUMN_TEXT, text);
        contentValues.put(NotesContract.NotesEntry.COLUMN_IMPORTANCE, importance);
        contentValues.put(NotesContract.NotesEntry.COLUMN_DEADLINE, dayToDeadline);
        contentValues.put(NotesContract.NotesEntry.COLUMN_DATE_OF_CREATE, dateOfCreate);
        database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
    }
}
