package com.romanoindustries.booksearch.viewmodels;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.networkutils.BookNetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class BookViewActivityViewModel extends ViewModel {

    private static MutableLiveData<Book> bookMutableLiveData;
    private static MutableLiveData<Boolean> isLoading;
    private static MutableLiveData<Boolean> isRemoved;

    public BookViewActivityViewModel() {
        bookMutableLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        isRemoved = new MutableLiveData<>();
    }

    public void loadBook(String bookUrl) {
        new LoadBook().execute(bookUrl);
    }

    public LiveData<Book> getBook() {
        return bookMutableLiveData;
    }

    public void setBookMutableLiveData(Book book) {
        isRemoved.setValue(false);
        bookMutableLiveData.setValue(book);
    }

    public void setIsRemoved(boolean removed) {
        isRemoved.setValue(removed);
    }

    public LiveData<Boolean> getIsRemoved() {
        return isRemoved;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    static class LoadBook extends AsyncTask<String, Void, Book> {

        @Override
        protected void onPreExecute() {
            isLoading.setValue(true);
        }

        @Override
        protected Book doInBackground(String... strings) {
            if (strings == null || strings.length < 1) {
                return null;
            }
            try {
                String stringUrl = strings[0];
                URL url;
                url = new URL(stringUrl);
                String response = BookNetworkUtils.getResponseFromUrl(url);
                if (response.equals("")) {
                    return null;
                }
                JSONObject bookJson = new JSONObject(response);
                return BookNetworkUtils.parseJsonBook(bookJson);
            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Book book) {
            isLoading.setValue(false);
            bookMutableLiveData.setValue(book);
        }
    }
}
