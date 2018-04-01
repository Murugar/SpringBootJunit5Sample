package com.iqmsoft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iqmsoft.model.Person;
import com.iqmsoft.repos.PersonRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository employeeRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Person getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Person getEmployeeByName(String name) {
        return employeeRepository.findByName(name);
    }

    @Override
    public boolean exists(String name) {
        return employeeRepository.findByName(name) != null;
    }

    @Override
    public Person save(Person employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Person> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
