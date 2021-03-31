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
                Exam exam, Assignment assignment, Module module, ArrayList<Task> tasks,
                ArrayList<Milestone> milestones) {
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
    public ArrayList<Task> getDependencies() { return dependencies; }
    public ArrayList<Milestone> getMilestones() { return milestones; }
    public ArrayList<Activity> getActivities() { return relatedActivities; }

    // Setters
    /* User may make mistake when creating it the first time
     * should be altered or current one destroyed and a new one inserted instead?
     */
    private void setTitle(String title) { this.title = title; }
    private void setStart(LocalDateTime start) { this.start = start; }
    private void setEnd(LocalDateTime end) { this.end = end; }
    private void setProgress(int progress) { this.progress = progress; }
    private void setNotes(String notes) { this.notes = notes; }
    private void appendNotes(String notes) { this.notes = this.notes + notes; }
    private void setExam(Exam exam) { this.exam = exam; }
    private void setAssignment(Assignment assignment) { this.assignment = assignment; }
    private void setDependencies(ArrayList<Task> dependencies) { this.dependencies = dependencies; }
    private void setRelatedActivities(ArrayList<Activity> relatedActivities) { this.relatedActivities = relatedActivities; }
    private void setMilestones(ArrayList<Milestone> milestones) { this.milestones = milestones; }

    private void setModule(Module module) { this.module = module; }
    public void updateTask(Task task) {
        this.setTitle(task.getTitle());
        this.setStart(task.getStart());
        this.setEnd(task.getEnd());
        this.setProgress(task.getProgress());
        this.setNotes(task.getNotes());
        this.setNotes(task.getNotes());
        this.setExam(task.getExam());
        this.setAssignment(task.getAssignment());
        this.setModule(task.getModule());
        this.setDependencies(task.getDependencies());
        this.setRelatedActivities(task.getActivities());
        this.setMilestones(task.getMilestones());
    }

    // Add to lists
    public void addDependencies(ArrayList<Task> dependencies) { this.dependencies.addAll(dependencies); }
    public void addMilestones(ArrayList<Milestone> milestones) {this.milestones.addAll(milestones); }
    public void addDependency(Task dependency) { this.dependencies.add(dependency); }
    public void addMilestone(Milestone milestone) { this.milestones.add(milestone); }
    public void addActivity(Activity activity) {
        if(this.relatedActivities == null) {
            this.relatedActivities = new ArrayList<>();
        }
        this.relatedActivities.add(activity);
        this.updateProgress();
    }

    public void updateProgress() {
        /* Does this for all related activities when adding one (doesn't ensure all total to 100 yet)
           Will need to show current % when adding task and preventing over 100
         */
        progress = 0;
        for(Activity activity : relatedActivities) {
            int percentageComplete = activity.getTimeSpent() / 100; // e.g. (30 / 100 = 0.3) 30% complete
            int percentageContribution = activity.getContribution() * percentageComplete; // 30% of the contribution
            this.progress =+ percentageContribution;
        }
    }

    public boolean isComplete() { return this.progress == 100; }

    public static void main(String[] args) {
        // test harness
        // note, intellij alt+ins create test class feature?
    }


}
