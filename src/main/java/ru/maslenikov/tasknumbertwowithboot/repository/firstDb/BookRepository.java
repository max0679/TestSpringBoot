package ru.maslenikov.tasknumbertwowithboot.repository.firstDb;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.maslenikov.tasknumbertwowithboot.model.firstDb.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findOtherBookByNameAndAuthorAndYearOfProductionAndIdNot(String name, String author, int year, int id);

    List<Book> findByNameContaining(String name, Pageable pageable);

    // два метода ниже работают одинаково
    Integer countBookByNameContaining(String name);

    @Query("select count(b) from Book b where b.name like %:name%")
    Integer countBookByNameContainingQuery(@Param("name") String name);

//    @Query("select count(book) from Book book where book.name like %?1%")
//    Integer findCountBookByName(String name);
}
