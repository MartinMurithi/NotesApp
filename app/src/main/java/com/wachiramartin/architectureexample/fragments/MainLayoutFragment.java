package com.wachiramartin.architectureexample.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wachiramartin.architectureexample.Database.Note;
import com.wachiramartin.architectureexample.R;
import com.wachiramartin.architectureexample.recyclerview.RecyclerViewAdapter;
import com.wachiramartin.architectureexample.recyclerview.RecyclerViewInterface;
import com.wachiramartin.architectureexample.ViewModel;

import java.util.List;

public class MainLayoutFragment extends Fragment {
    public static final String KEY = "REQUEST KEY";

    private ViewModel viewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter();
    ;
    private TextView tv_noteTitle;
    private TextView tv_noteDescription;
    private TextView tv_priority;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_layout, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        floatingActionButton = view.findViewById(R.id.fab_addNote);
        tv_noteTitle = view.findViewById(R.id.tv_noteTitle);
        tv_noteDescription = view.findViewById(R.id.tv_noteDescription);
        tv_priority = view.findViewById(R.id.tv_notePriority);

        //Open AddNote fragment
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNoteFragment fragment = new AddNoteFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();

            }
        });

        //fragmentResultListener API to get results from the addNote fragment
        getParentFragmentManager().setFragmentResultListener("KEY", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String title = result.getString("titleKey");
                String description = result.getString("descriptionKey");
                int priority = result.getInt("priorityKey");

                Note note = new Note(title, description, priority);
                viewModel.insert(note);
                Toast.makeText(getContext(), "Note saved ", Toast.LENGTH_SHORT).show();

                //Updating the note title , description and priority
                int id = result.getInt("EditKeyID", -1);
                if (id == -1) {
                    Toast.makeText(getContext(), "Note cannot be updated", Toast.LENGTH_SHORT).show();
                    return;
                }

                String updateTitle = result.getString("titleKey");
                String updateDescription = result.getString("descriptionKey");
                int updateUPriority = result.getInt("priorityKey");

                Note updatedNote = new Note(updateTitle, updateDescription, updateUPriority);
                note.setId(id);
                viewModel.update(updatedNote);
                Toast.makeText(getContext(), "Note Updated", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getAllNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //Update the recycler view
                recyclerViewAdapter.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(recyclerViewAdapter.getNoteAt(viewHolder.getAbsoluteAdapterPosition()));
                Toast.makeText(getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        recyclerViewAdapter.setOnClickListener(new RecyclerViewInterface() {
            @Override
            public void onClickListener(Note note) {
                AddNoteFragment addNoteFragment = new AddNoteFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addNoteFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();

                Bundle bundle = new Bundle();
                bundle.putInt("EditKeyID", note.getId());
                bundle.putString("EditKeyTitle", note.getTitle());
                bundle.putString("EditKeyDescription", note.getDescription());
                bundle.putInt("EditKeyPriority", note.getPriority());

                getParentFragmentManager().setFragmentResult("EDITKEY", bundle);
            }
        });
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(recyclerViewAdapter);
    }
}