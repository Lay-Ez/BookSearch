package com.romanoindustries.booksearch.roomstuff;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RoomConverters {

    @TypeConverter
    public static String authorsToString(List<String> authorsList) {
        return new Gson().toJson(authorsList);
    }

    @TypeConverter
    public static List<String> stringToAuthors(String json) {
        Type type = new TypeToken<List<String>>() {}.getClass();
        return new Gson().fromJson(json, type);
    }
}
