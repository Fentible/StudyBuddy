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

    // Constructors
    public Task(String title, LocalDateTime start, LocalDateTime end, int progress, String notes,
                Exam exam, Assignment assignment, Module module, List<Task> tasks, List<Milestone> milestones) {
        this.title = title; this.start = start; this.end = end; this.progress = progress; this.module = module;
        this.exam = exam; this.assignment = assignment; this.dependencies = tasks; this.milestones = milestones;
        this.notes = notes;
        /* How to handle nulls?
         * Likely a user wont input notes, progress, dependencies or milestones
         * a task may only belong to an assignment or exam, not both
         * should the nulls be passed to the constructor or handled in the calling class?
         */
    }

    // Getters
    public String getTitle() { return this.title; }
    public String getNotes() { return this.notes; }




}
