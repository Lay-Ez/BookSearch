package com.romanoindustries.booksearch.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.networkutils.BookNetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BooksRepository {

    private static final String TAG = "BooksRepository";

    private static BooksRepository instance;
    private static MutableLiveData<List<Book>> booksMutable;
    private static MutableLiveData<Boolean> isLoading;

    public static BooksRepository getInstance() {
        if (instance == null) {
            instance = new BooksRepository();
            booksMutable = new MutableLiveData<>();
            isLoading = new MutableLiveData<>();
            booksMutable.setValue(new ArrayList<Book>()); /*default value to avoid NPE*/
            isLoading.setValue(false); /*default value to avoid NPE*/
        }
        return instance;
    }

    private BooksRepository() {}

    public LiveData<List<Book>> getBooks() {
        return booksMutable;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadBooks(String query, int searchMode) {
        URL url = BookNetworkUtils.composeURL(query, 40, searchMode);
        new FetchBooks().execute(url);
    }

    static class FetchBooks extends AsyncTask<URL, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            isLoading.setValue(true);
        }

        @Override
        protected List<Book> doInBackground(URL... urls) {
            String response = BookNetworkUtils.getResponseFromUrl(urls[0]);
            return BookNetworkUtils.parseBooksFromJson(response);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            booksMutable.setValue(books);
            isLoading.setValue(false);
        }
    }
}


















