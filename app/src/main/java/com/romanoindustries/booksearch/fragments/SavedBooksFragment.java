package com.romanoindustries.booksearch.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.romanoindustries.booksearch.viewmodels.SavedBooksViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SavedBooksFragment extends Fragment implements BooksAdapter.OnBookListener {
    private static final String TAG = "SavedBooksFragment";

    private SavedBooksViewModel savedBooksViewModel;
    private RecyclerView savedBooksRecyclerView;
    private BooksAdapter booksAdapter;
    private TextView emptyListTv;

    public SavedBooksFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_books, container, false);

        savedBooksViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getActivity().getApplication())
                .create(SavedBooksViewModel.class);
        emptyListTv = view.findViewById(R.id.empty_saved_tv);
        savedBooksRecyclerView = view.findViewById(R.id.saved_books_list);
        booksAdapter = new BooksAdapter(new ArrayList<Book>(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        savedBooksRecyclerView.setLayoutManager(layoutManager);
        savedBooksRecyclerView.setAdapter(booksAdapter);

        savedBooksViewModel.getSavedBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                if (books != null) {
                    if (books.isEmpty()) {
                        displayEmptyList();
                    } else {
                        books.sort(new Comparator<Book>() {
                            @Override
                            public int compare(Book book1, Book book2) {
                                return  (book1.getSavedTime() - book2.getSavedTime() > 0) ? -1 : 1;
                            }
                        });
                        booksAdapter.updateBooks(books);
                        displayNonEmptyList();
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onBookClick(int position, ImageView imageForTransition) {
        Intent viewBookIntent = new Intent(getContext(), BookViewActivity.class);
        String bookUrl = savedBooksViewModel.getSavedBooks().getValue().get(position).getSelfLink();
        viewBookIntent.putExtra(Intent.EXTRA_CONTENT_QUERY, bookUrl);
        viewBookIntent.putExtra(Intent.EXTRA_FROM_STORAGE, true);
        viewBookIntent.putExtra(Intent.EXTRA_INDEX, position);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                imageForTransition, ViewCompat.getTransitionName(imageForTransition));

        startActivity(viewBookIntent, options.toBundle());
    }

    private void displayEmptyList() {
        savedBooksRecyclerView.setVisibility(View.GONE);
        emptyListTv.setVisibility(View.VISIBLE);
    }

    private void displayNonEmptyList() {
        emptyListTv.setVisibility(View.GONE);
        savedBooksRecyclerView.setVisibility(View.VISIBLE);
    }
}
