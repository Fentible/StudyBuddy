package com.company.model;

import java.time.LocalDateTime;

/*
 * Loaded in from the semester file
 * not described much on our documentation
 */
public class Assignment {

    private String title;
    private LocalDateTime dueDate;

    public Assignment(String title, LocalDateTime dueDate) {
        this.dueDate = dueDate;
        this.title = title;
    }
}
