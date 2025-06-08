package com.booknet.book_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booknet.book_service.Entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
