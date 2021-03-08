package com.company.model;

import java.time.LocalDateTime;

/*
 * Loaded in from the semester file
 * not described much on our documentation
 */
public class Exam {

    private String title;
    private LocalDateTime dueDate;

    public Exam(String title, LocalDateTime dueDate) {
        this.title = title;
        this.dueDate = dueDate;
    }
}
