package com.example.thebeastnotesofworld.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.thebeastnotesofworld.db.NotesContract;
import com.example.thebeastnotesofworld.db.NotesDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        Cursor cursor = database.query(NotesContract.ToDoNotesEntry.TABLE_NAME,
                null, null, null, null, null, sortBy);
        while (cursor.moveToNext()) {
            toDoNotes.add(getNoteFromDB(cursor));
        }
        cursor.close();
        // КОСТЫЛЬ! Сортируем по текущим оставшимся дням
        if (sortBy!=null && sortBy.equals(NotesContract.ToDoNotesEntry.COLUMN_DEADLINE + " DESC")) {
            Collections.sort(toDoNotes, ToDoNote.COMPARE_BY_CURRENT_DAY_TO_DEADLINE);
        }
        return toDoNotes;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public ToDoNote getOneNoteByID(Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        String selection = NotesContract.ToDoNotesEntry._ID + " == ?";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        Cursor cursor = database.query(NotesContract.ToDoNotesEntry.TABLE_NAME, null, selection,
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
        contentValues.put(NotesContract.ToDoNotesEntry.COLUMN_TITLE, title);
        contentValues.put(NotesContract.ToDoNotesEntry.COLUMN_TEXT, text);
        contentValues.put(NotesContract.ToDoNotesEntry.COLUMN_IMPORTANCE, importance);
        contentValues.put(NotesContract.ToDoNotesEntry.COLUMN_DEADLINE, dayToDeadline);
        contentValues.put(NotesContract.ToDoNotesEntry.COLUMN_DATE_OF_CREATE, dateOfCreate);
        database.insert(NotesContract.ToDoNotesEntry.TABLE_NAME, null, contentValues);
    }

    public void remote (Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        String where = NotesContract.ToDoNotesEntry._ID + " = ?";
        String[] whereArgs = new String[] {Integer.toString(id)};
        database.delete(NotesContract.ToDoNotesEntry.TABLE_NAME, where, whereArgs);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void transferNoteToCompleted (Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        ToDoNote note = getOneNoteByID(context, id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.CompletedToDoNotesEntry.COLUMN_TITLE, note.getTitle());
        contentValues.put(NotesContract.CompletedToDoNotesEntry.COLUMN_TEXT, note.getText());
        contentValues.put(NotesContract.CompletedToDoNotesEntry.COLUMN_DATE_OF_CREATE,
                note.getDateOfCreate());
        String dateOfCompleted = new SimpleDateFormat
                ("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        contentValues.put(NotesContract.CompletedToDoNotesEntry.COLUMN_DATE_OF_COMPLETED,
                dateOfCompleted);
        database.insert(NotesContract.CompletedToDoNotesEntry.TABLE_NAME,
                null, contentValues);

    }

    // Служебный метод для получения одной записи из БД
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ToDoNote getNoteFromDB(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.ToDoNotesEntry._ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow
                (NotesContract.ToDoNotesEntry.COLUMN_TITLE));
        String text = cursor.getString(cursor.getColumnIndexOrThrow
                (NotesContract.ToDoNotesEntry.COLUMN_TEXT));
        int importance = cursor.getInt(cursor.getColumnIndexOrThrow
                (NotesContract.ToDoNotesEntry.COLUMN_IMPORTANCE));
        int dayToDeadLine = cursor.getInt(cursor.getColumnIndexOrThrow
                (NotesContract.ToDoNotesEntry.COLUMN_DEADLINE));
        String dateOfCreate = cursor.getString(cursor.getColumnIndexOrThrow
                (NotesContract.ToDoNotesEntry.COLUMN_DATE_OF_CREATE));
        return new ToDoNote(id, title, text, importance, dayToDeadLine, dateOfCreate);
    }


}
