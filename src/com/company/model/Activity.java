package com.company.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    public static void main(String[] args) {
        //Activity activity = new Activity(ActivityType.READING, 80, 60);
        //System.out.println(activity.getTimeSpent());
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public LocalDateTime getEnd() { return end; }

}
