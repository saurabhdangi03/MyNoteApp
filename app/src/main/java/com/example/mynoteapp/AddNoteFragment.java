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

import com.example.mynoteapp.databinding.FragmentAddNoteBinding;


public class AddNoteFragment extends Fragment {

    private FragmentAddNoteBinding binding;
    private NotesDatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        db = new NotesDatabaseHelper(requireContext());

        binding.saveButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.titleEditTexts.getText().toString();
                String content = binding.contentEditTexts.getText().toString();
                Note note = new Note(0, title, content);
                db.insertNote(note);
                Toast.makeText(requireContext(), "Note Saved", Toast.LENGTH_SHORT).show();
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
