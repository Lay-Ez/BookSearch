package com.romanoindustries.booksearch.networkutils;

import android.net.Uri;
import android.util.Log;

import com.romanoindustries.booksearch.bookmodel.Book;
import com.romanoindustries.booksearch.bookmodel.VolumeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookNetworkUtils {
    private static final String TAG = "BookNetworkUtils";

    private static final String BASE_GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String STANDARD_QUERY_PARAMETER = "q";
    private static final String MAX_RESULTS_QUERY_PARAMETER = "maxResults";

    public static final int MAX_RESULTS_DEFAULT = 40;

    public static URL composeURL(String query, int maxResults) {
        Uri builtUri = Uri.parse(BASE_GOOGLE_BOOKS_API_URL)
                .buildUpon()
                .appendQueryParameter(STANDARD_QUERY_PARAMETER, query)
                .appendQueryParameter(MAX_RESULTS_QUERY_PARAMETER, String.valueOf(maxResults))
                .build();

        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "composeURL: composed URI=" + builtUri.toString());
            return null;
        }
    }

    public static URL composeURL(String query) {
       return composeURL(query, MAX_RESULTS_DEFAULT);
    }

    public static String getResponseFromUrl(URL url) {
        String result = "";

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() != 200) {
                return result;
            }

            Scanner s = new Scanner(connection.getInputStream()).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static List<Book> parseBooksFromJson(String jsonString) {

        ArrayList<Book> parsedBooks = new ArrayList<>();

        try {
            JSONObject jsonBooks = new JSONObject(jsonString);
            JSONArray jsonBooksArray = jsonBooks.getJSONArray("items");
            for (int i = 0; i < jsonBooksArray.length(); i++) {
                JSONObject jsonBookObject = jsonBooksArray.getJSONObject(i);
                Book parsedBook = parseJsonBook(jsonBookObject);
                parsedBooks.add(parsedBook);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedBooks;
    }

    private static Book parseJsonBook(JSONObject jsonBook) {

        VolumeInfo volumeInfo = new VolumeInfo();

        try {
            JSONObject volumeInfoJson = jsonBook.getJSONObject("volumeInfo");
            volumeInfo.setTitle(volumeInfoJson.optString("title", "No Title"));

            JSONArray authorsArray = volumeInfoJson.optJSONArray("authors");
            if (authorsArray != null) {
                for (int i = 0; i < authorsArray.length(); i++) {
                    String author = authorsArray.getString(i);
                    volumeInfo.addAuthorToList(author);
                }
            } else {
                volumeInfo.addAuthorToList("No Author");
            }

            volumeInfo.setPublisher(volumeInfoJson.optString("publisher", "No Publisher"));
            volumeInfo.setPublishedDate(volumeInfoJson.optString("publishedDate", "No Published Date"));
            volumeInfo.setDescription(volumeInfoJson.optString("description", "No Description"));
            volumeInfo.setPageCount(volumeInfoJson.optInt("pageCount", 0));

            JSONArray categoriesArray = volumeInfoJson.getJSONArray("categories");
            for (int i = 0; i < categoriesArray.length(); i++) {
                String category = categoriesArray.getString(i);
                volumeInfo.addCategory(category);
            }

            volumeInfo.setAvgRating(volumeInfoJson.optDouble("averageRating", 0));
            volumeInfo.setRatingsCount(volumeInfoJson.optInt("ratingsCount", 0));

            JSONObject imageLinks = volumeInfoJson.getJSONObject("imageLinks");
            volumeInfo.setSmallThumbnailURL(imageLinks.optString("smallThumbnail", ""));
            volumeInfo.setThumbnailURL(imageLinks.optString("thumbnail", ""));

            volumeInfo.setLanguage(volumeInfoJson.optString("language", "UNKNOWN"));
            volumeInfo.setPreviewLink(volumeInfoJson.optString("previewLink", ""));
            volumeInfo.setInfoLink(volumeInfoJson.optString("infoLink", ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Book(volumeInfo);
    }
}