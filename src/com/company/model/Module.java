package com.company.model;

import java.util.ArrayList;

public class Module {


    private String type;
    private String code;
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Milestone> milestones = new ArrayList<>();
    private ArrayList<Deadline> deadlines = new ArrayList<>();

    /* Constructor used when reading in the file */
    public Module(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public Module(String type, String code, ArrayList<Task> tasks,
                  ArrayList<Milestone> milestones, ArrayList<Deadline> deadlines) {
        this.type = type; this.code = code; this.tasks = tasks;
        this.milestones = milestones; this.deadlines = deadlines;
    }

    public void addTask(Task task) { tasks.add(task); }
    public void addMilestone(Milestone milestone) { milestones.add(milestone); }
    public void addDeadline(Deadline deadline) { deadlines.add(deadline); }

    public String getType() { return this.type; }
    public String getCode() { return this.code; }
    /* displayGantt() */
    public ArrayList<Task> getTasks() { return tasks; }
    public ArrayList<Milestone> getMilestones() { return milestones; }
    public ArrayList<Deadline> getDeadlines() { return deadlines; }


}
