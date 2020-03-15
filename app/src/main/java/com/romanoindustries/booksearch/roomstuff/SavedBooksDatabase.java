package com.romanoindustries.booksearch.roomstuff;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.romanoindustries.booksearch.bookmodel.Book;

@Database(entities = {Book.class}, version = 2)
@TypeConverters({RoomConverters.class})
public abstract class SavedBooksDatabase extends RoomDatabase {

    private static SavedBooksDatabase instance;

    public abstract BookDao bookDao();

    public static synchronized SavedBooksDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SavedBooksDatabase.class,
                    "books_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
