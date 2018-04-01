package com.iqmsoft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iqmsoft.repos.PersonRepository;
import com.iqmsoft.rest.PersonRestController;
import com.iqmsoft.service.PersonService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

// uncomment to run with JUNit4 runner if the IDE doesn't have built-in JUnit5 support
//@RunWith(JUnitPlatform.class)
@SpringBootTest
@ExtendWith(SpringExtension.class)
class MainAppTest {

    @Autowired
    PersonRestController controller;
    @Autowired
    PersonService employeeService;
    @Autowired
    PersonRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("When services are injected then they should not be null")
    void contextLoads() {
        assertAll("assert that all injections are not null",
            () -> assertThat(controller, notNullValue()),
            () -> assertThat(employeeService, notNullValue()),
            () -> assertThat(employeeRepository, notNullValue()),
            () -> assertThat(objectMapper, notNullValue())
        );
	}
}
