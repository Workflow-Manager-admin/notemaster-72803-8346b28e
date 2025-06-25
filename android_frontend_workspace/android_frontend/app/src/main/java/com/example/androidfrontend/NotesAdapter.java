package com.example.androidfrontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

// PUBLIC_INTERFACE
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> notes;
    private final OnNoteClickListener noteClickListener;

    public interface OnNoteClickListener {
        void onNoteClicked(Note note);
        void onNoteLongPressed(Note note);
    }

    public NotesAdapter(List<Note> notes, OnNoteClickListener listener) {
        this.notes = notes != null ? notes : Collections.emptyList();
        this.noteClickListener = listener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes != null ? notes : Collections.emptyList();
        notifyDataSetChanged();
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
        holder.itemView.setOnClickListener(v -> noteClickListener.onNoteClicked(note));
        holder.itemView.setOnLongClickListener(v -> {
            noteClickListener.onNoteLongPressed(note);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        NoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            content = itemView.findViewById(R.id.note_content);
        }
    }
}
