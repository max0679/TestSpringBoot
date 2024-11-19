package ru.maslenikov.tasknumbertwowithboot.model.firstDb;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Имя читателя не должно быть пустым")
    @Column(name = "name")
    private String name;

    @Min(value = 1900, message = "Минимальный год: 1900")
    @Column(name = "year")
    private int year;

    @OneToMany(mappedBy = "person")
    private List<Book> books;

    @Transactional
    public List<Book> getBooks() {
        return books;
    }
}
