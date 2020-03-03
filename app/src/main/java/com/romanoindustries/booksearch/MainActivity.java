package com.romanoindustries.booksearch;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.networkutils.BookNetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView booksRecyclerView;
    private RecyclerView.Adapter<BooksAdapter.BookViewHolder> booksAdapter;
//    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        booksRecyclerView = findViewById(R.id.books_list);

//        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
////        mainActivityViewModel.getBooksData().observe(this, new Observer<List<Book>>() {
////            @Override
////            public void onChanged(List<Book> books) {
////                booksAdapter.notifyDataSetChanged();
////            }
////        });

        new TestFetchBooks().execute("Agatha");
    }

    private void initRecyclerView() {
//        booksAdapter = new BooksAdapter(mainActivityViewModel.getBooksData().getValue());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        booksRecyclerView.setAdapter(booksAdapter);
        booksRecyclerView.setLayoutManager(layoutManager);
    }

    class TestFetchBooks extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... strings) {
            String query = strings[0];
            URL url = BookNetworkUtils.composeURL(query, 40);
            String response = BookNetworkUtils.getResponseFromUrl(url);
            return BookNetworkUtils.parseBooksFromJson(response);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            booksAdapter = new BooksAdapter(books);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            booksRecyclerView.setAdapter(booksAdapter);
            booksRecyclerView.setLayoutManager(layoutManager);
        }
    }
}
