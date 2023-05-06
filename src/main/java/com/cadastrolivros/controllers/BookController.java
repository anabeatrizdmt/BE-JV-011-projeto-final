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
                         @RequestParam("publicationDate") LocalDate publicationDate) {
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
}
