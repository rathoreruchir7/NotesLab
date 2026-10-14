package com.example.noteslab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NoteEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_editor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.note_editor), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText titleEdit = findViewById(R.id.Itemtitle);
        EditText  bodyEdit = findViewById(R.id.body);
        Button saveButton = findViewById(R.id.saveButton);

        int noteId = getIntent().getIntExtra("note_id", -1);

        if (noteId != -1) {
            titleEdit.setText(getIntent().getStringExtra("title"));
            bodyEdit.setText(getIntent().getStringExtra("body"));
        }

        saveButton.setOnClickListener(v -> {
            Intent result = new Intent();
            if (noteId != -1) {
                result.putExtra("note_id", noteId);
            }
            result.putExtra("title", titleEdit.getText().toString());
            result.putExtra("body", bodyEdit.getText().toString());
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
