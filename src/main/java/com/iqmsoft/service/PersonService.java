package com.iqmsoft.service;

import java.util.List;

import com.iqmsoft.model.Person;

public interface PersonService {

    Person getEmployeeById(Long id);

    Person getEmployeeByName(String name);

    List<Person> getAllEmployees();

    boolean exists(String email);

    Person save(Person employee);
}
