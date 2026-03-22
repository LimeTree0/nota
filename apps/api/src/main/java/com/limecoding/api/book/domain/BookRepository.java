package com.limecoding.api.book.domain;

public interface BookRepository {
    Book save(Book book);
    Book findBookById(Long id);
}