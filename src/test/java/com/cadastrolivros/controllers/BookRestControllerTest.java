package com.cadastrolivros.controllers;

import com.cadastrolivros.model.Book;
import com.cadastrolivros.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Book cleanCode;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        cleanCode = new Book(
                1L,
                "Clean Code: A Handbook of Agile Software Craftsmanship",
                "Noted software expert Robert C. Martin presents a revolutionary paradigm with Clean Code: A Handbook of Agile Software Craftsmanship. Martin has teamed up with his colleagues from Object Mentor to distill their best agile practice of cleaning code “on the fly” into a book that will instill within you the values of a software craftsman and make you a better programmer—but only if you work at it.",
                null,
                BigDecimal.valueOf(202.71),
                431L,
                "978-0132350884",
                LocalDate.of(2023,7,18));
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSaveBookWithCorrectInformations() throws Exception {
        String cleanCodeJson = objectMapper.writeValueAsString(cleanCode);
        doReturn(cleanCode).when(bookService).save(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/rest/book/add")
                        .content(cleanCodeJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(cleanCode.getId()))
                .andExpect(jsonPath("$.title").value(cleanCode.getTitle()))
                .andExpect(jsonPath("$.summary").value(cleanCode.getSummary()))
                .andExpect(jsonPath("$.tableOfContents").value(cleanCode.getTableOfContents()))
                .andExpect(jsonPath("$.price").value(cleanCode.getPrice().doubleValue()))
                .andExpect(jsonPath("$.pages").value(cleanCode.getPages()))
                .andExpect(jsonPath("$.isbn").value(cleanCode.getIsbn()))
                .andExpect(jsonPath("$.publicationDate").value(cleanCode.getPublicationDate().toString()));
    }

    @Test
    void shouldNotSaveBookWithMissingInformations() throws Exception {
        String INVALID_BOOK_JSON = "{\n" +
                "\t\"title\": \"Invalid\",\n" +
                "\t\"summary\": null\n" +
                "}";

        doReturn(cleanCode).when(bookService).save(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/rest/book/add")
                        .content(INVALID_BOOK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsMissing() throws Exception {
        String INVALID_BOOK_JSON = "{}";

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/book/add")
                        .content(INVALID_BOOK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Title is mandatory"));
    }

    @Test
    void shouldReturnBadRequestWhenSummaryIsMissing() throws Exception {
        String INVALID_BOOK_JSON = "{\n" +
                "\t\"title\": \"title\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/book/add")
                        .content(INVALID_BOOK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("A summary is mandatory"));
    }

    @Test
    void shouldReturnBadRequestWhenSummaryIsLongerThan500() throws Exception {
        String INVALID_BOOK_JSON = "{\n" +
                "\t\"title\": \"title\",\n" +
                "\t\"summary\": \"summarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummarysummary\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/book/add")
                        .content(INVALID_BOOK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("The summary can't have more than 500 characters"));
    }

    @Test
    void shouldReturnBadRequestWhenPriceIsEmptyOrLessThan20() throws Exception {
        String INVALID_BOOK_JSON = "{\n" +
                "\t\"title\": \"title\",\n" +
                "\t\"summary\": \"summary\",\n" +
                "\t\"price\": 19\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/book/add")
                        .content(INVALID_BOOK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Price must be at least 20"));
    }

    @Test
    void shouldReturnBadRequestWhenPagesIsEmptyOrLessThan100() throws Exception {
        String INVALID_BOOK_JSON = "{\n" +
                "\t\"title\": \"title\",\n" +
                "\t\"summary\": \"summary\",\n" +
                "\t\"price\": 40,\n" +
                "\t\"pages\": 99\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/book/add")
                        .content(INVALID_BOOK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Number of pages must be at least 100"));
    }

    @Test
    void shouldReturnBadRequestWhenIsbnIsMissing() throws Exception {
        String INVALID_BOOK_JSON = "{\n" +
                "\t\"title\": \"title\",\n" +
                "\t\"summary\": \"summary\",\n" +
                "\t\"price\": 40,\n" +
                "\t\"pages\": 100\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/book/add")
                        .content(INVALID_BOOK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("A International Standard Book Number(ISBN) is mandatory"));
    }

    @Test
    void shouldReturnBadRequestWhenPublicationDateIsMissingOrNotInTheFuture() throws Exception {
        String INVALID_BOOK_JSON = "{\n" +
                "\t\"title\": \"title\",\n" +
                "\t\"summary\": \"summary\",\n" +
                "\t\"price\": 40,\n" +
                "\t\"pages\": 100,\n" +
                "\t\"isbn\": \"123-123-123-123\",\n" +
                "\t\"publicationDate\": \"2022-05-20\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/book/add")
                        .content(INVALID_BOOK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("The date of publication needs to be in the future"));
    }

    @Test
    void shouldFindAllBooks() throws Exception {
        List<Book> books = Arrays.asList(
                new Book(1L, "Book 1", "Summary 1", null, BigDecimal.valueOf(50), 100L, "1234567890", LocalDate.of(2023,7,1)),
                new Book(2L, "Book 2", "Summary 2", null, BigDecimal.valueOf(20), 200L, "0987654321", LocalDate.of(2023,8,1))
        );

        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/book/findAll"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].summary").value("Summary 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pages").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value("1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Book 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].summary").value("Summary 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].pages").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isbn").value("0987654321"));
    }

    @Test
    void shouldFindBookById() throws Exception{
        Book book = new Book(1L, "Book 1", "Summary 1", null, BigDecimal.valueOf(50), 100L, "1234567890", LocalDate.of(2023,8,1));

        when(bookService.findById(1L)).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/book/findById/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.summary").value("Summary 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pages").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("1234567890"));
    }

    @Test
    void shouldReturnNotFoundWhenBookNotFound() throws Exception {
        Long bookId = 1L;
        when(bookService.findById(bookId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/book/findById/{id}", bookId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/book/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(bookService).deleteById(1L);
    }

    @Test
    void shouldUpdateBookById() throws Exception {
        Long bookId = 1L;

        Book existingBook = new Book();
        when(bookService.findById(bookId)).thenReturn(existingBook);

        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Book");
        updatedBook.setSummary("Updated Summary");
        updatedBook.setPrice(BigDecimal.valueOf(50.99));
        updatedBook.setPages(300L);
        updatedBook.setIsbn("9876543210");
        updatedBook.setPublicationDate(LocalDate.of(2023, 9, 1));

        String updatedBookJson = objectMapper.writeValueAsString(updatedBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/book/edit/{id}",bookId) // problema na url
                        .content(updatedBookJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookService, times(1)).save(existingBook);

        Book updatedBookFromService = bookService.findById(bookId);
        assertEquals(updatedBook.getTitle(), updatedBookFromService.getTitle());
        assertEquals(updatedBook.getSummary(), updatedBookFromService.getSummary());
        assertEquals(updatedBook.getTableOfContents(), updatedBookFromService.getTableOfContents());
        assertEquals(updatedBook.getPrice(), updatedBookFromService.getPrice());
        assertEquals(updatedBook.getPages(), updatedBookFromService.getPages());
        assertEquals(updatedBook.getIsbn(), updatedBookFromService.getIsbn());
        assertEquals(updatedBook.getPublicationDate(), updatedBookFromService.getPublicationDate());

    }
}
