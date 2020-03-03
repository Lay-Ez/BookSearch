package com.romanoindustries.booksearch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.repository.BooksRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Book>> booksData;
    private BooksRepository booksRepo;

    public void init() {
        if (booksData != null) {
            return;
        }
        booksRepo = BooksRepository.getInstance();
        booksData = booksRepo.getBooks();
    }

    public LiveData<List<Book>> getBooksData() {
        return booksData;
    }
}
