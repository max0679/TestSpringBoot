package ru.maslenikov.tasknumbertwowithboot.repository.firstDb;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maslenikov.tasknumbertwowithboot.model.firstDb.Person;


public interface PersonRepository extends JpaRepository<Person, Integer> {
}
