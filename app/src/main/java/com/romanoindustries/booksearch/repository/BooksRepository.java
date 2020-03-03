package com.romanoindustries.booksearch.repository;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.networkutils.BookNetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BooksRepository {

    private static BooksRepository instance;
    private List<Book> booksData = new ArrayList<>();

    public static BooksRepository getInstance() {
        if (instance == null) {
            instance = new BooksRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Book>> getBooks() {
        // TODO: 03.03.2020 implement
        return null;
    }

    private void setBooksData() {
        // TODO: 03.03.2020 implement
    }

    class FetchBooksData extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... strings) {
            String query = strings[0];
            URL url = BookNetworkUtils.composeURL(query, 40);
            String response = BookNetworkUtils.getResponseFromUrl(url);
            return BookNetworkUtils.parseBooksFromJson(response);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            booksData = books;
        }
    }
}


















