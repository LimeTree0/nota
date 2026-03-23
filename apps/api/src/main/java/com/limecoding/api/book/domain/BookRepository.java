package com.limecoding.api.book.domain;

import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findBookById(Long id);
}