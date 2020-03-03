package com.romanoindustries.booksearch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.romanoindustries.booksearch.bookmodel.Book;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView booksRecyclerView;
    private RecyclerView.Adapter<BooksAdapter.BookViewHolder> booksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        booksRecyclerView = findViewById(R.id.books_list);

        initRecyclerView();

    }

    private void initRecyclerView() {
        booksAdapter = new BooksAdapter(new ArrayList<Book>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        booksRecyclerView.setAdapter(booksAdapter);
        booksRecyclerView.setLayoutManager(layoutManager);

    }

    private void showProgressBar() {}
    private void hideProgressBar() {}
}
