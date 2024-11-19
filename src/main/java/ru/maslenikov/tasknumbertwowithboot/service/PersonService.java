package ru.maslenikov.tasknumbertwowithboot.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maslenikov.tasknumbertwowithboot.model.firstDb.Person;
import ru.maslenikov.tasknumbertwowithboot.repository.firstDb.PersonRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    @PersistenceContext(unitName = "firstDbEntityManagerFactory")
    private EntityManager entityManager;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    public List<Person> getPersonsWithBooks() {
        return entityManager.unwrap(Session.class).createQuery("SELECT p FROM Person p LEFT JOIN FETCH p.books").getResultList();
    }

    public Person getOnePerson(int id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person getOnePersonWithBooks(int id) {
        return entityManager.unwrap(Session.class).createQuery("SELECT p FROM Person p LEFT JOIN FETCH p.books WHERE p.id = :id", Person.class).setParameter("id", id).getResultList().stream().findAny().orElse(null);
    }

    @Transactional(readOnly = false)
    public void update(Person person) {
        personRepository.save(person);
    }

    @Transactional(readOnly = false)
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional(readOnly = false)
    public void delete(int id) {

        Person person = getOnePerson(id);

        if (person != null) {
            personRepository.delete(person);
        }
    }

}
