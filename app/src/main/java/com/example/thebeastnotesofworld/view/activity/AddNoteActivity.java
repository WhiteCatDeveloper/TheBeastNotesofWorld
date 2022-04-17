package com.example.thebeastnotesofworld.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.ToDoNote;
import com.example.thebeastnotesofworld.core.WorkingInDB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextTitleNote;
    private EditText editTextNote;
    private Spinner spinnerImportance;
    private EditText editTextDeadline;
    private Button buttonSave;
    private Button buttonToBack;
    private boolean editNote = false;
    private int idNote;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        init();
        Intent intent = getIntent();
        if (intent.hasExtra("idNote")) {
            editNote = true;
            idNote = intent.getIntExtra("idNote", 0);
            setTextField(new WorkingInDB().getOneToDoNoteByID(this, idNote));
        }
        listeners();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setTextField(ToDoNote toDoNote) {
        editTextTitleNote.setText(toDoNote.getTitle());
        editTextNote.setText(toDoNote.getText());
        spinnerImportance.setSelection(toDoNote.getImportance());
        editTextDeadline.setText(String.valueOf(toDoNote.getDayToDeadLine()));
    }

    private void saveNewNote() {
            String title = editTextTitleNote.getText().toString();
            String text = editTextNote.getText().toString();
            int importance = spinnerImportance.getSelectedItemPosition();
            int dayToDeadline = Integer.parseInt(editTextDeadline.getText().toString());
            String dateOfCreate = getCurrentDate();
            new WorkingInDB().saveNewToDoNote(this, title, text, importance, dayToDeadline, dateOfCreate);
    }

    private boolean checkField() {
        if (editTextTitleNote.length() == 0) {
            dialog("Не заполнен заголовок");
            return false;
        }
        if (editTextNote.length() == 0) {
            dialog("Пустая заметка");
            return false;
        }
        if (editTextDeadline.length() == 0) {
            dialog("Не указано время выполнения");
            return false;
        } else if ((Integer.parseInt(editTextDeadline.getText().toString())) < 0) {
            dialog("Количество дней на выполнение не может быть меньше 0");
            return false;
        }
        return true;
    }

    private void dialog (String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Предупреждение!");
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
        });
        builder.show();
    }

    // Важно!!! Использование паттерна такого формата критически важно для
    // корректной работы класса MyCalendar
    private String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    private void listeners() {
        buttonSave.setOnClickListener(view -> {
            if(checkField() && !editNote) {
                saveNewNote();
                toMain();
            } else if (checkField() && editNote) {
                saveNewNote();
                new WorkingInDB().remoteFromToDoNotes(this, idNote);
                toMain();
            }
        });

        buttonToBack.setOnClickListener(view -> toMain());
    }

    private void toMain () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void init() {
        editTextTitleNote = findViewById(R.id.editTextTitleNote);
        editTextNote = findViewById(R.id.editTextNote);
        spinnerImportance = findViewById(R.id.spinnerAddImportanceNote);
        editTextDeadline = findViewById(R.id.editTextDeadline);
        buttonSave = findViewById(R.id.buttonSaveNewNote);
        buttonToBack = findViewById(R.id.buttonToBack);
    }
}