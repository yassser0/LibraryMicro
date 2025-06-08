package com.booknet.book_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booknet.book_service.Entity.Book;
import com.booknet.book_service.Repository.BookRepository;

@Service
public class BookService {
    @Autowired private BookRepository repo;

    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    public Book save(Book book) {
        return repo.save(book);
    }
}

