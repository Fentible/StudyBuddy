package com.company.model;

import java.time.LocalDateTime;

/*
 * Loaded in from the semester file
 * not described much on our documentation
 */
public class Exam extends Deadline {


    public Exam(LocalDateTime dueDate, String title) {
        super(dueDate, title);
    }
}
