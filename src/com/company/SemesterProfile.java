package com.company;

import java.util.List;

public class SemesterProfile {

    private List<Task> tasks;
    // List<Reminder>
    private List<Deadline> deadlines;
    private List<Activity> activities;

    public List<Task> getTasks() {
        return this.tasks;
    }

    public List<Deadline> getDeadlines() {
        return this.deadlines;
    }

    public List<Activity> getActivities() {
        return this.activities;
    }

}
