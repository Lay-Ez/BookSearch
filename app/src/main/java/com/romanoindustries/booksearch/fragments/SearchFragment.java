package com.romanoindustries.booksearch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private SearchView searchView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);

        searchFragmentViewModel = new ViewModelProvider(this).get(SearchFragmentViewModel.class);
        searchFragmentViewModel.init();
        initRecyclerView();

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFragmentViewModel.loadBooks(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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
        searchView = view.findViewById(R.id.search_books_sv);
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