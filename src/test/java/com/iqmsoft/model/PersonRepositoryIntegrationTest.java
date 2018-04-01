package com.iqmsoft.model;

import com.iqmsoft.extensions.MockitoExtension;
import com.iqmsoft.model.Person;
import com.iqmsoft.repos.PersonRepository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIn.isOneOf;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Tag("integration")
class PersonRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository employeeRepository;

    @Test
    @DisplayName("when FindByName then return the employee")
    void whenFindByName_thenReturnEmployee() {
        assertThat(employeeRepository, notNullValue());
        // given
        Person expected = new Person("alex");
        Object id = entityManager.persistAndGetId(expected);

        // when
        Person actual = employeeRepository.findByName(expected.getName());

        // then
        assertAll(
            () -> assertThat(actual.getName(), equalTo(expected.getName())),
            () -> assertThat(actual.getId(), equalTo(id))
        );
    }

    @Test
    @DisplayName("when FindAll then return all employees")
    void whenFindAll_thenReturnAllEmployees() {
        assertThat(employeeRepository, notNullValue());
        // given
        Person bob = new Person("test1");
        Person alex = new Person("test2");
        Person john = new Person("test3");
        List<Person> employees = Arrays.asList(bob, alex, john);
        employeeRepository.save(employees);
        employeeRepository.flush();

        // when
        List<Person> allEmployees = employeeRepository.findAll();

        // then
        assertAll(
            () -> assertThat(allEmployees, hasSize(3)),
            () -> assertThat(
                allEmployees.stream().map(Person::getName).collect(Collectors.toList()),
                containsInAnyOrder(alex.getName(), bob.getName(), john.getName())
            )
        );
    }

    @TestFactory
    @DisplayName("FindById - Dynamic Id Tests")
    Stream<DynamicTest> generateFindByIdDynamicTests() {
        assertThat(employeeRepository, notNullValue());
        // given
        Person bob = new Person("bob");
        Person alex = new Person("alex");
        Person john = new Person("john");
        List<Person> employees = Arrays.asList(bob, alex, john);
        employeeRepository.save(employees);
        employeeRepository.flush();

        // when
        Long[] ids = employeeRepository.findAll().stream()
            .map(Person::getId)
            .collect(Collectors.toList())
            .toArray(new Long[]{});

        // then
        return Stream.of(ids).map(id -> dynamicTest("Find by employee id " + id, () -> {
            Person emp = employeeRepository.findById(id);
            assertThat(emp, notNullValue());
            assertThat(emp.getName(), isOneOf("bob","alex","john"));
        }));
    }
}

