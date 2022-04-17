package com.example.thebeastnotesofworld.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.SimpleNote;
import com.example.thebeastnotesofworld.view.adapters.RVAdapterForSimpleNotes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SimpleNoteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RVAdapterForSimpleNotes adapter;
    private FloatingActionButton buttonAddSimpleNote;
    private List<SimpleNote> simpleNoteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_note);
        getSimpleNote();
        recyclerView = findViewById(R.id.recyclerViewSimpleNote);
        buttonAddSimpleNote = findViewById(R.id.floatingActionButtonAddSimpleNote);
        adapter = new RVAdapterForSimpleNotes(this, simpleNoteList);
        recyclerView.setAdapter(adapter);
    }


    private void getSimpleNote () {
        simpleNoteList.add(new SimpleNote(1, "Title", "TEXT", "12343"));
        simpleNoteList.add(new SimpleNote(2, "111111", "dgfaT", "12343"));
        simpleNoteList.add(new SimpleNote(3, "!!!!!!!!!!!", "b;agvhdlkajbvnlvgvhklc ", "12343"));
        simpleNoteList.add(new SimpleNote(4, "#@*&^%$", "bbfaKLHBCNKVLFMA;,BC", "12343"));
    }
}