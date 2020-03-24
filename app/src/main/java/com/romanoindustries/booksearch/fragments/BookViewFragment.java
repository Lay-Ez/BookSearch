package com.romanoindustries.booksearch.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.romanoindustries.booksearch.EditNoteActivity;
import com.romanoindustries.booksearch.R;
import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.bookmodel.VolumeInfo;
import com.romanoindustries.booksearch.imagetransformation.PicassoHelper;
import com.romanoindustries.booksearch.viewmodels.BookViewActivityViewModel;
import com.romanoindustries.booksearch.viewmodels.SavedBooksViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import at.blogc.android.views.ExpandableTextView;


public class BookViewFragment extends Fragment {
    private static final String TAG = "BookViewFragment";

    private Book currentlyViewedBook;
    private boolean deleteBookOnExit = false;

    private TextView titleTV;
    private ImageView thumbnailIM;
    private TextView authorTV;
    private TextView publisherTV;
    private TextView ratingTv;
    private TextView numReviewsTv;
    private TextView numPagesTv;
    private TextView reviewsLabelTv;
    private ExpandableTextView descriptionExpendable;
    private TextView showMoreTv;
    private Button saveButtonTv;
    private Button previewButtonTv;
    private TextView categoriesTv;
    private Button googlePlayBtn;

    private TextView notesTv;
    private ImageButton editNoteBtn;

    private boolean startedFromSavedFragment;

