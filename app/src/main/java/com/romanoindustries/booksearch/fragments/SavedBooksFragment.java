package com.romanoindustries.booksearch.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import java.util.List;

public class SavedBooksFragment extends Fragment implements BooksAdapter.OnBookListener {
    private static final String TAG = "SavedBooksFragment";

    private SavedBooksViewModel booksViewModel;
    private RecyclerView savedBooksRecyclerView;
    private BooksAdapter booksAdapter;

    public SavedBooksFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_books, container, false);

        booksViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getActivity().getApplication())
                .create(SavedBooksViewModel.class);

        savedBooksRecyclerView = view.findViewById(R.id.saved_books_list);
        booksAdapter = new BooksAdapter(new ArrayList<Book>(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        savedBooksRecyclerView.setLayoutManager(layoutManager);
        savedBooksRecyclerView.setAdapter(booksAdapter);

        booksViewModel.getSavedBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                booksAdapter.updateBooks(books);
            }
        });

        return view;
    }

    @Override
    public void onBookClick(int position, ImageView imageForTransition) {
        Intent viewBookIntent = new Intent(getContext(), BookViewActivity.class);
        String bookUrl = booksViewModel.getSavedBooks().getValue().get(position).getSelfLink();
        viewBookIntent.putExtra(Intent.EXTRA_CONTENT_QUERY, bookUrl);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                imageForTransition, ViewCompat.getTransitionName(imageForTransition));

        startActivity(viewBookIntent, options.toBundle());
    }
}
