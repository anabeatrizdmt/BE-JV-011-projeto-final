package com.cadastrolivros.controllers;

import com.cadastrolivros.model.Book;
import com.cadastrolivros.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String getAllBooks(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @PostMapping("/search")
    public String getBookById(@RequestParam("id") Long id, Model model) {
        List<Book> books = bookService.queryById(id);
        model.addAttribute("books", books);
        return "books";
    }

    @PostMapping("/books")
    public String addBook(@ModelAttribute("book") Book book, Model model) {
        String errorMessage = validateBookData(book);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            return "add-book";
        }

        bookService.save(book);
        return "redirect:http://localhost:8080/books";
    }

    @GetMapping("/add-book")
    public String createBook(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }


    private String validateBookData(Book book) {
        if (book.getTitle().isEmpty()) {
            return "Title is mandatory";
        }
        if (book.getSummary().isEmpty()) {
            return "A summary is mandatory";
        }
        if (book.getSummary().length() > 500) {
            return "The summary can't have more than 500 characters";
        }
        if (book.getPrice() == null || book.getPrice().compareTo(BigDecimal.valueOf(20)) < 0) {
            return "Price must be at least 20";
        }
        if (book.getPages() == null || book.getPages() < 100) {
            return "Number of pages must be at least 100";
        }
        if (book.getIsbn().isEmpty()) {
            return "An International Standard Book Number(ISBN) is mandatory";
        }
        if (!book.getPublicationDate().isAfter(LocalDate.now().plusDays(1))) {
            return "The date of publication needs to be in the future";
        }
        return null;
    }
}
