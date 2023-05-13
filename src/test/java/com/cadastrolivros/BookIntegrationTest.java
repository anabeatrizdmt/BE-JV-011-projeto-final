package com.cadastrolivros;

import com.cadastrolivros.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.DisplayName.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Should add a book")
    public void saveBook() throws Exception {

        String BOOK_JSON = "{\n" +
                "    \"title\": \"Clean Code: A Handbook of Agile Software Craftsmanship\",\n" +
                "    \"summary\": \"Noted software expert Robert C. Martin presents a \",\n" +
                "    \"tableOfContents\": null,\n" +
                "    \"price\": 202.71,\n" +
                "    \"pages\": 431,\n" +
                "    \"isbn\": \"978-0132350884\",\n" +
                "    \"publicationDate\": \"2023-07-18\"\n" +
                "}";

        mockMvc.perform(post("/rest/book/add")
                        .content(BOOK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


        mockMvc.perform(get("/rest/book/findAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Should add a book")
    public void deleteBook() throws Exception {

        String BOOK_JSON = "{\n" +
                "    \"title\": \"Clean Code: A Handbook of Agile Software Craftsmanship\",\n" +
                "    \"summary\": \"Noted software expert Robert C. Martin presents a \",\n" +
                "    \"tableOfContents\": null,\n" +
                "    \"price\": 202.71,\n" +
                "    \"pages\": 431,\n" +
                "    \"isbn\": \"978-0132350884\",\n" +
                "    \"publicationDate\": \"2023-07-18\"\n" +
                "}";

        mockMvc.perform(post("/rest/book/add")
                        .content(BOOK_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/rest/book/findAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(status().isOk());
        
        mockMvc.perform(delete("/rest/book/{id}",1 ))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


        mockMvc.perform(get("/rest/book/findAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(status().isOk());

    }

}
