package com.example.thebeastnotesofworld.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.WorkingInDB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddSimpleNoteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextText;
    private Button buttonToBack;
    private Button buttonSaveNote;
    private Button buttonAddToDoNote;

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
        buttonToBack.setOnClickListener(v -> startActivity
                (new Intent(getApplicationContext(), MainActivity.class)));
        buttonAddToDoNote.setOnClickListener(v -> startActivity(
                new Intent(getApplicationContext(), AddNoteActivity.class)
        ));
        buttonSaveNote.setOnClickListener(v -> {
            if (isChecked()) {
                String title = editTextTitle.getText().toString();
                String text = editTextText.getText().toString();
                String dateOfCreate = new SimpleDateFormat
                        ("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                new WorkingInDB().saveNewSimpleNote(getApplicationContext(), title, text, dateOfCreate);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Предупреждение")
                        .setMessage("Не все поля заполнены")
                        .setPositiveButton("OK", (dialog, which) -> {})
                        .show();
            }
        });
    }

    private boolean isChecked () {
        return editTextTitle.length() != 0 && editTextText.length() != 0;
    }
}