package com.romanoindustries.booksearch.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.romanoindustries.booksearch.R;
import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.viewmodels.BookViewActivityViewModel;


public class BookViewFragment extends Fragment {
    private static final String TAG = "BookViewFragment";
    private BookViewActivityViewModel viewModel;

    private TextView title_tv;

    public BookViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_view, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(BookViewActivityViewModel.class);
        viewModel.getBook().observe(getViewLifecycleOwner(), new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                if (book == null) {
                    Log.d(TAG, "onChanged: book is now null");
                } else {
                    Log.d(TAG, "onChanged: book is now " + book.getVolumeInfo().getTitle());
                }
            }
        });

        return view;
    }

}
