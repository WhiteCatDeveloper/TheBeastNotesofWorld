package com.example.thebeastnotesofworld.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.db.WorkingInDB;
import com.example.thebeastnotesofworld.core.notes.ToDoNote;

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
        setContentView(R.layout.activity_detail_notes);
        Intent intent = getIntent();
        if (intent.hasExtra("idNote")) id = intent.getIntExtra("idNote", 0);
        init();
        listeners();
        setTextView(new WorkingInDB().getOneToDoNoteByID(this, id));
    }

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

    //Слушатели
    private void listeners() {
        buttonDetailToBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ToDoNotesActivity.class);
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

    //Инициализация всех переменных
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