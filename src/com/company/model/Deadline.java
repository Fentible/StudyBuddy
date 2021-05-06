package com.company.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class Deadline implements CalenderModelClass, Serializable {

    protected LocalDateTime dueDate;
    protected String title;
    protected Module module;
    protected int weighting;

    public Deadline(String dueDate, String title, Module module, int weighting) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.dueDate = LocalDateTime.parse(dueDate, formatter);
        this.title = title;
        this.module = module;
        this.weighting = weighting;
    }

    public LocalDateTime getDueDate() { return dueDate; }
    public String getTitle() { return title; }

    public int getWeighting() {
        return weighting;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public LocalDateTime getEnd() { return dueDate; }

    @Override
    public String[] toCSV() {
        return new String[]{this.getTitle(), this.getModule().getCode(), this.getEnd().toString()};

    }
}
