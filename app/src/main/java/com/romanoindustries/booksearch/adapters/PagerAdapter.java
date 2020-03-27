package com.romanoindustries.booksearch.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.romanoindustries.booksearch.fragments.SavedBooksFragment;
import com.romanoindustries.booksearch.fragments.SearchFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(@NonNull FragmentManager fm, int behaviour) {
        super(fm, behaviour);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new SearchFragment();
        } else {
            return new SavedBooksFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Search";
        } else {
            return "Added";
        }
    }
}
