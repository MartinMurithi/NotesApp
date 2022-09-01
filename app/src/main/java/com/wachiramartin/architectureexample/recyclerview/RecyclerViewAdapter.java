package com.wachiramartin.architectureexample.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wachiramartin.architectureexample.Database.Note;
import com.wachiramartin.architectureexample.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<Note> listNotes = new ArrayList<>();
    private RecyclerViewInterface recyclerViewInterface;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_note_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note currentNote = listNotes.get(position);
        holder.tv_noteTitle.setText(currentNote.getTitle());
        holder.tv_priority.setText(String.valueOf(currentNote.getPriority()));

    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public void setNotes(List<Note> listNotes) {
        this.listNotes = listNotes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {
        return listNotes.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_noteTitle;
        private TextView tv_description;
        private TextView tv_priority;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_noteTitle = itemView.findViewById(R.id.tv_noteTitle);
            tv_priority = itemView.findViewById(R.id.tv_notePriority);
            tv_description = itemView.findViewById(R.id.tv_noteDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if (recyclerViewInterface != null && position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onClickListener(listNotes.get(position));
                    }
                }
            });
        }
    }

    public void setOnClickListener(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }
}
