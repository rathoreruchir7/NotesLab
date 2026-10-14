package com.example.noteslab;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotesRepository {
    private final NoteDao noteDao;
    private final LiveData<List<Note>> notes;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public NotesRepository(Context context) {
        NotesDatabase database = Room.databaseBuilder(
                context.getApplicationContext(),
                NotesDatabase.class,
                "notes_db"
        ).build();

        noteDao = database.noteDao();
        notes = noteDao.observeAllNotes();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public void insert(Note note) {
        executor.execute(() -> noteDao.insert(note));
    }

    public void update(Note note) {
        executor.execute(() -> noteDao.update(note));
    }

    public void delete(Note note) {
        executor.execute(() -> noteDao.delete(note));
    }
}