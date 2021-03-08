package com.company.model;

import java.io.File;
import java.util.List;

public class SemesterProfile {

    private List<Task> tasks;
    // List<Reminder>
    private List<Deadline> deadlines;
    private List<Activity> activities;
    private List<Exam> exams;
    private List<Assignment> assignments;

    // Constructors
    public SemesterProfile(File profile) {
        /* Will create the semester profile from the file
         * adding the exams, modules, assignments and deadlines provided.
         */
    }

    // Getters
    public List<Task> getTasks() { return this.tasks; }
    public List<Deadline> getDeadlines() { return this.deadlines; }
    public List<Activity> getActivities() { return this.activities; }

    /* Adding an element to the appropriate list */
    public void addDeadline(Deadline deadline) { deadlines.add(deadline); }
    public void addActivity(Activity activity) { activities.add(activity); }
    public void addTask(Task task) { tasks.add(task); }
    /* Possibly only used when constructing -
     * should user be able to add new exams, wouldn't the hub know and provide all of them
     */
    private void addExam(Exam exam) { exams.add(exam); }
    private void addAssignments(Assignment assignment) { assignments.add(assignment); }

}
