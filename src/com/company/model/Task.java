package com.company.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Task implements CalenderModelClass, Serializable {

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
    private ArrayList<Task> dependencies;
    private ArrayList<Activity> relatedActivities;
    private ArrayList<Milestone> milestones;

    // Constructors
    public Task(String title, String start, String end, int progress, String notes,
                Exam exam, Assignment assignment, Module module, ArrayList<Task> tasks, ArrayList<Milestone> milestones) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.start = LocalDateTime.parse(start, formatter);
        this.end = LocalDateTime.parse(end, formatter);
        this.title = title; this.progress = progress; this.module = module; this.exam = exam;
        this.assignment = assignment; this.dependencies = tasks; this.milestones = milestones; this.notes = notes;
        /* How to handle nulls?
         * Likely a user wont input notes, progress, dependencies or milestones
         * a task may only belong to an assignment or exam, not both
         * should the nulls be passed to the constructor or handled in the calling class?
         */
    }

    // Getters
    public String getTitle() { return this.title; }
    public String getNotes() { return this.notes; }

    // Getters
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public int getProgress() { return progress; }
    public Exam getExam() { return exam; }
    public Assignment getAssignment() { return assignment; }
    public Module getModule() { return module; }
    public List<Task> getDependencies() { return dependencies; }
    public List<Milestone> getMilestones() { return milestones; }

    // Setters
    /* User may make mistake when creating it the first time
     * should be altered or current one destroyed and a new one inserted instead?
     */
    public void setTitle(String title) { this.title = title; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public void setEnd(LocalDateTime end) { this.end = end; }
    public void setProgress(int progress) { this.progress = progress; }
    public void setNotes(String notes) { this.notes = notes; }
    public void appendNotes(String notes) { this.notes = this.notes + notes; }
    public void setExam(Exam exam) { this.exam = exam; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }
    public void setModule(Module module) { this.module = module; }

    // Add to lists
    public void addDependencies(ArrayList<Task> dependencies) { this.dependencies.addAll(dependencies); }
    public void addMilestones(ArrayList<Milestone> milestones) {this.milestones.addAll(milestones); }
    public void addDependency(Task dependency) { this.dependencies.add(dependency); }
    public void addMilestone(Milestone milestone) { this.milestones.add(milestone); }

    public void updateProgress() {
        for(Activity activity : relatedActivities) {
            this.progress =+ activity.getContribution();
        }
    }

    public static void main(String[] args) {
        // test harness
        // note, intellij alt+ins create test class feature?
    }


}
