package com.company.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* primarily for handling and storing data, no view or controller is necessary
 * other classes shall query, insert and delete data then provide views appropriately
 */
public class SemesterProfile {

    private ArrayList<Task> tasks = new ArrayList<>();
    // private ArrayList<Reminder> reminders = new ArrayList<>();
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<Module> modules = new ArrayList<>();
    /* although exam and assignments extends deadlines it may be beneficial to store them separately
     * from generic deadlines for the purpose of searching and displaying them
     */
    private ArrayList<Deadline> deadlines = new ArrayList<>();
    private ArrayList<Exam> exams = new ArrayList<>();
    private ArrayList<Assignment> assignments = new ArrayList<>();

    // Constructors
    public SemesterProfile(File profile) throws FileNotFoundException, InvalidParameterException {
        /* Will create the semester profile from the file
         * adding the modules, exams, assignments and deadlines provided.
         * file explanation:
         * first section denotes the type (M - module, E - exam, A - assignment, D - deadline)
         * not all class attributes are loaded from the file such as tasks
         * this current method requires all deadlines relating to modules to come afterwards rather
         */
        String line, type, title;
        String code = null;
        LocalDateTime dueDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Scanner read = new Scanner(profile);

        while(read.hasNextLine()) {
            line = read.nextLine();
            Scanner readLine = new Scanner(line);
            readLine.useDelimiter(",");
            type = readLine.next();
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
                    Exam exam = new Exam(dueDate, title);
                    this.addExam(exam);
                    this.getModule(code).addDeadline(exam);
                    System.out.println("Adding exam: " + title);
                }
                case "A" -> {
                    title = readLine.next();
                    dueDate = LocalDateTime.parse(readLine.next(), formatter);
                    Assignment assignment = new Assignment(title, dueDate);
                    this.addAssignment(assignment);
                    this.getModule(code).addDeadline(assignment);
                    System.out.println("Adding assignment: " + title);
                }
                default -> {
                    // System.out.println("Unknown data type " + readLine.next());
                    /* throw an exception if there is unknown data and shouldn't create profile
                     * the creating class (possibly main) can then catch and handle it with a popup
                     * to the user
                     */
                    throw new InvalidParameterException("Unknown data type " + readLine.next()
                            + " generating profile has failed");
                }
            }
        }
    }


    // Getters
    /*
     * Generic getters for retrieving attributes
     */
    public ArrayList<Task> getTasks() { return this.tasks; }
    public ArrayList<Deadline> getDeadlines() { return this.deadlines; }
    public ArrayList<Activity> getActivities() { return this.activities; }
    public ArrayList<Module> getModules() { return this.modules; }

    /* Collection of functions for finding  from a list given a filter
     * e.g. find all tasks that have a specific date
     */
    public Task getTask(String title) {
        Pattern pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
        for(Task task : tasks) {
            Matcher matcher = pattern.matcher(task.getTitle());
            boolean found = matcher.find();
            if(found) {
                return task;
            }
        }
        return null;
    }

    public Module getModule(String code) {
        Pattern pattern = Pattern.compile(code, Pattern.CASE_INSENSITIVE);
        for(Module mod : modules) {
            Matcher matcher = pattern.matcher(mod.getCode());
            boolean found = matcher.find();
            if(found) {
                return mod;
            }
        }
        return null;
    }




    // Setters/Adders
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

    // Removers
    /* how to implement?
     * currently: user searches for object, selects then asks to delete
     */
    private boolean removeTask(Task task) { return this.tasks.remove(task); }

    // Helpers



    public static void main(String[] args) throws FileNotFoundException {
        // test harness
        // File reading and construction
        File file = new File("src/com/company/model/test_semester_profile"); // construct profile
        SemesterProfile semesterProfile = new SemesterProfile(file);
        for(Module module : semesterProfile.getModules()) { // check file is read and inputted correctly
            System.out.println("Type " + module.getCode());
            System.out.println(" Code " + module.getType());
        }
        // Add, get, remove
        semesterProfile.addTask(new Task("task1", null, null, 0, "This be notes", null,
                null, null, null, null));
        System.out.println(semesterProfile.getTask("tas").getNotes());
        semesterProfile.removeTask(semesterProfile.getTask("tas"));
        if(semesterProfile.getTask("tas") == null)
            System.out.println("Task not found");
    }

}
