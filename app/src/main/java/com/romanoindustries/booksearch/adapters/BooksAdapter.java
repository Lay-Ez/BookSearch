package com.romanoindustries.booksearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.romanoindustries.booksearch.R;
import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.bookmodel.VolumeInfo;
import com.romanoindustries.booksearch.imagetransformation.PicassoHelper;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {
    private static final String TAG = "BooksAdapter";

    private List<Book> books;
    private OnBookListener onBookListener;

    public BooksAdapter(List<Book> books, OnBookListener onBookListener) {
        this.books = books;
        this.onBookListener = onBookListener;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void updateBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int listItemResourceId = R.layout.book_list_item;

        View view = inflater.inflate(listItemResourceId, parent, false);
        return new BookViewHolder(view, onBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book bookToBind = books.get(position);
        holder.bind(bookToBind);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_bookThumb;
        private TextView tv_bookTitle;
        private TextView tv_authors;
        private RatingBar rb_rating;
        private TextView tv_numRatings;
        private OnBookListener onBookListener;

        public BookViewHolder(@NonNull final View itemView, final OnBookListener onBookListener) {
            super(itemView);
            iv_bookThumb = itemView.findViewById(R.id.iv_book_thumb);
            tv_bookTitle = itemView.findViewById(R.id.tv_book_title);
            tv_authors = itemView.findViewById(R.id.tv_authors);
            rb_rating = itemView.findViewById(R.id.rb_rating);
            tv_numRatings = itemView.findViewById(R.id.tv_num_ratings);
            this.onBookListener = onBookListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBookListener.onBookClick(getAdapterPosition(), iv_bookThumb);
                }
            });
        }

        public void bind(Book book) {
            VolumeInfo volumeInfo = book.getVolumeInfo();

            String imgUrl = volumeInfo.getThumbnailURL();
            PicassoHelper.loadThumbnail(imgUrl, iv_bookThumb);

            tv_bookTitle.setText(volumeInfo.getTitle());
            tv_authors.setText(volumeInfo.getAuthors().get(0));
            rb_rating.setRating((float) volumeInfo.getAvgRating());
            tv_numRatings.setText("(" + String.format("%d", volumeInfo.getRatingsCount()) + ")");
        }
    }

    public interface OnBookListener{
        void onBookClick(int position, ImageView imageForTransition);
    }
}
