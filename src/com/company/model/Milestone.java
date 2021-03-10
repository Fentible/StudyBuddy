package com.company.model;

import java.util.ArrayList;

public class Milestone {

    private ArrayList<Task> requiredTasks = new ArrayList<>();
    private Deadline event; // what the milestone is working towards

    // Constructors
    public Milestone(Deadline event) { this.event = event; }
    public Milestone(ArrayList<Task> requiredTasks, Deadline event) {
        this.requiredTasks = requiredTasks;
        this.event = event;
    }

    public ArrayList<Task> getRequiredTasks() { return requiredTasks; }
    public Deadline getEvent() { return event; }


}
