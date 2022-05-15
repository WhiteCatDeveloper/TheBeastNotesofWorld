package com.example.thebeastnotesofworld.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.notification.MyAlarmManager;
import com.example.thebeastnotesofworld.db.WorkingInDB;
import com.example.thebeastnotesofworld.core.notes.ToDoNote;
import com.example.thebeastnotesofworld.db.NotesContract;
import com.example.thebeastnotesofworld.view.adapters.RVAdapterForToDoNotes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Стартовая активность
 * Отвечает за отображение списка текущих задач
 */


public class ToDoNotesActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButtonAddNote;
    private Spinner spinnerSortBy;
    private RVAdapterForToDoNotes adapter;
    private SharedPreferences sharedPreferences;
    public static final String APP_PREFERENCES = "mySettings";
    private static final String SETTINGS_SORT_BY = "settingsSortBy";
    private String sortBy;

    private List<ToDoNote> toDoNotes = new ArrayList<>();


    // Создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Слушатель на меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goToSimpleNote) {
            startActivity(new Intent(this, SimpleNoteActivity.class));
        }else if (id == R.id.goToToDoNote){
            startActivity(new Intent(this, ToDoNotesActivity.class));
        }else if (id == R.id.goToCompletedNote) {
            startActivity(new Intent(this, CompletedNotesActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_notes);
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        getSort();
        setNotes();
        floatingActionButtonAddNote = findViewById(R.id.floatingActionButtonAddNote);
        spinnerSortBy = findViewById(R.id.spinnerShowSort);
        RecyclerView recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        adapter = new RVAdapterForToDoNotes(this, toDoNotes);
        recyclerViewNotes.setAdapter(adapter);
        listeners();
        new MyAlarmManager().setAlarm(this);
    }


    // Залолняем список заметок при первом запуске
    private void setNotes() {
        toDoNotes = new WorkingInDB().getToDoNotes(this, sortBy);
    }

    // Добавляем новые значения в список из БД. Т.к нельзя просто присвоить списку значения другого
    // списка, то очищаем его и добавляем новые значения через метод коллекций addAll
    // Так же у адаптера вызываем перерисовку
    private void updateListNotes() {
        List<ToDoNote> newList = new WorkingInDB().getToDoNotes(this, sortBy);
        adapter.updateList(newList);
        toDoNotes.clear();
        toDoNotes.addAll(newList);
    }


    //Все слушатели
    private void listeners() {
        floatingActionButtonAddNote.setOnClickListener(view -> {
            Intent intent = new Intent(ToDoNotesActivity.this, AddNoteActivity.class);
            startActivity(intent);
         });

        adapter.setOnNoteClickListener(new RVAdapterForToDoNotes.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                Intent intent = new Intent(getApplicationContext(), DetailNotesActivity.class);
                intent.putExtra("idNote", toDoNotes.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ToDoNotesActivity.this);
                builder.setTitle("Предупреждение!")
                        .setMessage("Переместить запись в выполненное или удалить?")
                        .setPositiveButton("ОТМЕНА", ((dialogInterface, i) -> {}))
                        .setNegativeButton("УДАЛИТЬ", (dialogInterface, i) -> remote(position))
                        .setNeutralButton("ПЕРЕМЕСТИТЬ", (dialog, which) -> {
                            new WorkingInDB().copyNoteToCompleted
                                    (getApplicationContext(), toDoNotes.get(position).getId());
                            remote(position);
                        })
                        .show();
            }
        });
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: sortBy = NotesContract.ToDoNotesEntry.COLUMN_IMPORTANCE + " DESC";
                    break;
                    case 1: sortBy = NotesContract.ToDoNotesEntry.COLUMN_DEADLINE + " DESC";
                    break;
                    default: sortBy = null;
                }
                saveSort(sortBy);
                updateListNotes();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sortBy = null;
            }
        });


    }

    // Сохраняет состояние сортировки списка
    private void saveSort(String sortBy) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SETTINGS_SORT_BY, sortBy);
        editor.apply();
    }

    // Возвращает состоряние сортировки списка
    private void getSort() {
        if (sharedPreferences.contains(SETTINGS_SORT_BY)) {
         sortBy = sharedPreferences.getString(SETTINGS_SORT_BY, null);
        } else sortBy = null;
    }

    //Удаление элемента из БД и отображаемого списка
    private void remote (int position) {
        int id = toDoNotes.get(position).getId();
        new WorkingInDB().remoteFromToDoNotes(this, id);
        toDoNotes.remove(position);
        adapter.notifyItemRemoved(position);
    }
}