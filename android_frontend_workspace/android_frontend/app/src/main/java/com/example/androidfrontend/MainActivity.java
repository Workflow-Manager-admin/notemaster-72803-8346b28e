package com.example.androidfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

// PUBLIC_INTERFACE
public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteClickListener {
    private static final int REQUEST_NEW_NOTE = 1;
    private static final int REQUEST_EDIT_NOTE = 2;

    private NotesAdapter adapter;
    private NotesRepository notesRepository;
    private List<Note> allNotes;
    private TextInputEditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NotesApp_Light);
        setContentView(R.layout.activity_main);

        notesRepository = new NotesRepository(this);

        RecyclerView recyclerView = findViewById(R.id.notes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        allNotes = notesRepository.getAllNotes();
        adapter = new NotesAdapter(allNotes, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_note);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
            startActivityForResult(intent, REQUEST_NEW_NOTE);
        });

        searchInput = findViewById(R.id.search_field);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterNotes(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNotes();
    }

    private void refreshNotes() {
        allNotes.clear();
        allNotes.addAll(notesRepository.getAllNotes());
        adapter.notifyDataSetChanged();
        filterNotes(searchInput.getText() != null ? searchInput.getText().toString() : "");
    }

    private void filterNotes(String query) {
        List<Note> filtered = new ArrayList<>();
        for (Note note : allNotes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())
                || note.getContent().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(note);
            }
        }
        adapter.setNotes(filtered);
    }

    // PUBLIC_INTERFACE
    @Override
    public void onNoteClicked(Note note) {
        Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.getId());
        startActivityForResult(intent, REQUEST_EDIT_NOTE);
    }

    // PUBLIC_INTERFACE
    @Override
    public void onNoteLongPressed(Note note) {
        notesRepository.deleteNote(note.getId());
        refreshNotes();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshNotes();
    }
}
