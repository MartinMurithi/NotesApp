package com.wachiramartin.architectureexample.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wachiramartin.architectureexample.Database.Note;

import java.util.List;

/*Dao's have to be either either interfaces or abstract classes, we don''t provide the method bodies, we annotate it and room generate all code for us
*
* */
@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    //Selects all data from note_table and sorts them
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();
}
