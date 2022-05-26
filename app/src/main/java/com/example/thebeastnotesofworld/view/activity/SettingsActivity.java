package com.example.thebeastnotesofworld.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import com.example.thebeastnotesofworld.R;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerSetTimeNotification;
    private Button buttonSaveData;
    private Button buttonLoadData;
    private Button buttonToBack;

    private int setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        spinnerSetTimeNotification = findViewById(R.id.spinnerSetTimeNotification);
        buttonSaveData = findViewById(R.id.buttonSaveData);
        buttonLoadData = findViewById(R.id.buttonLoadData);
        buttonToBack = findViewById(R.id.buttonSettingsToBack);

        buttonToBack.setOnClickListener(
                v -> startActivity(new Intent(this, ToDoNotesActivity.class)));
        setTime = spinnerSetTimeNotification.getSelectedItemPosition() + 1;
    }
}