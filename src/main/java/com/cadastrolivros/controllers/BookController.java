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

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @PostMapping("/books")
    public String addBook(@RequestParam("title") String title,
                         @RequestParam("summary") String summary,
                         @RequestParam("tableOfContents") String tableOfContents,
                         @RequestParam("price") BigDecimal price,
                         @RequestParam("pages") Long pages,
                         @RequestParam("isbn") String isbn,
                         @RequestParam("publicationDate") LocalDate publicationDate,
                          Model model){

        String errorMessage = validateBookData(title, summary, tableOfContents, price, pages, isbn, publicationDate);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            return "add-book";
        }

        Book book = new Book();
        book.setTitle(title);
        book.setSummary(summary);
        book.setTableOfContents(tableOfContents);
        book.setPrice(price);
        book.setPages(pages);
        book.setIsbn(isbn);
        book.setPublicationDate(publicationDate);

        bookService.save(book);

        return "redirect:books";
    }

    @GetMapping("add-book")
    public String createBook() {
        return "add-book";
    }

    @PostMapping("/delete/{id}")
    public String deletePet(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/books/books";
    }

    private String validateBookData(String title, String summary, String tableOfContents, BigDecimal price, Long pages, String isbn, LocalDate publicationDate) {
        if (title.isEmpty()) {
            return "Title is mandatory";
        }
        if (summary.isEmpty()) {
            return "A summary is mandatory";
        }
        if (summary.length() > 500) {
            return "The summary can't have more than 500 characters";
        }
        if (price == null || price.compareTo(BigDecimal.valueOf(20)) < 0) {
            return "Price must be at least 20";
        }
        if (pages == null || pages < 100) {
            return "Number of pages must be at least 100";
        }
        if (isbn.isEmpty()) {
            return "An International Standard Book Number(ISBN) is mandatory";
        }
        if (publicationDate.compareTo(LocalDate.now().plusDays(1)) <= 0) {
            return "The date of publication needs to be in the future";
        }
        return null;
    }
}
