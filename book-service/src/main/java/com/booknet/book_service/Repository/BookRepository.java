package com.booknet.book_service.Repository;
import com.booknet.book_service.Entity.book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<book, Long> {
}
