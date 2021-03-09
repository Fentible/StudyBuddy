package com.company.model;

import java.time.LocalDateTime;

public class Deadline {

    protected LocalDateTime dueDate;
    protected String title;

    public Deadline(LocalDateTime dueDate, String title) {
        this.dueDate = dueDate;
        this.title = title;
    }

}
