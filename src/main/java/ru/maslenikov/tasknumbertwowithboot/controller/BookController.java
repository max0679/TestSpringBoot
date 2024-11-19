package ru.maslenikov.tasknumbertwowithboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maslenikov.tasknumbertwowithboot.model.firstDb.Book;
import ru.maslenikov.tasknumbertwowithboot.model.firstDb.Person;
import ru.maslenikov.tasknumbertwowithboot.model.Request;
import ru.maslenikov.tasknumbertwowithboot.service.BookService;
import ru.maslenikov.tasknumbertwowithboot.service.PersonService;
import ru.maslenikov.tasknumbertwowithboot.validator.BookValidator;

import java.util.List;


@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PersonService personService;
    private final BookValidator bookValidator;

    @Autowired
    public BookController(BookService bookService, PersonService personService, BookValidator bookValidator) {
        this.bookService = bookService;
        this.personService = personService;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String index(HttpServletRequest request, Model model, @RequestParam(value = "sort", required = false) String sort) {

        int page = Request.getPage(request);

        sort = Book.getValidateSort(sort);

        List<Book> books = bookService.getBooksByPage(page, sort);

        model.addAttribute("sort", sort);
        model.addAttribute("books", bookService.getBooksByPage(page, sort));
        model.addAttribute("countOfPages", bookService.getCountOfPages((int) bookService.getCountOfBooks()));
        model.addAttribute("currentPage", page);
        return "books/index";
    }

    @GetMapping("/{id}")
    public String showBook(Model model, @PathVariable("id") int id) {

        Book book = bookService.getOneBook(id);
        if (book == null) {
            return "redirect:/books";
        }

        model.addAttribute("book", book);
        return "books/oneBook";
    }

    @GetMapping("/{id}/edit")
    public String editBookForm(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.getOneBook(id));
        model.addAttribute("persons", personService.getPersons());
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, HttpServletRequest httpServletRequest, @PathVariable("id") int id) {

        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        Person person = null;

        try {
            int ownerId = Integer.parseInt(httpServletRequest.getParameter("owner"));
            if (ownerId > 0) {
                person = personService.getOnePerson(ownerId);
            }
        } finally {
            book.setPerson(person);
            bookService.update(book);
        }

        return "redirect:/books/" + id;
    }

    @GetMapping("/new")
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("persons", personService.getPersons());
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, HttpServletRequest httpServletRequest) {

        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        Person person = null;

        try {
            int ownerId = Integer.parseInt(httpServletRequest.getParameter("owner"));
            if (ownerId > 0) {
                person = personService.getOnePerson(ownerId);
            }
        } finally {
            book.setPerson(person);
            bookService.save(book);
        }
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam(value = "search", required = false) String search,
                              @RequestParam(value = "sort", required = false) String sort,
                              Model model, HttpServletRequest request) {

        if (search == null || search.isEmpty()) {
            return "redirect:/books";
        }

        int page = Request.getPage(request);

        sort = Book.getValidateSort(sort);

        List<Book> books = bookService.getBooksByPage(page, sort, search);

        model.addAttribute("sort", sort);
        model.addAttribute("books", books);
        model.addAttribute("countOfPages", bookService.getCountOfPages((int) bookService.getCountOfBooks(search)));
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        return "books/search";

    }

}
