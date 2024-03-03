package com.example.mynoteapp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mynoteapp.databinding.FragmentUpdateNoteBinding;


public class UpdateNoteFragment extends Fragment
{

    private FragmentUpdateNoteBinding binding;
    private NotesDatabaseHelper db;
    private int noteId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpdateNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new NotesDatabaseHelper(requireContext());

        Bundle args = getArguments();
        if (args != null) {
            noteId = args.getInt("note_id", -1);
        }

        if (noteId == -1) {
            requireActivity().finish();
            return;
        }

        Note note = db.getNoteByID(noteId);
        if (note == null) {
            requireActivity().finish();
            return;
        }

        binding.updateTitleEditText.setText(note.getTitle());
        binding.updateContentEditText.setText(note.getContent());

        binding.updateSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = binding.updateTitleEditText.getText().toString();
                String newContent = binding.updateContentEditText.getText().toString();

                Note updatedNote = new Note(noteId, newTitle, newContent);
                db.updateNote(updatedNote);
                FragmentManager fragmentManager = getParentFragmentManager();
                // Assuming NextFragment is the next fragment to be opened
                MainFragment nextFragment = new MainFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, nextFragment).commit();
                Toast.makeText(requireContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

/*
public class UpdateNoteFragment extends Fragment {

    private FragmentUpdateNoteBinding binding;
    private NotesDatabaseHelper db;
    private int noteId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new NotesDatabaseHelper(requireContext());

        noteId = requireActivity().getIntent().getIntExtra("note_id", -1);
        if (noteId == -1) {
            requireActivity().finish();
            return;
        }

        final Note note = db.getNoteByID(noteId);
        if (note == null) {
            // Handle null case
            return;
        }

        binding.updateTitleEditText.setText(note.getTitle());
        binding.updateContentEditText.setText(note.getContent());

        binding.updateSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = binding.updateTitleEditText.getText().toString();
                String newContent = binding.updateContentEditText.getText().toString();

                Note updatedNote = new Note(noteId, newTitle, newContent);
                db.updateNote(updatedNote);
                requireActivity().finish();
                Toast.makeText(requireContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Replace the container with AddNoteFragment
                fragmentTransaction.replace(R.id.fragment_container, new MainFragment());

                // Add the transaction to the back stack
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });
    }
}
*/
