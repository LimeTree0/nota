package com.limecoding.api.book.application;

import com.limecoding.api.book.domain.Book;
import com.limecoding.api.book.domain.BookRepository;
import com.limecoding.api.book.presentation.dto.BookDTO;
import com.limecoding.api.common.exception.ResourceNotFoundException;
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

    public BookDTO getBookById(Long id) {
        return BookDTO.from(getBookEntityById(id));
    }

    private Book getBookEntityById(Long id) {
        return bookRepository.findBookById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
}
