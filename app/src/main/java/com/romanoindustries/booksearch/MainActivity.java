package com.romanoindustries.booksearch;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.romanoindustries.booksearch.BookData.Book;
import com.romanoindustries.booksearch.NetworkUtils.BookNetworkUtils;

import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = BookNetworkUtils.composeURL("harry", 5);
                String response = BookNetworkUtils.getResponseFromUrl(url);
                List<Book> books = BookNetworkUtils.parseBooksFromJson(response);
                books.forEach(new Consumer<Book>() {
                    @Override
                    public void accept(Book book) {
                        Log.d(TAG, "accept: " + book.getVolumeInfo());
                    }
                });
            }
        }).start();

    }
}
