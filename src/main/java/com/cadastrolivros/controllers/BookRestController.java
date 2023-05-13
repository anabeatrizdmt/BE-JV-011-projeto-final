package com.cadastrolivros.controllers;

import com.cadastrolivros.model.Book;
import com.cadastrolivros.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rest/book")
public class BookRestController {

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body("Title is mandatory");
        }
        if (book.getSummary() == null || book.getSummary().isEmpty()) {
            return ResponseEntity.badRequest().body("A summary is mandatory");
        }
        if (book.getSummary().length() > 500) {
            return ResponseEntity.badRequest().body("The summary can't have more than 500 characters");
        }
        if (book.getPrice() == null || book.getPrice().compareTo(BigDecimal.valueOf(20)) < 0) {
            return ResponseEntity.badRequest().body("Price must be at least 20");
        }
        if (book.getPages() == null || book.getPages() < 100) {
            return ResponseEntity.badRequest().body("Number of pages must be at least 100");
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            return ResponseEntity.badRequest().body("A International Standard Book Number(ISBN) is mandatory");
        }
        if (book.getPublicationDate() == null || book.getPublicationDate().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body("The date of publication needs to be in the future");
        }

        Book savedBook = bookService.save(book);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("/findAll")
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Book updatedBook) {
        if (id == null) {
            return ResponseEntity.badRequest().body("ID is mandatory");
        }

        Book existingBook = bookService.findById(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            updateBook(existingBook, updatedBook);
            Book savedBook = bookService.save(existingBook);
            return ResponseEntity.ok(savedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    private void updateBook(Book existingBook, Book updatedBook) {
        if (updatedBook.getTitle() != null) {
            existingBook.setTitle(updatedBook.getTitle());
        }
        if (updatedBook.getSummary() != null) {
            if (updatedBook.getSummary().length() <= 500) {
                existingBook.setSummary(updatedBook.getSummary());
            } else {
                throw new IllegalArgumentException("The summary can't have more than 500 characters");
            }
        }
        if (updatedBook.getTableOfContents() != null) {
            existingBook.setTableOfContents(updatedBook.getTableOfContents());
        }
        if (updatedBook.getPrice() != null) {
            if (updatedBook.getPrice().compareTo(BigDecimal.valueOf(20)) >= 0) {
                existingBook.setPrice(updatedBook.getPrice());
            } else {
                throw new IllegalArgumentException("Price must be at least 20");
            }
        }
        if (updatedBook.getPages() != null) {
            if (updatedBook.getPages() >= 100) {
                existingBook.setPages(updatedBook.getPages());
            } else {
                throw new IllegalArgumentException("Number of pages must be at least 100");
            }
        }
        if (updatedBook.getIsbn() != null) {
            existingBook.setIsbn(updatedBook.getIsbn());
        }
        if (updatedBook.getPublicationDate() != null) {
            if (updatedBook.getPublicationDate().isAfter(LocalDate.now().plusDays(1))) {
                existingBook.setPublicationDate(updatedBook.getPublicationDate());
            } else {
                throw new IllegalArgumentException("The date of publication needs to be in the future");
            }
        }
    }
}
