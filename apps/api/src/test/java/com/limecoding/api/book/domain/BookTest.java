package com.limecoding.api.book.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookTest {

    @Nested
    class TitleFieldTest {
        @Test
        @DisplayName("Throw IllegalArgumentException when title is null")
        void GivenBookInformationWithNullTitle_WhenCreateBook_ThenThrowIllegalArgumentException() {
            // Given & When & Then
            assertThatThrownBy(() -> BookFixture.createBook(null, "author", "isbn", BookSource.MANUAL))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Title cannot be empty");
        }

        @Test
        @DisplayName("Throw IllegalArgumentException when title exceeds maximum length(255)")
        void GivenBookInformationWithTitleExceedMaximumLength_WhenCreateBook_ThenThrowIllegalArgumentException() {
            // Given & When & Then
            assertThatThrownBy(() -> BookFixture.createBook("t".repeat(256), "author", "isbn", BookSource.MANUAL))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Title cannot exceed 255 characters");
        }
    }

    @Nested
    class AuthorFieldTest {
        @Test
        @DisplayName("Throw IllegalArgumentException when author is null")
        void GivenBookInformationWithNullAuthor_WhenCreateBook_ThenThrowIllegalArgumentException() {
            // Given & When & Then
            assertThatThrownBy(() -> BookFixture.createBook("title", null, "isbn", BookSource.MANUAL))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Author cannot be empty");
        }

        @Test
        @DisplayName("Throw IllegalArgumentException when author exceeds maximum length(255)")
        void GivenBookInformationWithAuthorExceedMaximumLength_WhenCreateBook_ThenThrowIllegalArgumentException() {
            // Given & When & Then
            assertThatThrownBy(() -> BookFixture.createBook("title", "t".repeat(256), "isbn", BookSource.MANUAL))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Author cannot exceed 255 characters");
        }
    }

    @Nested
    class ISBNFieldTest {
        @Test
        @DisplayName("Throw IllegalArgumentException when ISBN is null")
        void GivenBookInformationWithNullISBM_WhenCreateBook_ThenThrowIllegalArgumentException() {
            // Given & When & Then
            assertThatThrownBy(() -> BookFixture.createBook("title", "author", null, BookSource.MANUAL))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("ISBN must be 13 characters");
        }

        @Test
        @DisplayName("Throw IllegalArgumentException when ISBN exceeds maximum length(13)")
        void GivenBookInformationWithExceedMaximumLength_WhenCreateBook_ThenThrowIllegalArgumentException() {
            // Given & When & Then
            assertThatThrownBy(() -> BookFixture.createBook("title", "author", "1".repeat(14), BookSource.MANUAL))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("ISBN must be 13 characters");
        }
    }

    @Nested
    class SourceFieldTest {
        @Test
        @DisplayName("Throw IllegalArgumentException when source is null")
        void GivenBookInformationWithNullSource_WhenCreateBook_ThenThrowIllegalArgumentException() {
            // Given & When & Then
            assertThatThrownBy(() -> BookFixture.createBook("title", "author", "isbn", null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Book source cannot be null");
        }
    }
}