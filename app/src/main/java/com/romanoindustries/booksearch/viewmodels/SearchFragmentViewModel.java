package com.romanoindustries.booksearch.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.repository.BooksRepository;

import java.util.List;

public class SearchFragmentViewModel extends ViewModel {
    private static final String TAG = "SearchFragmentViewModel";

    private LiveData<List<Book>> booksLiveData;
    private LiveData<Boolean> isLoading;
    private MediatorLiveData<List<Book>> mediatorLiveDataBooks;
    private MediatorLiveData<Boolean> mediatorLiveDataIsLoading;
    private BooksRepository booksRepository;

    public void init() {
        if (mediatorLiveDataBooks != null) {
            return;
        }

        booksRepository = BooksRepository.getInstance();

        booksLiveData = booksRepository.getBooks();
        mediatorLiveDataBooks = new MediatorLiveData<>();
        mediatorLiveDataBooks.setValue(booksLiveData.getValue());
        mediatorLiveDataBooks.addSource(booksLiveData, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                mediatorLiveDataBooks.setValue(books);
            }
        });

        isLoading = booksRepository.getIsLoading();
        mediatorLiveDataIsLoading = new MediatorLiveData<>();
        mediatorLiveDataIsLoading.setValue(isLoading.getValue());
        mediatorLiveDataIsLoading.addSource(isLoading, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mediatorLiveDataIsLoading.setValue(aBoolean);
            }
        });
    }

    public LiveData<List<Book>> getBooks() {
        return mediatorLiveDataBooks;
    }

    public LiveData<Boolean> getIsLoading() {
        return mediatorLiveDataIsLoading;
    }

    public void loadBooks(String query, int searchMode) {
        booksRepository.loadBooks(query, searchMode);
    }

    public void loadMoreBooks() {
        booksRepository.loadMore();
    }
}
