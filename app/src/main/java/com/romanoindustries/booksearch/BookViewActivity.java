package com.romanoindustries.booksearch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.romanoindustries.booksearch.viewmodels.BookViewActivityViewModel;

public class BookViewActivity extends AppCompatActivity {
    private static final String TAG = "BookViewActivity";

    private BookViewActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.book_view_collapsing);
        collapsingToolbarLayout.setTitle("Hello!");
    }
}
