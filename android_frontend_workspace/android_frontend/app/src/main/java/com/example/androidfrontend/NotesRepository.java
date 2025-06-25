package com.example.androidfrontend;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// PUBLIC_INTERFACE
public class NotesRepository {
    private static final String PREFS_NAME = "notes_storage";
    private static final String NOTES_KEY = "notes_list";

    private final SharedPreferences sharedPreferences;

    public NotesRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // PUBLIC_INTERFACE
    public List<Note> getAllNotes() {
        String notesString = sharedPreferences.getString(NOTES_KEY, "[]");
        List<Note> notes = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(notesString);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                notes.add(new Note(obj.getString("id"), obj.getString("title"),
                        obj.getString("content"), obj.getLong("timestamp")));
            }
        } catch (JSONException e) { e.printStackTrace(); }
        // Most recent at the top
        notes.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        return notes;
    }

    // PUBLIC_INTERFACE
    public Note getNoteById(String id) {
        for (Note note : getAllNotes()) {
            if (note.getId().equals(id)) return note;
        }
        return null;
    }

    // PUBLIC_INTERFACE
    public void addNote(Note note) {
        List<Note> notes = getAllNotes();
        notes.add(note);
        saveNotes(notes);
    }

    // PUBLIC_INTERFACE
    public void updateNote(Note updatedNote) {
        List<Note> notes = getAllNotes();
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            if (note.getId().equals(updatedNote.getId())) {
                notes.set(i, updatedNote);
                break;
            }
        }
        saveNotes(notes);
    }

    // PUBLIC_INTERFACE
    public void deleteNote(String id) {
        List<Note> notes = getAllNotes();
        notes.removeIf(note -> note.getId().equals(id));
        saveNotes(notes);
    }

    private void saveNotes(List<Note> notes) {
        JSONArray arr = new JSONArray();
        for (Note note : notes) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", note.getId());
                obj.put("title", note.getTitle());
                obj.put("content", note.getContent());
                obj.put("timestamp", note.getTimestamp());
                arr.put(obj);
            } catch (JSONException e) { e.printStackTrace(); }
        }
        sharedPreferences.edit().putString(NOTES_KEY, arr.toString()).apply();
    }
}
