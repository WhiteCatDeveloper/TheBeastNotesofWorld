package com.example.thebeastnotesofworld.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.notes.CompletedToDoNote;
import com.example.thebeastnotesofworld.db.WorkingInDB;
import com.example.thebeastnotesofworld.view.adapters.RVAdapterForCompletedNotes;

import java.util.ArrayList;
import java.util.List;

public class CompletedNotesActivity extends AppCompatActivity {

    private Button buttonToBack;
    private Button buttonRemoteAll;
    private RVAdapterForCompletedNotes adapter;
    private final List<CompletedToDoNote> list = new ArrayList<>();

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
        setContentView(R.layout.activity_completed_notes);
        buttonToBack = findViewById(R.id.buttonCompletedToBack);
        buttonRemoteAll = findViewById(R.id.buttonCompletedRemoteAll);
        RecyclerView recyclerViewCompletedNotes = findViewById(R.id.recyclerViewCompletedNotes);
        list.addAll(new WorkingInDB().getCompletedNoteFromDB(this));
        adapter = new RVAdapterForCompletedNotes(this, list);
        recyclerViewCompletedNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompletedNotes.setAdapter(adapter);
        listeners();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void listeners() {
        buttonToBack.setOnClickListener(
                v -> startActivity(new Intent(getApplicationContext(), ToDoNotesActivity.class)));

        buttonRemoteAll.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ПРЕДУПРЕЖНЕНИЕ")
                    .setMessage("Вы хотите удалить ВСЕ заметки окончательно?")
                    .setPositiveButton("ДА", (dialog, which) -> {
                        new WorkingInDB().remoteAllCompletedNotes(this);
                        list.clear();
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("НЕТ", (dialog, which) -> {})
                    .show();
        });
        adapter.setOnNoteClickListener(position -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("ПРЕДУПРЕЖНЕНИЕ")
                        .setMessage("Вы хотите удалить заметку окончательно?")
                        .setPositiveButton("ДА", (dialog, which) -> remoteOneNote(position))
                        .setNegativeButton("НЕТ", (dialog, which) -> {})
                        .show();
        });
    }

    private void remoteOneNote (int position) {
        int id = list.get(position).getId();
        new WorkingInDB().remoteCompletedNote(this, id);
        list.remove(position);
        adapter.notifyItemRemoved(position);
    }
}