package com.cadastrolivros.services;

import com.cadastrolivros.model.Book;
import com.cadastrolivros.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void save_ValidBook_ReturnsSavedBook() {
        Book book = new Book();
        book.setTitle("Clean Code");

        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.save(book);

        verify(bookRepository, times(1)).save(book);
        assertEquals(book, savedBook);
    }

    @Test
    void findAll_ReturnsListOfBooks() {
        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(new Book());
        expectedBooks.add(new Book());

        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.findAll();

        verify(bookRepository, times(1)).findAll();
        assertEquals(expectedBooks, actualBooks);
    }

    @Test
    void findById_ExistingId_ReturnsBook() {
        Long bookId = 1L;
        Book expectedBook = new Book();
        expectedBook.setId(bookId);
        expectedBook.setTitle("Clean Code");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));

        Book actualBook = bookService.findById(bookId);

        verify(bookRepository, times(1)).findById(bookId);
        assertEquals(expectedBook, actualBook);
    }

    @Test
    void findById_NonExistingId_ReturnsNull() {
        Long bookId = 100L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Book actualBook = bookService.findById(bookId);

        verify(bookRepository, times(1)).findById(bookId);
        assertNull(actualBook);
    }

    @Test
    void queryById_ReturnsListOfBooks() {
        Long bookId = 1L;
        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(new Book());
        expectedBooks.add(new Book());

        when(bookRepository.queryById(bookId)).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.queryById(bookId);

        verify(bookRepository, times(1)).queryById(bookId);
        assertEquals(expectedBooks, actualBooks);
    }

    @Test
    void deleteById_ValidId_DeletesBook() {
        Long bookId = 1L;

        bookService.deleteById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }
}