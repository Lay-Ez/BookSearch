package com.romanoindustries.booksearch.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.repository.BooksRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private static final String TAG = "MainActivityViewModel";

    private LiveData<List<Book>> booksLiveData;
    private MediatorLiveData<List<Book>> mediatorLiveData;
    private BooksRepository booksRepository;

    public void init() {
        if (mediatorLiveData != null) {
            return;
        }
        booksRepository = BooksRepository.getInstance();
        booksLiveData = booksRepository.getBooks();

        mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(booksLiveData.getValue());
        mediatorLiveData.addSource(booksLiveData, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                Log.d(TAG, "onChanged: mediator received new value MVVM");
                mediatorLiveData.setValue(books);
            }
        });
    }

    public LiveData<List<Book>> getBooks() {
        return mediatorLiveData;
    }

    public void loadBooks(String query) {
        booksRepository.loadBooks(query);
    }

}
