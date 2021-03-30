package com.company.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Milestone implements CalenderModelClass, Serializable {

    private ArrayList<Task> requiredTasks = new ArrayList<>();
    private Deadline event; // what the milestone is working towards
    private String title;
    private LocalDateTime end;
    private int completion;

    // Constructors


    public Milestone(ArrayList<Task> requiredTasks, Deadline event, String title, String end) {
        this.requiredTasks = requiredTasks;
        this.event = event;
        this.title = title;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.end = LocalDateTime.parse(end, formatter);
        this.updateCompletion();
    }

    public ArrayList<Task> getRequiredTasks() { return requiredTasks; }
    public Deadline getEvent() { return event; }
    private void updateCompletion() {
        int total = 0, complete = 0;
        for(Task task : requiredTasks) {
            if(task.isComplete()) {
                complete++;
            }
            total++;
        }
        completion = (complete / total) * 100;
    }

    public int getCompletion() {
        return completion;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public LocalDateTime getEnd() { return end; }
}
