package com.cadastrolivros.services;

import com.cadastrolivros.model.Book;
import com.cadastrolivros.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> queryById(Long id) {
        return bookRepository.queryById(id);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

}
