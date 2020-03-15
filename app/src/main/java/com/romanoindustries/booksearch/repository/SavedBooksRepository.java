package com.romanoindustries.booksearch.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.roomstuff.BookDao;
import com.romanoindustries.booksearch.roomstuff.SavedBooksDatabase;

import java.util.List;

public class SavedBooksRepository {

    private BookDao bookDao;
    private LiveData<List<Book>> savedBooks;

    public SavedBooksRepository(Application application) {
        SavedBooksDatabase booksDatabase = Room.databaseBuilder(
                application,
                SavedBooksDatabase.class,
                "books_database")
                .build();

        bookDao = booksDatabase.bookDao();
    }

    public void insertBook(Book book) {
        new InsertBookAsyncTask(bookDao).execute(book);
    }

    public void updateBook(Book book) {
        new UpdateBookAsyncTask(bookDao).execute(book);
    }

    public void deleteBook(Book book) {
        new DeleteBookAsyncTask(bookDao).execute(book);
    }

    public void deleteAllBooks() {
        new DeleteBookAsyncTask(bookDao).execute();
    }

    public LiveData<List<Book>> getSavedBooks() {
        return bookDao.getAllBooks();
    }

    private static class InsertBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao bookDao;

        public InsertBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.insert(books[0]);
            return null;
        }
    }

    private static class UpdateBookAsyncTask extends AsyncTask<Book, Void, Void> {

        private BookDao bookDao;

        public UpdateBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.update(books[0]);
            return null;
        }
    }

    private static class DeleteBookAsyncTask extends android.os.AsyncTask<Book, Void, Void> {

        private BookDao bookDao;

        public DeleteBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.delete(books[0]);
            return null;
        }
    }

    private static class DeleteAllBooksAsyncTask extends AsyncTask<Void, Void, Void> {

        private BookDao bookDao;

        public DeleteAllBooksAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Void...voids) {
            bookDao.deleteAllBooks();
            return null;
        }
    }
}
