package com.romanoindustries.booksearch.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.romanoindustries.booksearch.R;
import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.viewmodels.SavedBooksViewModel;

import java.util.List;

public class SavedBooksFragment extends Fragment {

    private SavedBooksViewModel booksViewModel;

    public SavedBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        booksViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getActivity().getApplication())
                .create(SavedBooksViewModel.class);
        booksViewModel.getSavedBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                Toast.makeText(getContext(), "onCreate from fragment", Toast.LENGTH_SHORT).show();
            }
        });

        return inflater.inflate(R.layout.fragment_saved_books, container, false);
    }

}
