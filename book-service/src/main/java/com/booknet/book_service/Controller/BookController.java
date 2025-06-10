package com.booknet.book_service.Controller;
import com.booknet.book_service.Entity.book;
import com.booknet.book_service.Service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@CrossOrigin("*")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<book> addBook(@RequestBody book book) {
        return ResponseEntity.ok(bookService.save(book));
    }

    @GetMapping
    public ResponseEntity<List<book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

      @GetMapping("/{id}")
    public ResponseEntity<book> getBookById(@PathVariable Long id) {
        book foundBook = bookService.findById(id);
        if (foundBook != null) {
            return ResponseEntity.ok(foundBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<book> updateBook(@PathVariable Long id, @RequestBody book updatedBook) {
        book existingBook = bookService.findById(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        // Mettre à jour les champs
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setYear(updatedBook.getYear());
        existingBook.setStatus(updatedBook.getStatus());
        existingBook.setPrix(updatedBook.getPrix());
        existingBook.setDescription(updatedBook.getDescription());
        

        return ResponseEntity.ok(bookService.save(existingBook));
    }
}

