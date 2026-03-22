package com.limecoding.api.book.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
public class Book {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "isbn", length = 13)
    private String isbn;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "author", nullable = false, length = 255)
    private String author;

    @Column(name = "publisher", length = 255)
    private String publisher;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private BookSource source;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Book(
            String isbn, String title, String author, String publisher,
            LocalDate publishedDate, String coverImageUrl, String description,
            BookSource source) {
        // Required
        updateTitle(title);
        updateAuthor(author);
        updateSource(source);
        updateISBN(isbn);

        // Optional
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public void updateTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if (title.length() > 255) {
            throw new IllegalArgumentException("Title cannot exceed 255 characters");
        }

        this.title = title;
    }

    public void updateAuthor(String author) {
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }

        if (author.length() > 255) {
            throw new IllegalArgumentException("Author cannot exceed 255 characters");
        }

        this.author = author;
    }

    public void updateSource(BookSource source) {
        if (source == null) {
            throw new IllegalArgumentException("Book source cannot be null");
        }

        this.source = source;
    }

    public void updateISBN(String isbn) {
        if (isbn.length() != 13) {
            throw new IllegalArgumentException("ISBN must be 13 characters");
        }

        this.isbn = isbn;
    }
}