    public BookViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_view, container, false);
        startedFromSavedFragment = Objects.requireNonNull(getActivity()).getIntent().getBooleanExtra(Intent.EXTRA_FROM_STORAGE, false);

        initViews(view);
        BookViewActivityViewModel viewModel = new ViewModelProvider(requireActivity()).get(BookViewActivityViewModel.class);
        viewModel.getBook().observe(getViewLifecycleOwner(), new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                if (book != null) {
                    displayBook(book);
                    currentlyViewedBook = book;
                    if (startedFromSavedFragment) {
                        addRemoveOption();
                    } else {
                        checkIfBookSaved(book);
                    }
                }
            }
        });

        return view;
    }

    private void displayBook(final Book book) {
        VolumeInfo volumeInfo = book.getVolumeInfo();

        PicassoHelper.loadThumbnail(volumeInfo.getThumbnailURL(), thumbnailIM);
        titleTV.setText(volumeInfo.getTitle());

        List<String> authors = volumeInfo.getAuthors();
        authorTV.setText(null);
        for (int i = 0; i < authors.size(); i++) {
            if (i == authors.size() - 1) {
                authorTV.append(authors.get(i));
            } else {
                authorTV.append(authors.get(i) + ", ");
            }
        }

        List<String> categories = volumeInfo.getCategories();
        categoriesTv.setText(null);
        for (int i = 0; i < categories.size(); i++) {
            if (i == categories.size() - 1) {
                categoriesTv.append(categories.get(i));
            } else {
                categoriesTv.append(categories.get(i) + "\n");
            }
        }

        publisherTV.setText(volumeInfo.getPublisher());
        ratingTv.setText(String.valueOf(volumeInfo.getAvgRating()));
        numPagesTv.setText(String.valueOf(volumeInfo.getPageCount()));

        int countReviews = volumeInfo.getRatingsCount();
        numReviewsTv.setText(String.valueOf(countReviews));
        if (countReviews == 1) {
            reviewsLabelTv.setText(getString(R.string.review));
        } else {
            reviewsLabelTv.setText(getString(R.string.reviews));
        }

        previewButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String previewUrl = book.getWebReaderLink();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(previewUrl));
                startActivity(browserIntent);
            }
        });

        saveButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook(book);
            }
        });

        googlePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String previewUrl = book.getVolumeInfo().getInfoUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(previewUrl));
                startActivity(browserIntent);
            }
        });

        descriptionExpendable.setText(Html.fromHtml(volumeInfo.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        displayOptionalSaved(book);

        Log.d(TAG, "displayBook: ISBN10=" + volumeInfo.getIsbn10());
        Log.d(TAG, "displayBook: ISBN13=" + volumeInfo.getIsbn13());
        Log.d(TAG, "displayBook: webReaderLink=" + book.getWebReaderLink());
    }

    private void displayOptionalSaved(Book book) {
        if (!startedFromSavedFragment) {
            return;
        }
        String note = book.getPersonalNote();
        notesTv.setText(note);

        editNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditNoteActivity.class);
                String selfUrl = currentlyViewedBook.getSelfLink();
                intent.putExtra(Intent.EXTRA_CONTENT_QUERY, selfUrl);
                startActivity(intent);
            }
        });
    }

    private void initViews(View view) {
        titleTV = view.findViewById(R.id.title_tv);
        thumbnailIM = view.findViewById(R.id.thumbnail_iv);
        authorTV = view.findViewById(R.id.author_tv);
        publisherTV = view.findViewById(R.id.publisher_tv);
        ratingTv = view.findViewById(R.id.rating_tv);
        numReviewsTv = view.findViewById(R.id.num_reviews_tv);
        numPagesTv = view.findViewById(R.id.num_pages_tv);
        reviewsLabelTv = view.findViewById(R.id.reviews_label_tv);
        showMoreTv = view.findViewById(R.id.showmore_btn);
        categoriesTv = view.findViewById(R.id.categories_tv);

        descriptionExpendable = view.findViewById(R.id.expendable_text_view);
        descriptionExpendable.setInterpolator(new AccelerateDecelerateInterpolator());

        View.OnClickListener clickListenerForExpandableSummary = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreTv.setText(descriptionExpendable.isExpanded() ? getString(R.string.show_more) : getString(R.string.show_less));
                descriptionExpendable.toggle();
            }
        };

        showMoreTv.setOnClickListener(clickListenerForExpandableSummary);
        descriptionExpendable.setOnClickListener(clickListenerForExpandableSummary);

        previewButtonTv = view.findViewById(R.id.preview_btn);
        saveButtonTv = view.findViewById(R.id.save_book_button);

        googlePlayBtn = view.findViewById(R.id.google_play_btn);

        initOptionalViews(view);
    }

    private void checkIfBookSaved(final Book book) {
        SavedBooksViewModel savedBooksViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())
                .create(SavedBooksViewModel.class);
        savedBooksViewModel.getSavedBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (books != null && !books.isEmpty()) {
                    String currentlyViewedBookUrl = book.getSelfLink();
                    for (Book book: books) {
                        if (book.getSelfLink().equals(currentlyViewedBookUrl)) {
                            // change the UI slightly if book isn't viewed from saved list but is IN the saved list
                            saveButtonTv.setText(R.string.saved);
                            saveButtonTv.setEnabled(false);
                        }
                    }
                }
            }
        });
    }

    private void initOptionalViews(View view) {
        Log.d(TAG, "initOptionalViews: startedFromSaved=" + startedFromSavedFragment);
        //hide these if book isn't viewed from saved list
        TextView noteLabel = view.findViewById(R.id.note_label);
        notesTv = view.findViewById(R.id.notes_tv);
        editNoteBtn = view.findViewById(R.id.edit_note_btn);

        if (!startedFromSavedFragment) {
            noteLabel.setVisibility(View.GONE);
            notesTv.setVisibility(View.GONE);
            editNoteBtn.setVisibility(View.GONE);
        }
    }

    private void addRemoveOption() {
        saveButtonTv.setText(R.string.remove);
        saveButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonTv.setText(R.string.removed);
                saveButtonTv.setEnabled(false);
                deleteBookOnExit = true;
            }
        });
    }

    private void saveBook(Book book) {
        if (book == null) {
            Log.d(TAG, "saveBook: attempting tos");
            return;
        }
        SavedBooksViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())
                .create(SavedBooksViewModel.class);

        long currentTime = System.currentTimeMillis();
        book.setSavedTime(currentTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = dateFormat.format(new Date(book.getSavedTime()));
        book.setPersonalNote(getString(R.string.note_stub, dateString));
        viewModel.insert(currentlyViewedBook);
        saveButtonTv.setText(R.string.saved);
        saveButtonTv.setEnabled(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (deleteBookOnExit) {
            SavedBooksViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())
                    .create(SavedBooksViewModel.class);
            viewModel.delete(currentlyViewedBook);
        }
    }
}
