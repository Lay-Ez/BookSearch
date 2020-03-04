package com.romanoindustries.booksearch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.viewmodels.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView booksRecyclerView;
    private BooksAdapter booksAdapter;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        booksRecyclerView = findViewById(R.id.books_list);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();
        initRecyclerView();

        mainActivityViewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                booksAdapter.updateBooks(books);
            }
        });

        mainActivityViewModel.loadBooks("50 shades");
    }

    private void initRecyclerView() {
        booksAdapter = new BooksAdapter(mainActivityViewModel.getBooks().getValue());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        booksRecyclerView.setAdapter(booksAdapter);
        booksRecyclerView.setLayoutManager(layoutManager);
    }
}
