package com.iqmsoft.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqmsoft.model.Person;
import com.iqmsoft.repos.PersonRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = H2)
@AutoConfigureRestDocs(outputDir = "target/rest-docs")
@Tag("integration")
class PersonRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository employeeRepository;

    @AfterEach
    void resetDb() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("when valid input then create employee with HTTP status = 201")
    void whenValidInput_thenCreateEmployee() throws Exception {
        Person bob = new Person("bob");
        mvc.perform(post("/api/v1/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bob)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("api-v1-employees-post",
                responseFields(
                    fieldWithPath("id").description("The employees's id"),
                    fieldWithPath("name").description("The employee's name")
                ))
            )
        ;

        List<Person> employeesFound = employeeRepository.findAll();
        assertAll(
            "verify returned collection",
            () -> assertThat(employeesFound.size(), equalTo(1)),
            () -> assertThat(employeesFound.get(0), notNullValue()),
            () -> assertThat(employeesFound.get(0).getId(), notNullValue()),
            () -> assertThat(employeesFound.get(0).getName(), notNullValue())
        );
    }

    @Test
    @DisplayName("given Employees when GET /employees then HTTP status = 200")
    void givenEmployees_whenGetEmployees_thenStatus200() throws Exception {

        Person bob = new Person("test1");
        Person alex = new Person("test2");
        employeeRepository.save(Arrays.asList(bob, alex));
        employeeRepository.flush();

        mvc.perform(get("/api/v1/employees").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
            .andExpect(jsonPath("$[0].name", is("test1")))
            .andExpect(jsonPath("$[0].id", notNullValue()))
            .andExpect(jsonPath("$[1].name", is("test2")))
            .andExpect(jsonPath("$[1].id", notNullValue()))
            .andDo(print())
            .andDo(document("api-v1-employees-get",
                responseFields(
                    fieldWithPath("[]").description("An array of employees"),
                    fieldWithPath("[].id").description("The employees's id"),
                    fieldWithPath("[].name").description("The employee's name")
                )
            ))
        ;
    }
}