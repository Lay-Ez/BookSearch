package com.romanoindustries.booksearch.fragments;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.romanoindustries.booksearch.R;
import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.bookmodel.VolumeInfo;
import com.romanoindustries.booksearch.imagetransformation.RoundedCornersTransformation;
import com.romanoindustries.booksearch.networkutils.BookNetworkUtils;
import com.romanoindustries.booksearch.viewmodels.BookViewActivityViewModel;
import com.romanoindustries.booksearch.viewmodels.SavedBooksViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import at.blogc.android.views.ExpandableTextView;


public class BookViewFragment extends Fragment {
    private static final String TAG = "BookViewFragment";
    
    private BookViewActivityViewModel viewModel;
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
    private TextView previewButtonTv;
    private TextView saveButtonTv;
    private TextView categoriesTv;

    public BookViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_view, container, false);
        initViews(view);

        final boolean startedFromSavedFragment = getActivity().getIntent().getBooleanExtra(Intent.EXTRA_FROM_STORAGE, false);

        viewModel = new ViewModelProvider(requireActivity()).get(BookViewActivityViewModel.class);
        viewModel.getBook().observe(getViewLifecycleOwner(), new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                if (book == null) {
                    //
                } else {
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

    private void displayBook(Book book) {
        VolumeInfo volumeInfo = book.getVolumeInfo();

        loadThumbnail(volumeInfo.getThumbnailURL());
        titleTV.setText(volumeInfo.getTitle());

        List<String> authors = volumeInfo.getAuthors();
        for (int i = 0; i < authors.size(); i++) {
            if (i == authors.size() - 1) {
                authorTV.append(authors.get(i));
            } else {
                authorTV.append(authors.get(i) + ", ");
            }
        }

        List<String> categories = volumeInfo.getCategories();
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

        descriptionExpendable.setText(Html.fromHtml(volumeInfo.getDescription(), Html.FROM_HTML_MODE_LEGACY));
    }

    private void loadThumbnail(String url) {
        int radius = 5;
        int margin = 5;
        Transformation transformation = new RoundedCornersTransformation(radius, margin);
        if (url == null || url.equals("")) {
            Picasso.get().load(BookNetworkUtils.NO_IMAGE_AVAILABLE_URL).transform(transformation).into(thumbnailIM);
        } else {
            Picasso.get().load(url).transform(transformation).into(thumbnailIM);
        }
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
        showMoreTv = view.findViewById(R.id.showmore_tv);
        categoriesTv = view.findViewById(R.id.categories_tv);

        descriptionExpendable = view.findViewById(R.id.expendable_text_view);
        descriptionExpendable.setInterpolator(new AccelerateDecelerateInterpolator());

        View.OnClickListener clickListenerForExpandableSummary = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateViewFade(showMoreTv);
                showMoreTv.setText(descriptionExpendable.isExpanded() ? getString(R.string.show_more) : getString(R.string.show_less));
                descriptionExpendable.toggle();
            }
        };

        showMoreTv.setOnClickListener(clickListenerForExpandableSummary);
        descriptionExpendable.setOnClickListener(clickListenerForExpandableSummary);

        previewButtonTv = view.findViewById(R.id.preview_tv);
        saveButtonTv = view.findViewById(R.id.save_book_tv);

        previewButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateViewFade(v);
                String previewUrl = currentlyViewedBook.getVolumeInfo().getPreviewUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(previewUrl));
                startActivity(browserIntent);
            }
        });

        saveButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateViewFade(v);
                SavedBooksViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
                        .create(SavedBooksViewModel.class);
                viewModel.insert(currentlyViewedBook);
                saveButtonTv.setText(R.string.saved);
            }
        });

    }

    private void animateViewFade(final View view) {
        int colorFrom = getContext().getColor(R.color.color_buttons);
        int colorTo = getContext().getColor(R.color.color_when_button_pressed);
        final ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animator.setDuration(100);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                view.setBackgroundTintList(ColorStateList.valueOf(animatedValue));
            }
        });
        animator.start();
    }

    private void checkIfBookSaved(final Book book) {
        SavedBooksViewModel savedBooksViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
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
                        }
                    }
                }
            }
        });
    }

    private void addRemoveOption() {
        saveButtonTv.setText(R.string.remove);
        saveButtonTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateViewFade(v);
                saveButtonTv.setText(R.string.removed);
                deleteBookOnExit = true;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (deleteBookOnExit) {
            SavedBooksViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
                    .create(SavedBooksViewModel.class);
            viewModel.delete(currentlyViewedBook);
        }
    }
}
