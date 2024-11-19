package ru.maslenikov.tasknumbertwowithboot.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.maslenikov.tasknumbertwowithboot.model.firstDb.Book;
import ru.maslenikov.tasknumbertwowithboot.service.BookService;


@Component
public class BookValidator implements Validator {

    private final BookService bookService;

    public BookValidator(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        if (bookService.findOtherBookByNameAndAuthorAndYear(book)) {
            errors.rejectValue("name", "500", "Книга с данным наименованием уже существует");
        }

    }
}
