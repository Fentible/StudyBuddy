package com.company.model;

import java.time.LocalDateTime;
import java.util.List;

public class Task {

    // 'Generic' attributes
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private int progress;
    private String notes;
    // add Reminder

    // Related package classes
    private Exam exam;
    private Assignment assignment;
    private Module module;
    private List<Task> dependencies;
    private List<Milestone> milestones;


}
