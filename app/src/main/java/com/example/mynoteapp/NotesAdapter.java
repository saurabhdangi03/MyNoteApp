package com.example.mynoteapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> notes;
    private final NotesDatabaseHelper db;

    public NotesAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.db = new NotesDatabaseHelper(context);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        ImageView updateButton;
        ImageView deleteButton;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.contentTextView.setText(note.getContent());

        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager();
                UpdateNoteFragment updateFragment = new UpdateNoteFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("note_id", note.getId());
                updateFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, updateFragment).addToBackStack(null).commit();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteNote(note.getId());
                refreshData(db.getAllNotes());
                Toast.makeText(holder.itemView.getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<Note> newNotes) {
        notes = newNotes;
        notifyDataSetChanged();
    }
}
