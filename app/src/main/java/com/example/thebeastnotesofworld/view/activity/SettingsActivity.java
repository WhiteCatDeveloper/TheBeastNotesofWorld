package com.example.thebeastnotesofworld.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.notification.MyAlarmManager;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerSetTimeNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        spinnerSetTimeNotification = findViewById(R.id.spinnerSetTimeNotification);
        Button buttonSaveData = findViewById(R.id.buttonSaveData);
        Button buttonLoadData = findViewById(R.id.buttonLoadData);
        Button buttonSaveSettings = findViewById(R.id.buttonSaveSettings);
        Button buttonToBack = findViewById(R.id.buttonSettingsToBack);
        buttonToBack.setOnClickListener(
                v -> startActivity(new Intent(this, ToDoNotesActivity.class)));
        buttonSaveData.setOnClickListener(v -> saveData());
        buttonLoadData.setOnClickListener(v -> loadData());
        buttonSaveSettings.setOnClickListener(v -> saveSettings());
    }

    // Используем контент провайдер чтобы расшарить БД
    private void saveData () {

    }
    // Загружаем старые данные в текущую БД
    private void loadData() {

    }

    private void saveSettings() {
        SharedPreferences sharedPreferences =
                this.getSharedPreferences("TIME_ALARM", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int setTime = spinnerSetTimeNotification.getSelectedItemPosition() + 1;
        editor.putInt("GET_TIME_ALARM", setTime);
        editor.apply();
        new MyAlarmManager(this).reinstallAlarm();
        Toast.makeText(this, "Настройки успешно сохранены", Toast.LENGTH_SHORT).show();
    }

}