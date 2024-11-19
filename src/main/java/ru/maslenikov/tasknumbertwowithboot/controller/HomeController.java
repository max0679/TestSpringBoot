package ru.maslenikov.tasknumbertwowithboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class HomeController {

    @GetMapping(path = {"", "/"})
    public String index() {
        return "redirect:/books";
    }

}
