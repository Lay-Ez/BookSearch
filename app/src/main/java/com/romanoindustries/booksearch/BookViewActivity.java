package com.romanoindustries.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.viewmodels.BookViewActivityViewModel;

public class BookViewActivity extends AppCompatActivity {
    private static final String TAG = "BookViewActivity";

    private BookViewActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        viewModel = new ViewModelProvider(this).get(BookViewActivityViewModel.class);
        viewModel.getBook().observe(this, new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                if (book == null) {
                    Log.d(TAG, "onChanged: book is now null");
                    return;
                }

                Log.d(TAG, "onChanged: book is now=" + book.getVolumeInfo().getTitle());
            }
        });
        viewModel.loadBook(getIntent().getStringExtra(Intent.EXTRA_CONTENT_QUERY));
    }
}
