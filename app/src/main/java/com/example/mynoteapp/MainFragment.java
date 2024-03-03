package com.example.mynoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mynoteapp.databinding.FragmentMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private NotesDatabaseHelper db;
    private NotesAdapter notesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize database and adapter
        db = new NotesDatabaseHelper(requireContext());
        notesAdapter = new NotesAdapter(db.getAllNotes(), requireContext());

        // Set layout manager and adapter for RecyclerView
        binding.notesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.notesRecyclerView.setAdapter(notesAdapter);

        // Set click listener for add button
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Replace the container with AddNoteFragment
                    fragmentTransaction.replace(R.id.fragment_container, new AddNoteFragment());

                    // Add the transaction to the back stack
                    fragmentTransaction.addToBackStack(null);

                    // Commit the transaction
                    fragmentTransaction.commit();

            }
        });

        // Set click listener for sign out button
        /*binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(requireContext(), "Signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireContext(), MainActivity.class));
                requireActivity().finish();
            }
        });*/

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
