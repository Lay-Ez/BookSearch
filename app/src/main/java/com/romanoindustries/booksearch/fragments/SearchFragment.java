package com.romanoindustries.booksearch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
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
    private ImageButton searchOptionsBtn;
    private PopupMenu popupMenu;

    public static final int SEARCH_EVERYWHERE = 0;
    public static final int SEARCH_BY_TITLE = 1;
    public static final int SEARCH_BY_AUTHOR = 2;
    public static final int SEARCH_BY_PUBLISHER = 3;
    public static final int SEARCH_BY_SUBJECT = 4;

    private int currentSearchMode = 0;

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
                searchFragmentViewModel.loadBooks(query, currentSearchMode);
                searchView.clearFocus();
                booksRecyclerView.scrollToPosition(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchOptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupMenu == null) {
                    popupMenu = new PopupMenu(getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.search_popup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(searchMenuClickListener);
                }
                popupMenu.show();
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
        searchOptionsBtn = view.findViewById(R.id.search_menu_ib);
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
    public void onBookClick(int position, ImageView imageForTransition) {
        Intent viewBookIntent = new Intent(getContext(), BookViewActivity.class);
        String bookUrl = searchFragmentViewModel.getBooks().getValue().get(position).getSelfLink();
        viewBookIntent.putExtra(Intent.EXTRA_CONTENT_QUERY, bookUrl);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                imageForTransition, ViewCompat.getTransitionName(imageForTransition));

        /* Code for adding book to the saved list*/
//        Book book = searchFragmentViewModel.getBooks().getValue().get(position);
//        SavedBooksViewModel savedBooksViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(SavedBooksViewModel.class);
//        savedBooksViewModel.insert(book);

        startActivity(viewBookIntent, options.toBundle());
    }

    private PopupMenu.OnMenuItemClickListener searchMenuClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.isChecked()) {
                return true;
            }

            item.setChecked(!item.isChecked());

            switch (item.getItemId()) {
                case R.id.search_everywhere:
                    currentSearchMode = SEARCH_EVERYWHERE;
                    break;
                case R.id.search_by_title:
                    currentSearchMode = SEARCH_BY_TITLE;
                    break;
                case R.id.search_by_author:
                    currentSearchMode = SEARCH_BY_AUTHOR;
                    break;
                case R.id.search_by_publisher:
                    currentSearchMode = SEARCH_BY_PUBLISHER;
                    break;
                case R.id.search_by_subject:
                    currentSearchMode = SEARCH_BY_SUBJECT;
                    break;

                    default:
                        //
            }
            return true;
        }
    };
}
