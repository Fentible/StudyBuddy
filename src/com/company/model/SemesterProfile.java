package com.company.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class SemesterProfile {

    private ArrayList<Task> tasks = new ArrayList<>();
    // List<Reminder>
    private ArrayList<Deadline> deadlines = new ArrayList<>();
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<Exam> exams = new ArrayList<>();
    private ArrayList<Assignment> assignments = new ArrayList<>();
    private ArrayList<Module> modules = new ArrayList<>();

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Scanner read = new Scanner(profile);

        while(read.hasNextLine()) {
            line = read.nextLine();
            Scanner readLine = new Scanner(line);
            readLine.useDelimiter(",");
            type = readLine.next();
            System.out.println(type);
            switch (type) {
                case "M" -> {
                    title = readLine.next();
                    code = readLine.next();
                    this.addModule(new Module(title, code));
                    System.out.println("Adding module: " + title);
                }
                case "E" -> {
                    title = readLine.next();
                    dueDate = LocalDateTime.parse(readLine.next(), formatter);
                    this.addExam(new Exam(title, dueDate));
                    System.out.println("Adding exam: " + title);
                }
                case "A" -> {
                    title = readLine.next();
                    dueDate = LocalDateTime.parse(readLine.next(), formatter);
                    this.addAssignment(new Assignment(title, dueDate));
                    System.out.println("Adding assignment: " + title);
                }
                default -> {
                    System.out.println("Unknown data type " + readLine.next());

                }
            }
        }
    }



    // Getters
    public ArrayList<Task> getTasks() { return this.tasks; }
    public ArrayList<Deadline> getDeadlines() { return this.deadlines; }
    public ArrayList<Activity> getActivities() { return this.activities; }
    public ArrayList<Module> getModules() { return this.modules; }

    /* Adding an element to the appropriate list */
    public void addDeadline(Deadline deadline) { deadlines.add(deadline); }
    public void addActivity(Activity activity) { activities.add(activity); }
    public void addTask(Task task) { tasks.add(task); }
    /* Possibly only used when constructing -
     * should user be able to add new exams, wouldn't the hub know and provide all of them
     */
    private void addExam(Exam exam) { exams.add(exam); }
    private void addAssignment(Assignment assignment) { assignments.add(assignment); }
    private void addModule(Module module) { modules.add(module); }


    public static void main(String[] args) throws FileNotFoundException {
        // test harness
        File file = new File("src/com/company/model/test_semester_profile");
        SemesterProfile semesterProfile = new SemesterProfile(file);
        for(Module module : semesterProfile.getModules()) {
            System.out.println(module.getCode());
        }
    }

}
