package com.romanoindustries.booksearch.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.romanoindustries.booksearch.BookViewActivity;
import com.romanoindustries.booksearch.R;
import com.romanoindustries.booksearch.adapters.BooksAdapter;
import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.viewmodels.SearchFragmentViewModel;

import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements BooksAdapter.OnBookListener {
    private static final String TAG = "SearchFragment";

    private RecyclerView booksRecyclerView;
    private LinearLayoutManager layoutManager;
    private BooksAdapter booksAdapter;
    private SearchFragmentViewModel searchFragmentViewModel;
    private TextView emptyTextView;
    private SearchView searchView;
    private ProgressBar progressBar;
    private ProgressBar loadMoreProgressBar;
    private ImageButton searchOptionsBtn;
    private PopupMenu popupMenu;

    private boolean sharedElementTransitionStarted = false;

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
        findViews(view);

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
                    Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenuStyle);
                    popupMenu = new PopupMenu(wrapper, v);
                    popupMenu.getMenuInflater().inflate(R.menu.search_popup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(searchMenuClickListener);
                }
                popupMenu.show();
            }
        });

        searchFragmentViewModel.getBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                checkConnection();
                if (books == null) {
                    return;
                }
                if (books.size() == 0) {
                    hideList();
                    return;
                }
                booksAdapter.updateBooks(books);
                showList();
            }
        });

        searchFragmentViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showProgressBar();
                    emptyTextView.setText(R.string.no_books_found); /*to show if no results*/
                } else {
                    hideProgressBar();
                }
            }
        });

        searchFragmentViewModel.getIsLoadingMore().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoadingMore) {
                if (isLoadingMore) {
                    loadMoreProgressBar.setVisibility(View.VISIBLE);
                } else {
                    loadMoreProgressBar.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private void findViews(View view) {
        booksRecyclerView = view.findViewById(R.id.books_list);
        emptyTextView = view.findViewById(R.id.empty_text_view);
        searchView = view.findViewById(R.id.search_books_sv);
        progressBar = view.findViewById(R.id.progress_bar);
        searchOptionsBtn = view.findViewById(R.id.search_menu_ib);
        loadMoreProgressBar = view.findViewById(R.id.loading_more_pb);
    }

    private void initRecyclerView() {
        booksAdapter = new BooksAdapter(searchFragmentViewModel.getBooks().getValue(), this);
        layoutManager = new LinearLayoutManager(getContext());
        booksRecyclerView.setAdapter(booksAdapter);
        booksRecyclerView.setLayoutManager(layoutManager);

        booksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if ((searchFragmentViewModel.getBooks().getValue() != null) &&
                        (searchFragmentViewModel.getBooks().getValue().size() - 1) == layoutManager.findLastCompletelyVisibleItemPosition()) {
                    Log.d(TAG, "onScrolled: scrolled to the bottom");
                    searchFragmentViewModel.loadMoreBooks();
                }
            }
        });

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
        if (sharedElementTransitionStarted) {
            return;
        }
        sharedElementTransitionStarted = true;
        Intent viewBookIntent = new Intent(getContext(), BookViewActivity.class);
        viewBookIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String bookUrl = Objects.requireNonNull(searchFragmentViewModel.getBooks().getValue()).get(position).getSelfLink();
        viewBookIntent.putExtra(Intent.EXTRA_CONTENT_QUERY, bookUrl);
        viewBookIntent.putExtra(Intent.EXTRA_FROM_STORAGE, false);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()),
                imageForTransition, imageForTransition.getTransitionName());

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

    private void checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            Snackbar snackbar =  Snackbar
                    .make(getActivity().findViewById(android.R.id.content),
                            R.string.no_internet_snack,
                            Snackbar.LENGTH_LONG);

            View snackView = snackbar.getView();
            TextView tv = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedElementTransitionStarted = false;
    }
}
