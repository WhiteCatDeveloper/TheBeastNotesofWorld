package com.example.thebeastnotesofworld.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.db.WorkingInDB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddSimpleNoteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextText;
    private Button buttonToBack;
    private Button buttonSaveNote;
    private Button buttonAddToDoNote;

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
        setContentView(R.layout.activity_add_simple_note);
        editTextTitle = findViewById(R.id.editTextAddSimpleTitle);
        editTextText = findViewById(R.id.editTextAddSimpleText);
        buttonToBack = findViewById(R.id.buttonSimpleToBack);
        buttonSaveNote = findViewById(R.id.buttonSaveSimpleNote);
        buttonAddToDoNote = findViewById(R.id.buttonAddToDoNote);
        listeners();
    }

    private void listeners() {
        buttonToBack.setOnClickListener(v -> toSimpleActivity());
        buttonAddToDoNote.setOnClickListener(v -> startActivity(
                new Intent(getApplicationContext(), AddNoteActivity.class)
        ));
        buttonSaveNote.setOnClickListener(v -> {
            if (isChecked()) {
                saveSimpleNote();
                toSimpleActivity();
            } else showAlertDialog();
        });
    }

    private void saveSimpleNote() {
            String title = editTextTitle.getText().toString();
            String text = editTextText.getText().toString();
            String dateOfCreate = new SimpleDateFormat
                    ("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            new WorkingInDB().saveNewSimpleNote(this, title, text, dateOfCreate);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Предупреждение")
                .setMessage("Не все поля заполнены")
                .setPositiveButton("OK", (dialog, which) -> {})
                .show();
    }

    private boolean isChecked () {
        return editTextTitle.length() != 0 && editTextText.length() != 0;
    }

    private void toSimpleActivity() {
        startActivity(new Intent(getApplicationContext(), SimpleNoteActivity.class));
    }
}