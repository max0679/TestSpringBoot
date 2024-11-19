package ru.maslenikov.tasknumbertwowithboot.model;

import jakarta.servlet.http.HttpServletRequest;

public class Request {

    public static int getPage(HttpServletRequest request) {
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ex) {
            page = 1;
        }

        return page;
    }

}
