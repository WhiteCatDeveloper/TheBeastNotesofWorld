package com.example.thebeastnotesofworld.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.db.WorkingInDB;
import com.example.thebeastnotesofworld.core.notes.ToDoNote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextTitleNote;
    private EditText editTextNote;
    private EditText editTextDeadline;
    private Spinner spinnerImportance;
    private Button buttonSave;
    private Button buttonToBack;
    private Button buttonGoToAddSimple;

    private boolean editNote = false;
    private int idNote;

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
            String dateOfCreate = new SimpleDateFormat
                    ("dd-MM-yyyy", Locale.getDefault()).format(new Date());
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
        buttonGoToAddSimple.setOnClickListener(v -> startActivity(
                new Intent(this, AddSimpleNoteActivity.class)));
    }

    private void toMain () {
        Intent intent = new Intent(this, ToDoNotesActivity.class);
        startActivity(intent);
    }


    private void init() {
        editTextTitleNote = findViewById(R.id.editTextTitleNote);
        editTextNote = findViewById(R.id.editTextNote);
        spinnerImportance = findViewById(R.id.spinnerAddImportanceNote);
        editTextDeadline = findViewById(R.id.editTextDeadline);
        buttonSave = findViewById(R.id.buttonSaveNewNote);
        buttonToBack = findViewById(R.id.buttonToBack);
        buttonGoToAddSimple = findViewById(R.id.buttonToAddSimpleNote);
    }
}