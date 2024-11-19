package ru.maslenikov.tasknumbertwowithboot.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maslenikov.tasknumbertwowithboot.model.firstDb.Person;
import ru.maslenikov.tasknumbertwowithboot.service.PersonService;


@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personService.getPersonsWithBooks());
        return "people/index";
    }

    @Transactional
    @GetMapping("/{id}")
    public String onePerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.getOnePersonWithBooks(id));
        return "people/onePerson";
    }

    @GetMapping("/{id}/edit")
    public String editPersonForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.getOnePerson(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "people/edit";
        }

        personService.update(person);
        return "redirect:/people/" + id;
    }

    @GetMapping("/new")
    public String createPersonForm(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping("")
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        personService.save(person);
        return "redirect:/people";

    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/people";
    }

}
