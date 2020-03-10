package com.romanoindustries.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class BookViewActivity extends AppCompatActivity {
    private static final String TAG = "BookViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        loadBook();
    }

    private void loadBook() {
        Intent intent = getIntent();
        String bookUrl = intent.getStringExtra(Intent.EXTRA_CONTENT_QUERY);
        Log.d(TAG, "loadBook: bookUrl=" + bookUrl);
    }
}
