package com.romanoindustries.booksearch.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.repository.SavedBooksRepository;

import java.util.List;

public class SavedBooksViewModel extends AndroidViewModel {

    private SavedBooksRepository repository;
    private LiveData<List<Book>> savedBooks;

    public SavedBooksViewModel(@NonNull Application application) {
        super(application);
        repository = new SavedBooksRepository(application);
        savedBooks = repository.getSavedBooks();
    }

    public void insert(Book book) {
        long currentTime = System.currentTimeMillis();
        book.setSavedTime(currentTime);
        book.setPersonalNote("");
        repository.insertBook(book);
    }

    public void update(Book book) {
        repository.updateBook(book);
    }

    public void delete(Book book) {
        repository.deleteBook(book);
    }

    public void deleteAllBooks() {
        repository.deleteAllBooks();
    }

    public LiveData<List<Book>> getSavedBooks() {
        return savedBooks;
    }
}
