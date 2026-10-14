package com.example.noteslab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    
    private NoteAdapter noteAdapter;
    private NotesViewModel viewModel;

    private final ActivityResultLauncher<Intent> noteEditorLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            String title =
                                    result.getData().getStringExtra("title");

                            String body =
                                    result.getData().getStringExtra("body");

                            int noteId = result.getData().getIntExtra("note_id", -1);

//                            allNotes.add(0, new Note(title, body));
//                            noteAdapter.notifyItemInserted(0);
//
//                            Toast.makeText(
//                                    this,
//                                    "Saved: " + title + "total: " + allNotes.size(),
//                                    Toast.LENGTH_SHORT
//                            ).show();
                            //now saving the data to the database
                            Note note = new Note(title, body);
                            if (noteId == -1) {
                                viewModel.insert(note);
                            } else {
                                note.setId(noteId);
                                viewModel.update(note);
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteAdapter = new NoteAdapter(note ->
        {
            Intent intent = new Intent(this, NoteEditorActivity.class);
            intent.putExtra("note_id", note.getId());
            intent.putExtra("title", note.getTitle());
            intent.putExtra("body", note.getBody());
            noteEditorLauncher.launch(intent);
        }, note -> confirmDelete(note));
        recyclerView.setAdapter(noteAdapter);

        //Loading the data from the Room database
        viewModel.getNotes().observe(this, notes -> {
            noteAdapter.submitList(notes);
        });

        findViewById(R.id.floatingActionButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteEditorActivity.class);
            noteEditorLauncher.launch(intent);
        });
    }

    private void confirmDelete(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Delete note?")
                .setMessage(note.getTitle())
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> viewModel.delete(note))
                .show();
    }
}