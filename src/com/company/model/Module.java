package com.company.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Module implements Serializable {


    private String title;
    private String code;
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Milestone> milestones = new ArrayList<>();
    private ArrayList<Deadline> deadlines = new ArrayList<>();

    /* Constructor used when reading in the file */
    public Module(String type, String code) {
        this.title = type;
        this.code = code;
    }

    public Module(String type, String code, ArrayList<Task> tasks,
                  ArrayList<Milestone> milestones, ArrayList<Deadline> deadlines) {
        this.title = type; this.code = code; this.tasks = tasks;
        this.milestones = milestones; this.deadlines = deadlines;
    }

    public void addTask(Task task) { tasks.add(task); }
    public void addMilestone(Milestone milestone) { milestones.add(milestone); }
    public void addDeadline(Deadline deadline) { deadlines.add(deadline); }

    public String getTitle() { return this.title; }
    public String getCode() { return this.code; }
    /* displayGantt() */
    public ArrayList<Task> getTasks() { return tasks; }
    public ArrayList<Milestone> getMilestones() { return milestones; }
    public ArrayList<Deadline> getDeadlines() { return deadlines; }
    public ArrayList<Exam> getExams() {
        ArrayList<Exam> exams = new ArrayList<>();
        for(Deadline deadline : deadlines) {
            if(deadline instanceof Exam) {
                exams.add((Exam) deadline);
            }
        }
        return exams;
    }
    public ArrayList<Assignment> getAssignments() {
        ArrayList<Assignment> assignments = new ArrayList<>();
        for(Deadline deadline : deadlines) {
            if(deadline instanceof Assignment) {
                assignments.add((Assignment) deadline);
            }
        }
        return assignments;
    }


}
