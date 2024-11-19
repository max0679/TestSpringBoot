package ru.maslenikov.tasknumbertwowithboot.model.firstDb;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Transient
    public static int cntItemsByPage;
    static {
        cntItemsByPage = 2;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Наименование не должно быть пустым")
    private String name;

    @Column(name = "author")
    private String author;

    @Min(value = 1000, message = "Минимальный год издания 1000")
    @Column(name = "year_of_production")
    private int yearOfProduction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public String getOwnerName() {
        return this.getPerson() == null ? "" : this.getPerson().getName() + " взял эту книгу";
    }

    public static String getValidateSort(String sort) {
        if (sort == null || !"none;yearOfProduction;name;author".contains(sort)) {
            return "none";
        }
        return sort;
    }

}
