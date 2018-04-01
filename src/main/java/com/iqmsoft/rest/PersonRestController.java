package com.iqmsoft.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iqmsoft.model.Person;
import com.iqmsoft.service.PersonService;
import com.iqmsoft.utils.NotFoundException;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/api/v1")
public class PersonRestController {

    private static final Logger logger = LoggerFactory.getLogger(PersonRestController.class);

    private final PersonService employeeService;

    @ResponseStatus(value=NOT_FOUND, reason="Resource not found")
    @ExceptionHandler({
        NotFoundException.class
    })
    void handleNotFoundResponse(NotFoundException e) {
        logger.error("Status = {}, Message = {}", NOT_FOUND, e.getMessage());
        logger.debug("Status = {}, Message = {}", NOT_FOUND, e);
    }

    @Autowired
    public PersonRestController(PersonService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(
        value="/employees",
        produces = { APPLICATION_JSON_UTF8_VALUE }
    )
    public Collection<Person> getAllEmployees() {
        logger.info("Called REST endpoint: GET /api/v1/emploxyees");
        return employeeService.getAllEmployees();
    }

    @PostMapping(
        value="/employees",
        consumes = { APPLICATION_JSON_UTF8_VALUE }
    )
    @ResponseStatus(CREATED)
    public Person createEmployee(@RequestBody Person employee) {
        logger.info("Called REST endpoint: POST /api/v1/employees with body {}", employee);
        return employeeService.save(employee);
    }
}
