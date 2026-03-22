package com.limecoding.api.book.infrastructure;

import com.limecoding.api.book.domain.Book;
import com.limecoding.api.book.domain.BookRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookJpaRepository extends BookRepository, JpaRepository<Book, Long> {
}