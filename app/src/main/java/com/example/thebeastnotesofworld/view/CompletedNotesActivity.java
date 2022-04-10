package com.example.thebeastnotesofworld.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.thebeastnotesofworld.R;

public class CompletedNotesActivity extends AppCompatActivity {

    private Button buttonToBack;
    private RecyclerView recyclerViewCompletedNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_notes);
        buttonToBack = findViewById(R.id.buttonCompletedToBack);
        recyclerViewCompletedNotes = findViewById(R.id.recyclerViewCompletedNotes);
        buttonToBack.setOnClickListener(
                v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
    }
}