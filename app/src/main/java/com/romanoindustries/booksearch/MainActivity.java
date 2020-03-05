package com.romanoindustries.booksearch;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.viewmodels.MainActivityViewModel;

import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView booksRecyclerView;
    private BooksAdapter booksAdapter;
    private MainActivityViewModel mainActivityViewModel;
    private TextView emptyTextView;
    private TextInputEditText searchInputText;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        booksRecyclerView = findViewById(R.id.books_list);
        emptyTextView = findViewById(R.id.empty_text_view);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        searchInputText = findViewById(R.id.edit_text_search);
        searchInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        progressBar = findViewById(R.id.progress_bar);


        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();
        initRecyclerView();

        mainActivityViewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (books == null || books.size() == 0) {
                    hideList();
                    return;
                }
                books.sort(new Comparator<Book>() {
                    @Override
                    public int compare(Book book1, Book book2) {
                        int countRatings1 = book1.getVolumeInfo().getRatingsCount();
                        int countRatings2 = book2.getVolumeInfo().getRatingsCount();
                        return countRatings2 - countRatings1;
                    }
                });
                booksAdapter.updateBooks(books);
                showList();
            }
        });

        mainActivityViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showProgressBar();
                } else {
                    hideProgressBar();
                }
            }
        });
    }

    private void initRecyclerView() {
        booksAdapter = new BooksAdapter(mainActivityViewModel.getBooks().getValue());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        booksRecyclerView.setAdapter(booksAdapter);
        booksRecyclerView.setLayoutManager(layoutManager);
        hideList();
    }

    private void hideList() {
        booksRecyclerView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
    }

    private void showList() {
        emptyTextView.setVisibility(View.GONE);
        booksRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        emptyTextView.setVisibility(View.GONE);
        booksRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void performSearch() {
        String query = searchInputText.getText().toString();
        mainActivityViewModel.loadBooks(query);
        searchInputText.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchInputText.getWindowToken(), 0);
    }
}
