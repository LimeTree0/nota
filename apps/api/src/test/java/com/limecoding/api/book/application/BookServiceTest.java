package com.limecoding.api.book.application;

import com.limecoding.api.book.domain.Book;
import com.limecoding.api.book.domain.BookFixture;
import com.limecoding.api.book.domain.BookSource;
import com.limecoding.api.book.domain.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Register book successfully")
    void registerBookSuccessfully() {
        // Given
        Book book = BookFixture.createBook("title", "author", "ISBN-12345678", BookSource.MANUAL);

        // When
        Long l = bookService.registerBook(book);
        Book bookById = bookRepository.findBookById(l);

        // Then
        assertThat(bookById.getTitle()).isEqualTo("title");
        assertThat(bookById.getAuthor()).isEqualTo("author");
        assertThat(bookById.getIsbn()).isEqualTo("ISBN-12345678");
        assertThat(bookById.getSource()).isEqualTo(BookSource.MANUAL);
    }
}