package com.wachiramartin.architectureexample.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.wachiramartin.architectureexample.R;


public class AddNoteFragment extends Fragment {

    private EditText et_title;
    private EditText et_description;
    private TextView tv_priority;
    private NumberPicker numberPicker_priority;
    private Button save;
    private Button cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        et_title = view.findViewById(R.id.et_noteTitle);
        et_description = view.findViewById(R.id.et_description);
        tv_priority = view.findViewById(R.id.tv_priority);
        numberPicker_priority = view.findViewById(R.id.numberPicker);
        save = view.findViewById(R.id.button_save);
        cancel = view.findViewById(R.id.button_cancel);

        numberPickerMinMaxValues();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });


        //Receive data when user clicks to update the values
        getParentFragmentManager().setFragmentResultListener("EDITKEY", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                et_title.setText(result.getString("EditKeyTitle"));
                et_description.setText(result.getString("EditKeyDescription"));
                numberPicker_priority.setValue(result.getInt("EditKeyPriority", 3));
            }
        });


        return view;
    }

    private void saveNote() {
        String noteTitle = et_title.getText().toString();
        String noteDescription = et_description.getText().toString();
        int priority = numberPicker_priority.getValue();

        if (noteTitle.trim().isEmpty() || noteDescription.trim().isEmpty()) {
            Toast.makeText(getContext(), "Please enter Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("titleKey", noteTitle);
        bundle.putString("descriptionKey", noteDescription);
        bundle.putInt("priorityKey", priority);

        //Get the ID of the note selected and put it in the bundle if its not -1
        int id = bundle.getInt("EditKeyID", -1);
        if(id != -1){
            bundle.putInt("EditKeyID", id);
        }

        //Use setFragmentResult API to send data to MainLayoutFragment
        getParentFragmentManager().setFragmentResult("KEY", bundle);
        getActivity().getSupportFragmentManager().popBackStack();
    }




    private void numberPickerMinMaxValues() {
        numberPicker_priority.setMinValue(1);
        numberPicker_priority.setMaxValue(10);
    }

}