package com.booknet.book_service.Service;
import com.booknet.book_service.Entity.book;
import com.booknet.book_service.Repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public book save(book book) {
        return bookRepository.save(book);
    }

    public List<book> getAll() {
        return bookRepository.findAll();
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
