package com.romanoindustries.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BookInfoActivity extends AppCompatActivity {

    public static final String BOOK_TITLE = "book_title";
    public static final String BOOK_AUTHORS = "bool_authors";
    public static final String BOOK_PUBLISHER = "book_publisher";
    public static final String BOOK_DATE = "book_date";
    public static final String BOOK_PAGES = "book_pages";
    public static final String BOOK_ISBN10 = "book_isbn10";
    public static final String BOOK_ISBN13 = "book_isbn13";
    public static final String BOOK_LANG = "book_lang";

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
        displayInfo(getIntent());

    }

    private void displayInfo(Intent intent) {
        titleTv.setText(intent.getStringExtra(BOOK_TITLE));
        authorTv.setText(intent.getStringExtra(BOOK_AUTHORS));
        publisherTv.setText(intent.getStringExtra(BOOK_PUBLISHER));
        dateTv.setText(intent.getStringExtra(BOOK_DATE));
        pagesCountTv.setText(intent.getStringExtra(BOOK_PAGES));
        isbn10Tv.setText(intent.getStringExtra(BOOK_ISBN10));
        isbn13Tv.setText(intent.getStringExtra(BOOK_ISBN13));
        langTv.setText(intent.getStringExtra(BOOK_LANG));
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
