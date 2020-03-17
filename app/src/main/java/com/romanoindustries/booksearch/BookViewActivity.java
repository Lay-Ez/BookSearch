package com.romanoindustries.booksearch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.romanoindustries.booksearch.viewmodels.BookViewActivityViewModel;

public class BookViewActivity extends AppCompatActivity {
    private static final String TAG = "BookViewActivity";
    private BookViewActivityViewModel viewBookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        viewBookViewModel = new ViewModelProvider(this).get(BookViewActivityViewModel.class);
        String bookUrl = getIntent().getStringExtra(Intent.EXTRA_CONTENT_QUERY);
        viewBookViewModel.loadBook(bookUrl);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
