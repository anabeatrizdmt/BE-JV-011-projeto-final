package com.cadastrolivros.controllers;

import com.cadastrolivros.model.Book;
import com.cadastrolivros.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/book")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public Book save(@RequestBody Book book) {
        return bookService.save(book);
    }

    @GetMapping("/listAll")
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

}
