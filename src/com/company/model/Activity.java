package com.company.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Activity implements CalenderModelClass, Serializable {

    private ActivityType type;
    private int contribution;
    private int timeSpent;
    private String notes;
    private String title;
    private LocalDateTime end;
    private ArrayList<Task> relatedTasks;

    public Activity(ActivityType type, int contribution, int timeSpent, String notes, String title,
                    String endDate, ArrayList<Task> tasks) {
        this.type = type;  this.contribution = contribution; this.timeSpent = timeSpent;
        this.notes = notes; this.title = title;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.end = LocalDateTime.parse(endDate, formatter);
        this.relatedTasks = tasks;

    }

    public ActivityType getType() { return type; }
    public int getContribution() { return contribution; }
    public int getTimeSpent() { return timeSpent; }
    public String getNotes() { return notes; }
    public void updateProgress(int progress) {
        this.timeSpent = progress;
        for(Task task : relatedTasks) {
            task.updateProgress();
        }
    }

    @Override
    public String[] toCSV() {
        return new String[]{this.getTitle(), this.getEnd().toString(),
               this.getRelatedTasks().stream().map(Task::getTitle).collect(Collectors.joining()),
                Integer.toString(this.getContribution()), Integer.toString(this.getTimeSpent()) };
    }

    public void updateActivity(Activity activity) {
        this.setTitle(activity.getTitle());
        this.setContribution(activity.getContribution());
        this.setEnd(activity.getEnd());
        this.setNotes(activity.getNotes());
        this.setNotes(activity.getNotes());
        this.setTimeSpent(activity.getTimeSpent());
        this.setType(activity.getType());
        this.setRelatedTasks(activity.getRelatedTasks());
    }

    public static void main(String[] args) {
        //Activity activity = new Activity(ActivityType.READING, 80, 60);
        //System.out.println(activity.getTimeSpent());
    }

    public ArrayList<Task> getRelatedTasks() {
        return relatedTasks;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setRelatedTasks(ArrayList<Task> relatedTasks) {
        this.relatedTasks = relatedTasks;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public LocalDateTime getEnd() { return end; }

}
