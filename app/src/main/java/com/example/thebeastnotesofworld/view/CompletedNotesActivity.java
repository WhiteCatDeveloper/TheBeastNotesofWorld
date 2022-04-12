package com.example.thebeastnotesofworld.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.CompletedToDoNote;
import com.example.thebeastnotesofworld.core.WorkingInDB;
import com.example.thebeastnotesofworld.view.adapters.RVAdapterForCompletedNotes;

import java.util.ArrayList;
import java.util.List;

public class CompletedNotesActivity extends AppCompatActivity {

    private Button buttonToBack;
    private RecyclerView recyclerViewCompletedNotes;
    RVAdapterForCompletedNotes adapter;
    List<CompletedToDoNote> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_notes);
        buttonToBack = findViewById(R.id.buttonCompletedToBack);
        recyclerViewCompletedNotes = findViewById(R.id.recyclerViewCompletedNotes);
        buttonToBack.setOnClickListener(
                v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
        list.addAll(new WorkingInDB().getCompletedNoteFromDB(this));
        adapter = new RVAdapterForCompletedNotes(this, list);
        recyclerViewCompletedNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompletedNotes.setAdapter(adapter);
    }
}