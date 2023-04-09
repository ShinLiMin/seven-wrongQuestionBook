package com.seven;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SevenWrongQuestionBookApp {
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(SevenWrongQuestionBookApp.class, args);
    }
}
