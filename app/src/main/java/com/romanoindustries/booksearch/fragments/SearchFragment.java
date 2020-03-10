package com.romanoindustries.booksearch.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.romanoindustries.booksearch.BookViewActivity;
import com.romanoindustries.booksearch.R;
import com.romanoindustries.booksearch.adapters.BooksAdapter;
import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.viewmodels.SearchFragmentViewModel;

import java.util.Comparator;
import java.util.List;

public class SearchFragment extends Fragment implements BooksAdapter.OnBookListener {
    private static final String TAG = "SearchFragment";

    private RecyclerView booksRecyclerView;
    private BooksAdapter booksAdapter;
    private SearchFragmentViewModel searchFragmentViewModel;
    private TextView emptyTextView;
    private TextInputEditText searchInputText;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);


        searchInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        searchFragmentViewModel = new ViewModelProvider(this).get(SearchFragmentViewModel.class);
        searchFragmentViewModel.init();
        initRecyclerView();

        searchFragmentViewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (books == null || books.size() == 0) {
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

        searchFragmentViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showProgressBar();
                } else {
                    hideProgressBar();
                }
            }
        });
        return view;
    }

    private void initViews(View view) {
        booksRecyclerView = view.findViewById(R.id.books_list);
        emptyTextView = view.findViewById(R.id.empty_text_view);
        searchInputText = view.findViewById(R.id.edit_text_search);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void initRecyclerView() {
        booksAdapter = new BooksAdapter(searchFragmentViewModel.getBooks().getValue(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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

    private void showProgressBar() {
        emptyTextView.setVisibility(View.GONE);
        booksRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void performSearch() {
        String query = searchInputText.getText().toString();
        searchFragmentViewModel.loadBooks(query);
        searchInputText.clearFocus();
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchInputText.getWindowToken(), 0);
    }

    @Override
    public void onBookClick(int position) {
        Intent viewBookIntent = new Intent(getContext(), BookViewActivity.class);
        String bookUrl = searchFragmentViewModel.getBooks().getValue().get(position).getSelfLink();
        Log.d(TAG, "onBookClick: clicked the book at pos " + position);
        Log.d(TAG, "onBookClick: bookurl = " + bookUrl);
        viewBookIntent.putExtra(Intent.EXTRA_CONTENT_QUERY, bookUrl);
        startActivity(viewBookIntent);
    }
}
