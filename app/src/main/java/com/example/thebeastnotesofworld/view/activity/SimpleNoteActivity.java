package com.example.thebeastnotesofworld.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.SimpleNote;
import com.example.thebeastnotesofworld.core.WorkingInDB;
import com.example.thebeastnotesofworld.view.adapters.RVAdapterForSimpleNotes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SimpleNoteActivity extends AppCompatActivity {

    private RVAdapterForSimpleNotes adapter;
    private final List<SimpleNote> simpleNoteList = new ArrayList<>();


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
                (v -> startActivity(new Intent(this, AddNoteActivity.class)));
    }


    private void getSimpleNote() {
        simpleNoteList.clear();
        simpleNoteList.addAll(new WorkingInDB().getAllSimpleNotes(this));
    }

    private void remoteSimpleNote(int position) {
        int id = simpleNoteList.get(position).getId();
        new WorkingInDB().remoteSimpleNote(this, id);
        adapter.notifyItemRemoved(position);
    }
}