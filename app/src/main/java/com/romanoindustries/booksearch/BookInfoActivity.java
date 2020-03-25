package com.romanoindustries.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.bookmodel.VolumeInfo;
import com.romanoindustries.booksearch.viewmodels.SavedBooksViewModel;

import java.util.List;

public class BookInfoActivity extends AppCompatActivity {

    private TextView titleTv;
    private TextView authorTv;
    private TextView publisherTv;
    private TextView dateTv;
    private TextView pagesCountTv;
    private TextView isbn10Tv;
    private TextView isbn13Tv;
    private TextView langTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        Toolbar toolbar = findViewById(R.id.info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initViews();

        final String bookUrl = getIntent().getStringExtra(Intent.EXTRA_CONTENT_QUERY);
        SavedBooksViewModel savedBooksViewModel =
                new ViewModelProvider.AndroidViewModelFactory(getApplication()).
                        create(SavedBooksViewModel.class);
        savedBooksViewModel.getSavedBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (books != null && !books.isEmpty()) {
                    for (Book book : books) {
                        if (book.getSelfLink().equals(bookUrl)) {
                            displayBook(book);
                            return;
                        }
                    }
                }
            }
        });


    }

    private void displayBook(Book book) {
        VolumeInfo volumeInfo = book.getVolumeInfo();

        titleTv.setText(volumeInfo.getTitle());

        List<String> authors = volumeInfo.getAuthors();
        authorTv.setText("");
        for (int i = 0; i < authors.size(); i++) {
            if (i == authors.size() - 1) {
                authorTv.append(authors.get(i));
            } else {
                authorTv.append(authors.get(i) + ", ");
            }
        }

        publisherTv.setText(volumeInfo.getPublisher());
        dateTv.setText(volumeInfo.getPublishedDate());
        pagesCountTv.setText(String.valueOf(volumeInfo.getPageCount()));
        isbn10Tv.setText(volumeInfo.getIsbn10());
        isbn13Tv.setText(volumeInfo.getIsbn13());
        langTv.setText(volumeInfo.getLanguage());
    }

    private void initViews() {
        titleTv = findViewById(R.id.info_title_tv);
        authorTv = findViewById(R.id.info_author_tv);
        publisherTv = findViewById(R.id.info_publisher_tv);
        dateTv = findViewById(R.id.info_publication_date_tv);
        pagesCountTv = findViewById(R.id.info_pages_tv);
        isbn10Tv = findViewById(R.id.isbn10_tv);
        isbn13Tv = findViewById(R.id.isbn13_tv);
        langTv = findViewById(R.id.info_laguage_tv);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
