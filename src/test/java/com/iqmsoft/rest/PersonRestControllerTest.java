package com.iqmsoft.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqmsoft.extensions.MockitoExtension;
import com.iqmsoft.model.Person;
import com.iqmsoft.rest.PersonRestController;
import com.iqmsoft.service.PersonService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class PersonRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PersonService employeeService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PersonRestController employeeRestController = new PersonRestController(employeeService);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeRestController).build();
    }

    @Test
    void whenPostEmployee_thenCreateEmployee() throws Exception {
        Person alex = new Person("test1");
        given(employeeService.save(any(Person.class))).willReturn(alex);

        mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(alex)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("test1")))
            .andDo(print())
        ;
        verify(employeeService, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(employeeService);
    }

    @Test
    void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {
        Person alex = new Person("test1");
        Person john = new Person("test2");
        Person bob = new Person("test3");
        List<Person> allEmployees = Arrays.asList(alex, john, bob);

        given(employeeService.getAllEmployees()).willReturn(allEmployees);

        mockMvc.perform(get("/api/v1/employees").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", is(alex.getName())))
            .andExpect(jsonPath("$[1].name", is(john.getName())))
            .andExpect(jsonPath("$[2].name", is(bob.getName())))
            .andDo(print());
        verify(employeeService, VerificationModeFactory.times(1)).getAllEmployees();
        reset(employeeService);
    }
}
