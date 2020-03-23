package com.romanoindustries.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.bookmodel.VolumeInfo;
import com.romanoindustries.booksearch.imagetransformation.PicassoHelper;
import com.romanoindustries.booksearch.viewmodels.SavedBooksViewModel;

import java.util.List;
import java.util.Objects;

public class EditNoteActivity extends AppCompatActivity {
    private static final String TAG = "EditNoteActivity";

    private ImageView thumbIv;
    private TextView authorTv;
    private TextView titleTv;
    private TextInputEditText noteTextInput;
    private Button saveBtn;
    private Button cancelBtn;
    private Book currentlyEditedBook;
    private SavedBooksViewModel savedBooksViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note_activity);
        initViews();

        final String bookUrl = getIntent().getStringExtra(Intent.EXTRA_CONTENT_QUERY);
        savedBooksViewModel =
                new ViewModelProvider.AndroidViewModelFactory(getApplication()).
                        create(SavedBooksViewModel.class);
        savedBooksViewModel.getSavedBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (books != null && !books.isEmpty()) {
                    for (Book book : books) {
                        if (book.getSelfLink().equals(bookUrl)) {
                            currentlyEditedBook = book;
                            displayBook(book);
                            return;
                        }
                    }
                    Log.e(TAG, "onChanged: couldn't load the book from db. Url=" + bookUrl);
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void displayBook(Book book) {
        VolumeInfo volumeInfo = book.getVolumeInfo();
        String thumbUrl = volumeInfo.getThumbnailURL();
        PicassoHelper.loadThumbnail(thumbUrl, thumbIv);

        titleTv.setText(volumeInfo.getTitle());

        List<String> authors = volumeInfo.getAuthors();
        for (int i = 0; i < authors.size(); i++) {
            if (i == authors.size() - 1) {
                authorTv.append(authors.get(i));
            } else {
                authorTv.append(authors.get(i) + ", ");
            }
        }
        setupListeners();
    }

    private void setupListeners() {
        noteTextInput.setText(currentlyEditedBook.getPersonalNote());
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void saveNote() {
        String newNote = Objects.requireNonNull(noteTextInput.getText()).toString();
        currentlyEditedBook.setPersonalNote(newNote);
        savedBooksViewModel.update(currentlyEditedBook);
        onBackPressed();
    }

    private void initViews() {
        thumbIv = findViewById(R.id.note_thumbnail_iv);
        authorTv = findViewById(R.id.note_author_tv);
        titleTv = findViewById(R.id.note_title_tv);
        noteTextInput = findViewById(R.id.note_text_input);
        saveBtn = findViewById(R.id.save_note_btn);
        cancelBtn = findViewById(R.id.cancel_note_btn);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
