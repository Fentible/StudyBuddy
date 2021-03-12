package com.company.model;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Activity implements CalenderModelClass, Serializable {

    ActivityType type;
    int contribution;
    int timeSpent;
    String notes;
    String title;
    LocalDateTime end;

    public Activity(ActivityType type, int contribution, int timeSpent) {
        this.type = type;  this.contribution = contribution; this.timeSpent = timeSpent;
    }

    public ActivityType getType() { return type; }
    public int getContribution() { return contribution; }
    public int getTimeSpent() { return timeSpent; }
    public String getNotes() { return notes; }

    public static void main(String[] args) {
        Activity activity = new Activity(ActivityType.READING, 80, 60);
        System.out.println(activity.getTimeSpent());
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public LocalDateTime getEnd() { return end; }
}
