package ru.maslenikov.tasknumbertwowithboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maslenikov.tasknumbertwowithboot.model.firstDb.Book;
import ru.maslenikov.tasknumbertwowithboot.repository.firstDb.BookRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksByPage(int page, String sort) {
        return sort.equals("none") ?
            bookRepository.findAll(PageRequest.of(page - 1, Book.cntItemsByPage)).getContent() :
            bookRepository.findAll(PageRequest.of(page - 1, Book.cntItemsByPage, Sort.by(sort))).getContent();
    }

    public List<Book> getBooksByPage(int page, String sort, String name) {
        Pageable pageable = sort.equals("none") ?
                PageRequest.of(page - 1, Book.cntItemsByPage) :
                PageRequest.of(page - 1, Book.cntItemsByPage, Sort.by(sort));

        return bookRepository.findByNameContaining(name, pageable);
    }

    public Book getOneBook(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = false)
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    public long getCountOfBooks() {
        return bookRepository.count();
    }

//    private void test() {
//        Person person = bookRepository.findById(1).map(Book::getPerson).orElse(null);
//    }

    public long getCountOfBooksQuery(String name) {
        return bookRepository.countBookByNameContaining(name);
    }

    public long getCountOfBooks(String name) {
        return bookRepository.countBookByNameContaining(name);
    }

    public long getCountOfPages(int pages) {
       // return (int) Math.ceil((double) pages / Book.cntItemsByPage);
        return (int) Math.ceil((double) pages / Book.cntItemsByPage);
    }

    public Boolean findOtherBookByNameAndAuthorAndYear(Book book) {
        return bookRepository.findOtherBookByNameAndAuthorAndYearOfProductionAndIdNot(book.getName(), book.getAuthor(), book.getYearOfProduction(), book.getId()).size() > 0;
    }

    @Transactional(readOnly = false)
    public void update(Book book) {
        bookRepository.save(book);
    }

    @Transactional(readOnly = false)
    public void save(Book book) {
        bookRepository.save(book);
    }

}
