package com.example.thebeastnotesofworld.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.Note;
import com.example.thebeastnotesofworld.core.WorkingInDB;

public class DetailNotesActivity extends AppCompatActivity {

    private TextView textViewDetailTitle;
    private TextView textViewDetailNote;
    private TextView textViewDetailImportance;
    private TextView textViewDetailDeadline;
    private TextView textViewDetailDateOfCreate;
    private Button buttonDetailToBack;
    private Button buttonDetailToEdit;
    private Button buttonDetailToAddNote;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notes);
        Intent intent = getIntent();
        if (intent.hasExtra("idNote")) id = intent.getIntExtra("idNote", 0);
        init();
        listeners();
        setTextView(new WorkingInDB().getOneNoteByID(this, id));
    }

    private void setTextView(Note note) {
        textViewDetailTitle.setText(note.getTitle());
        textViewDetailNote.setText(note.getText());
        String importance;
        String[] arrImportance = getResources().getStringArray(R.array.importance);
        switch (note.getImportance()) {
            case 0: importance = arrImportance[0];
            break;
            case 1: importance = arrImportance[1];
            break;
            case 2: importance = arrImportance[2];
            break;
            default: importance = arrImportance[3];
        }
        textViewDetailImportance.setText(importance);
        textViewDetailDateOfCreate.setText(note.getDateOfCreate());
        textViewDetailDeadline.setText(String.valueOf(note.getDayToDeadLine()));
    }

    private void listeners() {
        buttonDetailToBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        buttonDetailToEdit.setOnClickListener(view -> {
            Intent intentEditNote = new Intent(getApplicationContext(), AddNoteActivity.class);
            intentEditNote.putExtra("idNote", id);
            startActivity(intentEditNote);
        });
        buttonDetailToAddNote.setOnClickListener(view -> {
            Intent intentAddNote = new Intent(getApplicationContext(), AddNoteActivity.class);
            startActivity(intentAddNote);
        });
    }

    private void init() {
        textViewDetailTitle = findViewById(R.id.textViewDetailTitle);
        textViewDetailNote = findViewById(R.id.textViewDetailNote);
        textViewDetailImportance = findViewById(R.id.textViewDetailImportance);
        textViewDetailDeadline = findViewById(R.id.textViewDetailDeadline);
        textViewDetailDateOfCreate = findViewById(R.id.textViewDetailDateOfCreate);
        buttonDetailToBack = findViewById(R.id.buttonDetailToBack);
        buttonDetailToEdit = findViewById(R.id.buttonDetailToEdit);
        buttonDetailToAddNote = findViewById(R.id.buttonDetailToAddNote);
    }
}