package com.romanoindustries.booksearch.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.networkutils.BookNetworkUtils;

import java.net.URL;
import java.util.List;

public class BooksRepository {

    private static final String TAG = "BooksRepository";
    private static BooksRepository instance;
    private static MutableLiveData<List<Book>> booksMutable;

    public static BooksRepository getInstance() {
        if (instance == null) {
            instance = new BooksRepository();
        }
        return instance;
    }

    private BooksRepository() {
        booksMutable = new MutableLiveData<>();
    }

    public LiveData<List<Book>> getBooks() {
        return booksMutable;
    }

    public void loadBooks(String query) {
        new FetchData().execute(query);
    }

    static class FetchData extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... strings) {
            String query = strings[0];
            URL url = BookNetworkUtils.composeURL(query);
            String response = BookNetworkUtils.getResponseFromUrl(url);
            return BookNetworkUtils.parseBooksFromJson(response);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            booksMutable.setValue(books);
        }
    }
}


















