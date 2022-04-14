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

    /**
     * Далее методы для работы с ToDoNote
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ToDoNote> getToDoNotes(Context context, String sortBy){
        ArrayList<ToDoNote> toDoNotes = new ArrayList<>();
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(NotesContract.ToDoNotesEntry.TABLE_NAME,
                null, null, null, null, null, sortBy);
        while (cursor.moveToNext()) {
            toDoNotes.add(getToDoNoteFromDB(cursor));
        }
        cursor.close();
        // КОСТЫЛЬ! Сортируем по текущим оставшимся дням
        if (sortBy!=null && sortBy.equals(NotesContract.ToDoNotesEntry.COLUMN_DEADLINE + " DESC")) {
            Collections.sort(toDoNotes, ToDoNote.COMPARE_BY_CURRENT_DAY_TO_DEADLINE);
        }
        return toDoNotes;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public ToDoNote getOneToDoNoteByID(Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        String selection = NotesContract.ToDoNotesEntry._ID + " == ?";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        Cursor cursor = database.query(NotesContract.ToDoNotesEntry.TABLE_NAME, null, selection,
                selectionArgs, null, null, null);
        List<ToDoNote> toDoNotes = new ArrayList<>();
        while (cursor.moveToNext()) {
            toDoNotes.add(getToDoNoteFromDB(cursor));
        }
        cursor.close();
        return toDoNotes.get(0);
    }

    public void saveNewToDoNote(Context context, String title, String text, int importance, int dayToDeadline, String dateOfCreate) {
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

    public void remoteFromToDoNotes(Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        String where = NotesContract.ToDoNotesEntry._ID + " = ?";
        String[] whereArgs = new String[] {Integer.toString(id)};
        database.delete(NotesContract.ToDoNotesEntry.TABLE_NAME, where, whereArgs);
    }

    // Служебный метод для получения одной записи из БД
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ToDoNote getToDoNoteFromDB(Cursor cursor) {
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

    /**
     * Далее методы для работы с CompletedNote
     */


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void copyNoteToCompleted(Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getWritableDatabase();
        ToDoNote note = getOneToDoNoteByID(context, id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.CompletedToDoNotesEntry.COLUMN_TITLE, note.getTitle());
        contentValues.put(NotesContract.CompletedToDoNotesEntry.COLUMN_TEXT, note.getText());
        contentValues.put(NotesContract.CompletedToDoNotesEntry.COLUMN_DATE_OF_CREATE,
                note.getDateOfCreate());
        contentValues.put(NotesContract.CompletedToDoNotesEntry.COLUMN_IMPORTANCE,
                note.getImportance());
        String dateOfCompleted = new SimpleDateFormat
                ("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        contentValues.put(NotesContract.CompletedToDoNotesEntry.COLUMN_DATE_OF_COMPLETED,
                dateOfCompleted);
        database.insert(NotesContract.CompletedToDoNotesEntry.TABLE_NAME,
                null, contentValues);
    }

    public ArrayList<CompletedToDoNote> getCompletedNoteFromDB (Context context) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getWritableDatabase();
        ArrayList<CompletedToDoNote> list = new ArrayList<>();
        Cursor cursor = database.query(NotesContract.CompletedToDoNotesEntry.TABLE_NAME,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow
                    (NotesContract.CompletedToDoNotesEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow
                    (NotesContract.CompletedToDoNotesEntry.COLUMN_TITLE));
            String text = cursor.getString(cursor.getColumnIndexOrThrow
                    (NotesContract.CompletedToDoNotesEntry.COLUMN_TEXT));
            String dateOfCreate = cursor.getString(cursor.getColumnIndexOrThrow
                    (NotesContract.CompletedToDoNotesEntry.COLUMN_DATE_OF_CREATE));
            int importance = cursor.getInt(cursor.getColumnIndexOrThrow
                    (NotesContract.CompletedToDoNotesEntry.COLUMN_IMPORTANCE));
            String dateOfCompleted = cursor.getString(cursor.getColumnIndexOrThrow
                    (NotesContract.CompletedToDoNotesEntry.COLUMN_DATE_OF_COMPLETED));
            CompletedToDoNote note = new CompletedToDoNote
                    (id, title, text, dateOfCreate, importance, dateOfCompleted);
            list.add(note);
        }
        cursor.close();
        return list;
    }

    public void remoteCompletedNote(Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        String where = NotesContract.ToDoNotesEntry._ID + " = ?";
        String[] whereArgs = new String[] {Integer.toString(id)};
        database.delete(NotesContract.CompletedToDoNotesEntry.TABLE_NAME, where, whereArgs);
    }

    public void remoteAllCompletedNotes (Context context) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        database.execSQL("delete from " + NotesContract.CompletedToDoNotesEntry.TABLE_NAME);
    }

    /**
     * Далее методы для работы с SimpleNote
     */

    public void saveNewSimpleNote(Context context, String title, String text, String dateOfCreate) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.ToDoNotesEntry.COLUMN_TITLE, title);
        contentValues.put(NotesContract.ToDoNotesEntry.COLUMN_TEXT, text);
        contentValues.put(NotesContract.ToDoNotesEntry.COLUMN_DATE_OF_CREATE, dateOfCreate);
        database.insert(NotesContract.SimpleNoteEntry.TABLE_NAME, null, contentValues);
    }

    public ArrayList<SimpleNote> getAllSimpleNotes(Context context) {
        ArrayList<SimpleNote> list = new ArrayList<>();
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(NotesContract.SimpleNoteEntry.TABLE_NAME,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.SimpleNoteEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow
                    (NotesContract.SimpleNoteEntry.COLUMN_TITLE));
            String text = cursor.getString(cursor.getColumnIndexOrThrow
                    (NotesContract.SimpleNoteEntry.COLUMN_TEXT));
            String dateOfCreate = cursor.getString(cursor.getColumnIndexOrThrow
                    (NotesContract.SimpleNoteEntry.COLUMN_DATE_OF_CREATE));
            SimpleNote note = new SimpleNote(id, title, text, dateOfCreate);
            list.add(note);
        }
        cursor.close();
        return list;
    }

    public void remoteSimpleNote(Context context, int id) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getReadableDatabase();
        String where = NotesContract.ToDoNotesEntry._ID + " = ?";
        String[] whereArgs = new String[] {Integer.toString(id)};
        database.delete(NotesContract.SimpleNoteEntry.TABLE_NAME, where, whereArgs);
    }

}
