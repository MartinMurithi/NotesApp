package com.wachiramartin.architectureexample.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//When a change is made in the database, the version number increases
@Database(entities =  {Note.class}, version = 1)
public abstract class NoteDataBase extends RoomDatabase {
    //This class will follow the singleton pattern

    private static NoteDataBase instance;

    public abstract NoteDao noteDao();//Used to access the NoteDao class

    public  static NoteDataBase getInstance(Context context){
        //Synchronized ensures the database is accessed by one thread at a time
        if(instance == null){
            synchronized (NoteDataBase.class) {
                //Creates a RoomDatabase.Builder for a persistent database.
                instance = Room.databaseBuilder(context.getApplicationContext(), NoteDataBase.class, "note_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallBack)
                        .build();
            }
        }
        return instance;
    }

    //Populate the database on creation
    private static RoomDatabase.Callback roomCallBack= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDatabaseAsyncTask(instance).execute();
        }
    };

    private static class populateDatabaseAsyncTask extends AsyncTask<Void, Void, Void>{
        private NoteDao noteDao;

        private populateDatabaseAsyncTask(NoteDataBase db){
            this.noteDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            return null;
        }
    }
}