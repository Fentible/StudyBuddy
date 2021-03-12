package com.company.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Milestone implements CalenderModelClass {

    private ArrayList<Task> requiredTasks = new ArrayList<>();
    private Deadline event; // what the milestone is working towards
    String title;
    LocalDateTime end;

    // Constructors
    public Milestone(Deadline event) { this.event = event; }
    public Milestone(ArrayList<Task> requiredTasks, Deadline event) {
        this.requiredTasks = requiredTasks;
        this.event = event;
    }

    public ArrayList<Task> getRequiredTasks() { return requiredTasks; }
    public Deadline getEvent() { return event; }


    @Override
    public String getTitle() { return title; }

    @Override
    public LocalDateTime getEnd() { return end; }
}
