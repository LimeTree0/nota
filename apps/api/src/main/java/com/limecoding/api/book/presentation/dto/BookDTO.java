package com.limecoding.api.book.presentation.dto;

import com.limecoding.api.book.domain.Book;
import com.limecoding.api.book.domain.BookSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class BookDTO {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishedDate;
    private String coverImageUrl;
    private String description;
    private BookSource source;
    private LocalDateTime createdAt;

    public static BookDTO from(Book book) {
        return BookDTO.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .coverImageUrl(book.getCoverImageUrl())
                .description(book.getDescription())
                .source(book.getSource())
                .createdAt(book.getCreatedAt())
                .build();
    }
}
