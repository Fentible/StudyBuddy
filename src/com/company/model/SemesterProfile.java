package com.company.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class SemesterProfile {

    private List<Task> tasks;
    // List<Reminder>
    private List<Deadline> deadlines;
    private List<Activity> activities;
    private List<Exam> exams;
    private List<Assignment> assignments;

    // Constructors
    public SemesterProfile(File profile) throws FileNotFoundException {
        /* Will create the semester profile from the file
         * adding the modules, exams, assignments and deadlines provided.
         * file explanation:
         * first section denotes the type (M - module, E - exam, A - assignment, D - deadline)
         * not all class attributes are loaded from the file such as tasks
         */
        String line, type, code, title;
        LocalDateTime dueDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Scanner read = new Scanner(profile);
        read.useDelimiter(",");
        line = read.nextLine();
        type = read.next();
        switch (type) {
            case "M" -> {
                title = read.next();
                code = read.next();
                System.out.println("Adding module: " + title);
            }
            case "E" -> {
                title = read.next();
                dueDate = LocalDateTime.parse(read.next(), formatter);
                System.out.println("Adding exam: " + title);
            }
            case "A" -> {
                title = read.next();
                dueDate = LocalDateTime.parse(read.next(), formatter);
                System.out.println("Adding assignment: " + title);
            }
            default -> {
                System.out.println("Unknown data type " + read.next());
                read.nextLine();
            }
        }
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

    public static void main(String[] args) {
        // test harness

    }

}
