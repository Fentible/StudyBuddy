package com.company.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Milestone implements CalenderModelClass, Serializable {

    private ArrayList<Task> requiredTasks = new ArrayList<>();
    private Deadline event; // what the milestone is working towards
    private String title;
    private LocalDateTime end;
    private int completion;
    private Module module;

    // Constructors


    public Milestone(ArrayList<Task> requiredTasks, Deadline event, String title, String end) {
        this.requiredTasks = requiredTasks;
        this.event = event;
        this.title = title;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.end = LocalDateTime.parse(end, formatter);
        this.updateCompletion();
        module = event.getModule();
    }

    public ArrayList<Task> getRequiredTasks() { return requiredTasks; }
    public Deadline getEvent() { return event; }
    public void updateCompletion() {
        int total = 0, complete = 0;
        for(Task task : requiredTasks) {
            if(task.isComplete()) {
                complete++;
            }
            total++;
        }
        completion = (complete / total) * 100;
    }

    public void setRequiredTasks(ArrayList<Task> requiredTasks) { this.requiredTasks = requiredTasks; }
    public void setEvent(Deadline event) { this.event = event; }
    public void setTitle(String title) { this.title = title; }
    public void setEnd(LocalDateTime end) { this.end = end; }
    public void setCompletion(int completion) { this.completion = completion; }

    public void updateMilestone(Milestone milestone) {
        this.setTitle(milestone.getTitle());
        this.setEnd(milestone.getEnd());
        this.setEvent(milestone.getEvent());
        this.setRequiredTasks(milestone.getRequiredTasks());
        this.module = milestone.getEvent().getModule();
        this.updateCompletion();
    }

    public int getCompletion() {
        return completion;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public LocalDateTime getEnd() { return end; }

    @Override
    public String[] toCSV() {
        return new String[]{this.getTitle(), this.getEvent().getTitle(), this.getEnd().toString(),
                Integer.toString(this.getCompletion()),
                this.getRequiredTasks() == null ? "" : this.getRequiredTasks().stream().map(Task::getTitle).collect(Collectors.joining()) };

    }
}
