package com.example.noteslab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private final List<Note> allNotes;
    private final OnNoteClickListener clickListener;
    private final OnNoteLongClickListener longClickListener;

    public NoteAdapter(List<Note> allNotes,
                       OnNoteClickListener clickListener,
                       OnNoteLongClickListener longClickListener) {
        this.allNotes = allNotes;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public interface OnNoteLongClickListener {
        void onNoteLongClick(Note note);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull NoteViewHolder holder, int position) {

        Note note = allNotes.get(position);

        // Your two lines go here:
        holder.title.setText(note.getTitle());
        holder.body.setText(note.getBody());

        holder.itemView.setOnClickListener(v ->
                clickListener.onNoteClick(note)
        );

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onNoteLongClick(note);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return allNotes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView body;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            body = itemView.findViewById(R.id.itemBody);
        }
    }
}
