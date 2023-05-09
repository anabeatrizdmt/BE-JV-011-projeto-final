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

    @Autowired
    private BookService bookService;

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
        return ResponseEntity.ok(savedBook);    }

    @GetMapping("/findAll")
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/findById/{id}")
    public Book findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> update(@RequestParam Long id, @RequestBody Book book) {

        //todo: verificações dos campos

        try {
            bookService.save(book);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok(book);

    }

}
