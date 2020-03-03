package com.romanoindustries.booksearch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.romanoindustries.booksearch.bookmodel.Book;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Book>> booksData;

    public LiveData<List<Book>> getBooksData() {
        return booksData;
    }
}
