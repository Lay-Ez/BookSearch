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

    private static BooksRepository instance;
    private static MutableLiveData<List<Book>> booksMutable;
    private static MutableLiveData<Boolean> isLoading;
    private static MutableLiveData<Boolean> isLoadingMore;
    private static URL lastAccessedUrl = null;
    private static int totalLoadedResults;
    private static boolean isEndOfListReached;

    public static BooksRepository getInstance() {
        if (instance == null) {
            instance = new BooksRepository();
            booksMutable = new MutableLiveData<>();
            isLoading = new MutableLiveData<>();
            isLoadingMore = new MutableLiveData<>();
            booksMutable.setValue(new ArrayList<Book>()); /*default value to avoid NPE*/
            isLoading.setValue(false); /*default value to avoid NPE*/
            isLoadingMore.setValue(false);
            totalLoadedResults = 0;
            isEndOfListReached = false;
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

    public LiveData<Boolean> getIsLoadingMore() {
        return isLoadingMore;
    }

    public void loadBooks(String query, int searchMode) {
        isEndOfListReached = false;
        URL url = BookNetworkUtils.composeURL(query, 40, searchMode);
        lastAccessedUrl = url;
        new FetchBooks().execute(url);
    }

    public void loadMore() {
        if (isEndOfListReached) {
            return;
        }
        URL lastUrl = lastAccessedUrl;
        URL urlWithStartingIndex = BookNetworkUtils.recomposeURLWithOffset(lastUrl, totalLoadedResults);
        new LoadMoreBooks().execute(urlWithStartingIndex);
    }

    static class FetchBooks extends AsyncTask<URL, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            isLoading.setValue(true);
        }

        @Override
        protected List<Book> doInBackground(URL... urls) {
            String response = BookNetworkUtils.getResponseFromUrl(urls[0]);
            List<Book> result = BookNetworkUtils.parseBooksFromJson(response);
            totalLoadedResults = result.size();
            return  result;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            booksMutable.setValue(books);
            isLoading.setValue(false);
        }
    }

    static class LoadMoreBooks extends AsyncTask<URL, Void, List<Book>> {

        @Override
        protected void onPreExecute() {
            isLoadingMore.setValue(true);
        }

        @Override
        protected List<Book> doInBackground(URL... urls) {
            String response = BookNetworkUtils.getResponseFromUrl(urls[0]);
            List<Book> newlyLoadedBooks = BookNetworkUtils.parseBooksFromJson(response);
            totalLoadedResults += newlyLoadedBooks.size();
            return newlyLoadedBooks;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            isLoadingMore.setValue(false);
            if (books.size() == 0 || booksMutable.getValue() == null) {
                isEndOfListReached = true;
                return;
            }
            List<Book> oldBooks = booksMutable.getValue();
            oldBooks.addAll(books);
            booksMutable.setValue(oldBooks);
        }
    }
}


















