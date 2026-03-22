package com.limecoding.api.book.domain;

import java.time.LocalDate;

public class BookFixture {
    public static Book createBook(String title, String author, String isbn, BookSource source) {
        return new Book(
                isbn,
                title,
                author,
                "Book Publisher",
                LocalDate.now(),
                "https://example.com/cover.jpg",
                "Book Description",
                source
        );
    }
}
