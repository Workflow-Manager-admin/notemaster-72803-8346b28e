package com.example.androidfrontend;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// PUBLIC_INTERFACE
public class NoteDetailActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE_ID = "note_id";

    private NotesRepository notesRepository;
    private EditText titleEdit;
    private EditText contentEdit;
    private Note editingNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NotesApp_Light);
        setContentView(R.layout.activity_note_detail);

        notesRepository = new NotesRepository(this);

        titleEdit = findViewById(R.id.edit_note_title);
        contentEdit = findViewById(R.id.edit_note_content);

        String noteId = getIntent().getStringExtra(EXTRA_NOTE_ID);
        if (noteId != null) {
            editingNote = notesRepository.getNoteById(noteId);
            if (editingNote != null) {
                titleEdit.setText(editingNote.getTitle());
                contentEdit.setText(editingNote.getContent());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_detail_menu, menu);
        if (editingNote == null) {
            menu.findItem(R.id.menu_delete).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            saveNote();
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            notesRepository.deleteNote(editingNote.getId());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String title = titleEdit.getText().toString().trim();
        String content = contentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) return;

        if (editingNote != null) {
            editingNote.setTitle(title);
            editingNote.setContent(content);
            editingNote.setTimestamp(System.currentTimeMillis());
            notesRepository.updateNote(editingNote);
        } else {
            Note newNote = new Note(title, content);
            notesRepository.addNote(newNote);
        }
    }
}
