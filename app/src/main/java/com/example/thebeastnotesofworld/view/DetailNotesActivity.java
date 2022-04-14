package com.example.thebeastnotesofworld.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.ToDoNote;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notes);
        Intent intent = getIntent();
        if (intent.hasExtra("idNote")) id = intent.getIntExtra("idNote", 0);
        init();
        listeners();
        setTextView(new WorkingInDB().getOneToDoNoteByID(this, id));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setTextView(ToDoNote toDoNote) {
        textViewDetailTitle.setText(toDoNote.getTitle());
        textViewDetailNote.setText(toDoNote.getText());
        String importance;
        String[] arrImportance = getResources().getStringArray(R.array.importance);
        switch (toDoNote.getImportance()) {
            case 0: importance = arrImportance[0];
            break;
            case 1: importance = arrImportance[1];
            break;
            case 2: importance = arrImportance[2];
            break;
            default: importance = arrImportance[3];
        }
        textViewDetailImportance.setText(importance);
        textViewDetailDateOfCreate.setText(toDoNote.getDateOfCreate());
        textViewDetailDeadline.setText(String.valueOf(toDoNote.getDayToDeadLine()));
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