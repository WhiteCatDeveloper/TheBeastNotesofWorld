package com.example.thebeastnotesofworld.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.thebeastnotesofworld.db.NotesContract;
import com.example.thebeastnotesofworld.db.NotesDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * В этом классе происходит вся работа с БД.
 */

public class WorkingInDB {

    private NotesDBHelper dbHelper;
    private SQLiteDatabase database;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ToDoNote> getNotes(Context context, String sortBy){
        ArrayList<ToDoNote> toDoNotes = new ArrayList<>();
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME,
                null, null, null, null, null, sortBy);
        while (cursor.moveToNext()) {
            toDoNotes.add(getNoteFromDB(cursor));
        }
        cursor.close();
        // КОСТЫЛЬ! Сортируем по текущим оставшимся дням
        if (sortBy!=null && sortBy.equals(NotesContract.NotesEntry.COLUMN_DEADLINE + " DESC")) {
            Collections.sort(toDoNotes, ToDoNote.COMPARE_BY_CURRENT_DAY_TO_DEADLINE);
        }
        return toDoNotes;
    }

    public void remote (Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        String where = NotesContract.NotesEntry._ID + " = ?";
        String[] whereArgs = new String[] {Integer.toString(id)};
        database.delete(NotesContract.NotesEntry.TABLE_NAME, where, whereArgs);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public ToDoNote getOneNoteByID(Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        String selection = NotesContract.NotesEntry._ID + " == ?";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, null, selection,
                selectionArgs, null, null, null);
        List<ToDoNote> toDoNotes = new ArrayList<>();
        while (cursor.moveToNext()) {
            toDoNotes.add(getNoteFromDB(cursor));
        }
        cursor.close();
        return toDoNotes.get(0);
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

    // Служебный метод для получения одной записи из БД
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ToDoNote getNoteFromDB(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry._ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_TITLE));
        String text = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_TEXT));
        int importance = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_IMPORTANCE));
        int dayToDeadLine = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_DEADLINE));
        String dateOfCreate = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_DATE_OF_CREATE));
        return new ToDoNote(id, title, text, importance, dayToDeadLine, dateOfCreate);
    }
}
