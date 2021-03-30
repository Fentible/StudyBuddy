package com.company.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline implements CalenderModelClass, Serializable {

    protected LocalDateTime dueDate;
    protected String title;

    public Deadline(String dueDate, String title) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.dueDate = LocalDateTime.parse(dueDate, formatter);
        this.title = title;
    }

    public LocalDateTime getDueDate() { return dueDate; }
    public String getTitle() { return title; }

    @Override
    public LocalDateTime getEnd() { return dueDate; }
}
