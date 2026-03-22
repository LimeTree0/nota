package com.limecoding.api.book.application;

import com.limecoding.api.book.domain.Book;
import com.limecoding.api.book.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public Long registerBook(Book book) {
        Book save = bookRepository.save(book);

        return save.getId();
    }
}
