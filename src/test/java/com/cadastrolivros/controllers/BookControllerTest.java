package com.cadastrolivros.controllers;

import com.cadastrolivros.model.Book;
import com.cadastrolivros.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private Model model;

    private BookController bookController;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookController = new BookController(bookService);
    }

    @Test
    void shouldAddBookWithValidData() {
        Book book1 = new Book(1L, "Book 1", "Summary 1", null, BigDecimal.valueOf(30), 100L, "1234567890", LocalDate.of(2023, 7, 1));
        String viewName = bookController.addBook(book1, model);
        assertEquals("redirect:http://localhost:8080/books", viewName);
        verify(bookService, times(1)).save(book1);
    }

    @Test
    void shouldNotAddBookWithInvalidData() {
        Book bookInvalid = new Book(1L, "Book 1", "Summary 1", null, BigDecimal.valueOf(10), 100L, "1234567890", LocalDate.of(2023, 7, 1));
        String viewName = bookController.addBook(bookInvalid, model);
        assertEquals("add-book", viewName);
        verify(model, times(1)).addAttribute(eq("errorMessage"), anyString());
        verify(bookService, never()).save(bookInvalid);
    }

    @Test
    void shouldCreateBook() {
        String viewName = bookController.createBook(model);

        assertEquals("add-book", viewName);
        verify(model, times(1)).addAttribute(eq("book"), any(Book.class));
    }

    @Test
    void shouldGetAllBooks() {
        List<Book> books = new ArrayList<>();
        Book book1 = new Book(1L, "Book 1", "Summary 1", null, BigDecimal.valueOf(30), 100L, "1234567890", LocalDate.of(2023, 7, 1));
        Book book2 = new Book(2L, "Book 2", "Summary 2", null, BigDecimal.valueOf(20), 200L, "0987654321", LocalDate.of(2023, 8, 1));
        books.add(book1);
        books.add(book2);

        when(bookService.findAll()).thenReturn(books);

        String viewName = bookController.getAllBooks(model);

        assertEquals("books", viewName);
        verify(bookService, times(1)).findAll();
        verify(model, times(1)).addAttribute("books", books);
    }

    @Test
    void shouldGetBookById() {
        Long bookId = 1L;
        List<Book> books = new ArrayList<>();
        Book book1 = new Book(1L, "Book 1", "Summary 1", null, BigDecimal.valueOf(30), 100L, "1234567890", LocalDate.of(2023, 7, 1));
        books.add(book1);

        when(bookService.queryById(bookId)).thenReturn(books);

        String viewName = bookController.getBookById(bookId, model);

        assertEquals("books", viewName);
        verify(bookService, times(1)).queryById(bookId);
        verify(model, times(1)).addAttribute("books", books);

    }

    @Test
    void shouldDeleteBook() {
        Long bookId = 1L;

        String viewName = bookController.deleteBook(bookId);

        assertEquals("redirect:/books", viewName);
        verify(bookService, times(1)).deleteById(bookId);
    }



}