package com.example.thebeastnotesofworld.view;

// ЕЩЕ ЗАДАЧИ
// 1. Сохранение состояния сортировки. V
// 2. Исправление обновления ресайклера (DiffUtil). V
//         2.1 Поломалась сортировка по срочности. Какого??? V
// 3. Увеличить апи до 26.
//              3.1 Избавиться от RequiresApi в MyCalendar;
// 4. Иконка приложения.  V
// 5. Убрать везде @SuppressLint. V
// 6. Разобраться с крашем в эмуляторе с апи30 (в MyCalendar) V

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.thebeastnotesofworld.R;
import com.example.thebeastnotesofworld.core.WorkingInDB;
import com.example.thebeastnotesofworld.core.adapters.RVAdapter;
import com.example.thebeastnotesofworld.core.Note;
import com.example.thebeastnotesofworld.db.NotesContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButtonAddNote;
    RecyclerView recyclerViewNotes;
    private RVAdapter adapter;
    SharedPreferences sharedPreferences;
    public static final String APP_PREFERENCES = "mySettings";
    private static final String SETTINGS_SORT_BY = "settingsSortBy";
    private String sortBy;


    private List<Note> notes = new ArrayList<>();


    // Создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Слушатель на меню
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sortByImportance)
            sortBy = NotesContract.NotesEntry.COLUMN_IMPORTANCE + " DESC";
        else if (id == R.id.sortByDeadline)
            sortBy = NotesContract.NotesEntry.COLUMN_DEADLINE + " ASC";
        else if (id == R.id.sortByDateOfCreate)
            sortBy = NotesContract.NotesEntry.COLUMN_DATE_OF_CREATE + " DESC";
        else sortBy = null;
        saveSort(sortBy);
        updateListNotes();
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        getSort();
        setNotes();
        floatingActionButtonAddNote = findViewById(R.id.floatingActionButtonAddNote);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        adapter = new RVAdapter(this, notes);
        recyclerViewNotes.setAdapter(adapter);
        listeners();
    }

    // Залолняем список заметок при первом запуске
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNotes() {
        notes = new WorkingInDB().getNotes(this, sortBy);
    }

    // Добавляем новые значения в список из БД. Т.к нельзя просто присвоить списку значения другого
    // списка, то очищаем его и добавляем новые значения через метод коллекций addAll
    // Так же у адаптера вызываем перерисовку
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateListNotes() {
        List<Note> newList = new WorkingInDB().getNotes(this, sortBy);
        adapter.updateList(newList);
        notes.clear();
        notes.addAll(newList);
    }


    private void listeners() {
        floatingActionButtonAddNote.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
         });

        adapter.setOnNoteClickListener(new RVAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                Intent intent = new Intent(getApplicationContext(), DetailNotesActivity.class);
                intent.putExtra("idNote", notes.get(position).getId());
                startActivity(intent);
            }
            @Override
            public void onLongClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Предупреждение!")
                        .setMessage("Вы действительно хотите удалить заметку?")
                        .setPositiveButton("ДА", (dialogInterface, i) -> remote(position))
                        .setNegativeButton("НЕТ", ((dialogInterface, i) -> {}))
                        .show();
            }
        });


    }

    // Сохраняет состояние сортировки списка
    private void saveSort(String sortBy) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SETTINGS_SORT_BY, sortBy);
        editor.apply();
    }

    // Возвращает состоряние сортировки списка
    private void getSort() {
        if (sharedPreferences.contains(SETTINGS_SORT_BY)) {
         sortBy = sharedPreferences.getString(SETTINGS_SORT_BY, null);
        } else sortBy = null;
    }

    private void remote (int position) {
        int id = notes.get(position).getId();
        new WorkingInDB().remote(this, id);
        notes.remove(position);
        adapter.notifyItemRemoved(position);
    }
}