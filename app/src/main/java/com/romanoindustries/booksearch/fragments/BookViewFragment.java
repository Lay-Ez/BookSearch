package com.romanoindustries.booksearch.fragments;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;


public class BookViewFragment extends Fragment {
    private static final String TAG = "BookViewFragment";
    private BookViewActivityViewModel viewModel;

    private TextView titleTV;
    private ImageView thumbnailIM;
    private TextView authorTV;
    private TextView publisherTV;
    private TextView ratingTv;
    private TextView numReviewsTv;
    private TextView numPagesTv;
    private TextView reviewsLabelTv;
    private Book currentlyViewedBook;
    private TextView descriptionTv;

    public BookViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_view, container, false);
        initViews(view);

        viewModel = new ViewModelProvider(requireActivity()).get(BookViewActivityViewModel.class);
        viewModel.getBook().observe(getViewLifecycleOwner(), new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                if (book == null) {
                    //
                } else {
                    displayBook(book);
                    currentlyViewedBook = book;
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

        descriptionTv.setText(Html.fromHtml(volumeInfo.getDescription(), Html.FROM_HTML_MODE_LEGACY));
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
        descriptionTv = view.findViewById(R.id.description_tv);
        descriptionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleTextViewExpansion((TextView) v);
            }
        });
    }

    private void cycleTextViewExpansion(TextView tv){
        int collapsedMaxLines = 5;
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines",
                tv.getMaxLines() == collapsedMaxLines? tv.getLineCount() : collapsedMaxLines);
        animation.setDuration(200).start();
    }
}
