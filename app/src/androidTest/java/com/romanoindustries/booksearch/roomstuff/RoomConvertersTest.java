package com.romanoindustries.booksearch.roomstuff;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RoomConvertersTest {

    @Test
    public void authorsToString() {
        List<String> authors = new ArrayList<>();
        authors.add("Jamie Oliver");
        authors.add("Stacie Morgan");
        authors.add("Big'D");
        authors.add("Hamal Jackson");

        String result = RoomConverters.authorsToString(authors);

        assertEquals("Jamie Oliver,Stacie Morgan,Big'D,Hamal Jackson", result);
    }

    @Test
    public void stringToAuthors() {
        String input = "Jamie Oliver,Stacie Morgan,Big'D,Hamal Jackson";

        List<String> exected = new ArrayList<>();
        exected.add("Jamie Oliver");
        exected.add("Stacie Morgan");
        exected.add("Big'D");
        exected.add("Hamal Jackson");

        List<String> actual = RoomConverters.stringToAuthors(input);
        assertEquals(exected, actual);

    }
}