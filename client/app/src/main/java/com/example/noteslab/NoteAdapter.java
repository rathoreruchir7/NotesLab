package com.example.noteslab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    private static final DiffUtil.ItemCallback<Note> NOTE_DIFF =
            new DiffUtil.ItemCallback<Note>() {
                @Override
                public boolean areItemsTheSame(@NonNull Note oldNote,
                                               @NonNull Note newNote) {
                    return oldNote.getId() == newNote.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Note oldNote,
                                                  @NonNull Note newNote) {
                    return Objects.equals(oldNote.getTitle(), newNote.getTitle())
                            && Objects.equals(oldNote.getBody(), newNote.getBody());
                }
            };

    private final OnNoteClickListener clickListener;
    private final OnNoteLongClickListener longClickListener;

    public NoteAdapter(OnNoteClickListener clickListener,
                       OnNoteLongClickListener longClickListener) {
        super(NOTE_DIFF);
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
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        holder.title.setText(note.getTitle());
        holder.body.setText(note.getBody());

        holder.itemView.setOnClickListener(v ->
                clickListener.onNoteClick(note));

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onNoteLongClick(note);
            return true;
        });
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