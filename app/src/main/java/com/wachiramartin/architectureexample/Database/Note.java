package com.wachiramartin.architectureexample.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//A room annotation, at compile time, it will create all the necessary code to create an SQLite table for this object.
@Entity(tableName = "note_table")
public class Note {
    //THIS IS THE ENTITY CLASS

    //Member variables we want the table to contain
    @PrimaryKey(autoGenerate = true)
    private  int id;

    private String title;
    private String description;
    private int priority;

    // Room will automatically generate columns for this table

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

}
