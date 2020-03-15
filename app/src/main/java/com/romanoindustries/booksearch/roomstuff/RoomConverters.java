package com.romanoindustries.booksearch.roomstuff;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class RoomConverters {

    @TypeConverter
    public static String authorsToString(List<String> authorsList) {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < authorsList.size(); i++) {
            if (i == authorsList.size() - 1) {
                sb.append(authorsList.get(i));
            } else {
                sb.append(authorsList.get(i)).append(",");
            }
        }

        return sb.toString();
    }

    @TypeConverter
    public static List<String> stringToAuthors(String authors) {
        String[] splitted = authors.split(",");
        return Arrays.asList(splitted);
    }
}
