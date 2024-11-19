package ru.maslenikov.tasknumbertwowithboot.config.firstDb;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstDbDataSource {

    private String url;
    private String username;
    private String password;
    private String classDriver;

}