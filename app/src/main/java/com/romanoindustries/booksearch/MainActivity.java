package com.romanoindustries.booksearch;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        booksRecyclerView = findViewById(R.id.books_list);
        emptyTextView = findViewById(R.id.empty_text_view);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();
        initRecyclerView();

        mainActivityViewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (books.size() == 0) {
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

        //for tests only
        mainActivityViewModel.loadBooks("Hello");
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
}
