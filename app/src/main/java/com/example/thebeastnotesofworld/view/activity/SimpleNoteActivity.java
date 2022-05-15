package com.example.thebeastnotesofworld.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.notes.SimpleNote;
import com.example.thebeastnotesofworld.db.WorkingInDB;
import com.example.thebeastnotesofworld.view.adapters.RVAdapterForSimpleNotes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SimpleNoteActivity extends AppCompatActivity {

    private RVAdapterForSimpleNotes adapter;
    private final List<SimpleNote> simpleNoteList = new ArrayList<>();

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
        setContentView(R.layout.activity_simple_note);
        getSimpleNote();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSimpleNote);
        FloatingActionButton buttonAddSimpleNote = findViewById(R.id.floatingActionButtonAddSimpleNote);
        adapter = new RVAdapterForSimpleNotes(this, simpleNoteList);
        recyclerView.setAdapter(adapter);
        adapter.setOnSimpleNoteClickListener
                (position -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Предупреждение!")
                            .setMessage("Удалить заметку?")
                            .setPositiveButton("ОТМЕНА", ((dialogInterface, i) -> {}))
                            .setNegativeButton("УДАЛИТЬ",
                                    (dialogInterface, i) -> remoteSimpleNote(position))
                            .show();
                });
        buttonAddSimpleNote.setOnClickListener
                (v -> startActivity(new Intent(this, AddSimpleNoteActivity.class)));
    }

    //Заполнение списка из БД
    private void getSimpleNote() {
        simpleNoteList.clear();
        simpleNoteList.addAll(new WorkingInDB().getAllSimpleNotes(this));
    }

    //Удаление заметки из БД и отображаемого списка
    private void remoteSimpleNote(int position) {
        int id = simpleNoteList.get(position).getId();
        new WorkingInDB().remoteSimpleNote(this, id);
        simpleNoteList.remove(position);
        adapter.notifyItemRemoved(position);
    }
}