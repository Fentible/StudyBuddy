package com.company.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline implements CalenderModelClass, Serializable {

    protected LocalDateTime dueDate;
    protected String title;
    protected Module module;

    public Deadline(String dueDate, String title, Module module) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.dueDate = LocalDateTime.parse(dueDate, formatter);
        this.title = title;
        this.module = module;
    }

    public LocalDateTime getDueDate() { return dueDate; }
    public String getTitle() { return title; }

    public Module getModule() {
        return module;
    }

    @Override
    public LocalDateTime getEnd() { return dueDate; }
}
