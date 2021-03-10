package com.company.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Deadline {

    protected LocalDateTime dueDate;
    protected String title;

    public Deadline(String dueDate, String title) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.dueDate = LocalDateTime.parse(dueDate, formatter);
        this.title = title;
    }

    public LocalDateTime getDueDate() { return dueDate; }
    public String getTitle() { return title; }
}
