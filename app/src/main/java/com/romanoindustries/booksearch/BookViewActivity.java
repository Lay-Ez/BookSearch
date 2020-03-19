package com.romanoindustries.booksearch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.viewmodels.BookViewActivityViewModel;
import com.romanoindustries.booksearch.viewmodels.SavedBooksViewModel;

import java.util.List;

public class BookViewActivity extends AppCompatActivity {
    private static final String TAG = "BookViewActivity";
    private BookViewActivityViewModel viewBookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        viewBookViewModel = new ViewModelProvider(this).get(BookViewActivityViewModel.class);
        String bookUrl = getIntent().getStringExtra(Intent.EXTRA_CONTENT_QUERY);
        boolean loadFromLocalStorage = getIntent().getBooleanExtra(Intent.EXTRA_FROM_STORAGE, false);
        if (loadFromLocalStorage) {
            loadBookFromDb();
        } else {
            viewBookViewModel.loadBook(bookUrl);
        }

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

    private void loadBookFromDb() {
        SavedBooksViewModel savedBooksViewModel =
                new ViewModelProvider.AndroidViewModelFactory(getApplication()).
                        create(SavedBooksViewModel.class);

        final int positionInTheViewModel = getIntent().getIntExtra(Intent.EXTRA_INDEX, 0);
        savedBooksViewModel.getSavedBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (books != null) {
                    Book book = books.get(positionInTheViewModel);
                    viewBookViewModel.setBookMutableLiveData(book);
                }
            }
        });
    }
}
