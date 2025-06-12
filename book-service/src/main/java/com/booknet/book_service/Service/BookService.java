package com.booknet.book_service.Service;
import com.booknet.book_service.DTO.BookDTO;
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
    public book findById(Long id) {
    return bookRepository.findById(id).orElse(null);
}

 public BookDTO getBookDTOById(Long id) {
        book b = bookRepository.findById(id).orElse(null);
        if (b == null) return null;
        return convertToDTO(b);
    }

    private BookDTO convertToDTO(book b) {
        BookDTO dto = new BookDTO();
        dto.setId(b.getId());
        dto.setTitle(b.getTitle());
        dto.setAuthor(b.getAuthor());
        dto.setCategory(b.getCategory());
        dto.setIsbn(b.getIsbn());
        dto.setYear(b.getYear());
        dto.setDescription(b.getDescription());
        dto.setStatus(b.getStatus());
        dto.setPrix(b.getPrix());
        return dto;
    }
}

