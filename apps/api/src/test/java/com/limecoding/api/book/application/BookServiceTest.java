package com.limecoding.api.book.application;

import com.limecoding.api.book.domain.Book;
import com.limecoding.api.book.domain.BookFixture;
import com.limecoding.api.book.domain.BookRepository;
import com.limecoding.api.book.domain.BookSource;
import com.limecoding.api.book.presentation.dto.BookDTO;
import com.limecoding.api.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Book bookById = bookRepository.findBookById(l).orElseThrow();

        // Then
        assertThat(bookById.getTitle()).isEqualTo("title");
        assertThat(bookById.getAuthor()).isEqualTo("author");
        assertThat(bookById.getIsbn()).isEqualTo("ISBN-12345678");
        assertThat(bookById.getSource()).isEqualTo(BookSource.MANUAL);
    }

    @Test
    @DisplayName("Find Book By Id successfully")
    void GivenRegisteredBook_WhenSearchBookById_ThenReturnBook() {
        // Given
        Book book = BookFixture.createBook("title", "author", "ISBN-12345678", BookSource.MANUAL);
        Book target = bookRepository.save(book);

        // When
        BookDTO bookById = bookService.getBookById(target.getId());

        // Then
        assertThat(bookById.getTitle()).isEqualTo(target.getTitle());
        assertThat(bookById.getAuthor()).isEqualTo(target.getAuthor());
        assertThat(bookById.getIsbn()).isEqualTo(target.getIsbn());
        assertThat(bookById.getSource()).isEqualTo(target.getSource());
    }

    @Test
    @DisplayName("Find Book By Id with non-existent book")
    void GivenNonExistentBook_WhenSearchBookById_ThenThrowException() {
        // When & Then
        assertThatThrownBy(() -> bookService.getBookById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book not found with id: " + 999L);
    }
}