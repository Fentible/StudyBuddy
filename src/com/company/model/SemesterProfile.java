package com.company.model;

import com.company.Dashboard;

import java.io.*;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* primarily for handling and storing data, no view or controller is necessary
 * other classes shall query, insert and delete data then provide views appropriately
 */
public class SemesterProfile implements Serializable {

    private ArrayList<Task> tasks = new ArrayList<>();
    // private ArrayList<Reminder> reminders = new ArrayList<>();
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<Module> modules = new ArrayList<>();
    private ArrayList<Milestone> milestones = new ArrayList<>();
    /* although exam and assignments extends deadlines it may be beneficial to store them separately
     * from generic deadlines for the purpose of searching and displaying them
     */
    // exams and assignments are subclasses of deadline

    private ArrayList<Exam> exams = new ArrayList<>();
    private ArrayList<Assignment> assignments = new ArrayList<>();
    private ArrayList<Deadline> deadlines = new ArrayList<>();
    private String saveFileLocation = "src/com/company/model/profile.ser";
    private Properties properties = new Properties();
    @Serial
    private static final long serialVersionUID = 6529685098267757690L;


    // Constructors
    public SemesterProfile(File profile, Properties properties) throws IOException, InvalidParameterException {
        /* Will create the semester profile from the file
         * adding the modules, exams, assignments and deadlines provided.
         * file explanation:
         * first section denotes the type (M - module, E - exam, A - assignment, D - deadline)
         * not all class attributes are loaded from the file such as tasks
         * this current method requires all deadlines relating to modules to come afterwards rather
         */
        String line, type, title;
        String code = null;
        Scanner read = new Scanner(profile);
        Module module = null;
        this.properties = properties;
        while(read.hasNextLine()) {
            line = read.nextLine();
            Scanner readLine = new Scanner(line);
            readLine.useDelimiter(",");
            type = readLine.next();
            switch (type) {
                case "M" -> {
                    title = readLine.next();
                    code = readLine.next();
                    module = new Module(title, code);
                    this.addModule(module);
                    System.out.println("Adding module: " + title);
                }
                case "E" -> {
                    title = readLine.next();
                    Exam exam = new Exam(title, readLine.next(), module);
                    this.addExam(exam);
                    this.getModule(code).addDeadline(exam);
                    System.out.println("Adding exam: " + title);
                }
                case "A" -> {
                    title = readLine.next();
                    Assignment assignment = new Assignment(title, readLine.next(), module);
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
    public ArrayList<Milestone> getMilestones() { return this.milestones; }
    public ArrayList<Exam> getExams() { return this.exams; }
    public ArrayList<Assignment> getAssignments() { return assignments; }
    public ArrayList<Deadline> getDeadlines() {
            return (ArrayList<Deadline>) Stream.concat(exams.stream(), assignments.stream()).collect(Collectors.toList());
    }
    public ArrayList<Activity> getActivities() { return this.activities; }
    public ArrayList<Module> getModules() { return this.modules; }
    public String getSaveFileLocation() { return this.saveFileLocation; }

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

    public ArrayList<CalenderModelClass> getAll() {
        ArrayList<CalenderModelClass> calenderModelClasses = new ArrayList<>();
        calenderModelClasses.addAll(this.activities);
        calenderModelClasses.addAll(this.getDeadlines());
        calenderModelClasses.addAll(this.milestones);
        calenderModelClasses.addAll(this.tasks);
        return calenderModelClasses;
    }
    public ArrayList<Task> getTasks(String title) {
        Pattern pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
        ArrayList<Task> tasks = new ArrayList<>();
        for(Task task : this.tasks) {
            Matcher matcher = pattern.matcher(task.getTitle());
            boolean found = matcher.find();
            if(found) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    public ArrayList<Milestone> getMilestones(String title) {
        Pattern pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
        ArrayList<Milestone> milestones = new ArrayList<>();
        for(Milestone milestone: this.milestones) {
            Matcher matcher = pattern.matcher(milestone.getTitle());
            boolean found = matcher.find();
            if(found) {
                milestones.add(milestone);
            }
        }
        return milestones;
    }

    public ArrayList<Deadline> getDeadlines(String title) {
        Pattern pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
        ArrayList<Deadline> deadlines = new ArrayList<>();
        for(Deadline deadline : this.getDeadlines()) {
            Matcher matcher = pattern.matcher(deadline.getTitle());
            boolean found = matcher.find();
            if(found) {
                deadlines.add(deadline);
            }
        }
        return deadlines;
    }

    public ArrayList<Activity> getActivities(String title) {
        Pattern pattern = Pattern.compile(title, Pattern.CASE_INSENSITIVE);
        ArrayList<Activity> activities = new ArrayList<>();
        for(Activity activity : this.activities) {
            Matcher matcher = pattern.matcher(activity.getTitle());
            boolean found = matcher.find();
            if(found) {
                activities.add(activity);
            }
        }
        return activities;
    }

    public ArrayList<Task> getTasksFromDate(LocalDate start, LocalDate end) {
        ArrayList<Task> tasks = new ArrayList<>();
        for(Task task : this.tasks) {
            if(task.getEnd().toLocalDate().isAfter(start) && task.getEnd().toLocalDate().isBefore(end)) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    public ArrayList<Deadline> getDeadlinesFromDate(LocalDate start, LocalDate end) {
        ArrayList<Deadline> deadlines = new ArrayList<>();
        for(Deadline deadline : this.getDeadlines()) {
            if(deadline.getEnd().toLocalDate().isAfter(start) && deadline.getEnd().toLocalDate().isBefore(end)) {
                deadlines.add(deadline);
            }
        }
        return deadlines;
    }

    public ArrayList<Milestone> getMilestonesFromDate(LocalDate start, LocalDate end) {
        ArrayList<Milestone> milestones = new ArrayList<>();
        for(Milestone milestone : this.milestones) {
            if(milestone.getEnd().toLocalDate().isAfter(start) && milestone.getEnd().toLocalDate().isBefore(end)) {
                milestones.add(milestone);
            }
        }
        return milestones;
    }

    public ArrayList<Activity> getActivitiesFromDate(LocalDate start, LocalDate end) {
        ArrayList<Activity> activities = new ArrayList<>();
        for(Activity activity : this.activities) {
            if(activity.getEnd().toLocalDate().isAfter(start) && activity.getEnd().toLocalDate().isBefore(end)) {
                activities.add(activity);
            }
        }
        return activities;
    }

    public ArrayList<Task> getTasksFromDate(LocalDate date) {
        ArrayList<Task> tasks = new ArrayList<>();
        for(Task task : this.tasks) {
            if(task.getEnd().toLocalDate().compareTo(date) == 0) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    public ArrayList<Deadline> getDeadlinesFromDate(LocalDate date) {
        ArrayList<Deadline> deadlines = new ArrayList<>();
        for(Deadline deadline : this.getDeadlines()) {
            if(deadline.getEnd().toLocalDate().compareTo(date) == 0) {
                deadlines.add(deadline);
            }
        }
        return deadlines;
    }

    public ArrayList<Milestone> getMilestonesFromDate(LocalDate date) {
        ArrayList<Milestone> milestones = new ArrayList<>();
        for(Milestone milestone : this.milestones) {
            if(milestone.getEnd().toLocalDate().compareTo(date) == 0) {
                milestones.add(milestone);
            }
        }
        return milestones;
    }

    public ArrayList<Activity> getActivitiesFromDate(LocalDate date) {
        ArrayList<Activity> activities = new ArrayList<>();
        for(Activity activity : this.activities) {
            if(activity.getEnd().toLocalDate().compareTo(date) == 0) {
                activities.add(activity);
            }
        }
        return activities;
    }

    public ArrayList<Deadline> getModuleDeadlines(Module module) {
        for(Module module1 : modules) {
            if(module == module1) {
                return module1.getDeadlines();
            }
        }
        return null;
    }
    public ArrayList<Exam> getModuleExams(Module module) {
        for(Module module1 : modules) {
            if(module == module1) {
                return module1.getExams();
            }
        }
        return null;
    }

    public ArrayList<Assignment> getModuleAssignments(Module module) {
        for(Module module1 : modules) {
            if(module == module1) {
                return module1.getAssignments();
            }
        }
        return null;
    }

    public ArrayList<Task> getAssignedTasks(Deadline deadline) {
        ArrayList<Task> taskList = new ArrayList<>();
        for(Task task : tasks) {
            if(deadline instanceof Assignment && task.getAssignment() == deadline) {
                taskList.add(task);
            } else if(deadline instanceof Exam && task.getExam() == deadline) {
                taskList.add(task);
            }
        }
        return taskList;
    }



    /*
     * Probably a better way of doing this
     */
    public ArrayList<CalenderModelClass> getItemsFromDate(LocalDate date, CalenderDisplayType type) {
        ArrayList<CalenderModelClass> items = new ArrayList<>();
        if(type == CalenderDisplayType.ACTIVITIES) {
            for (Activity temp : this.activities) {
                if (temp.getEnd().toLocalDate().compareTo(date) == 0) {
                    items.add(temp);
                }
            }
        } else if(type == CalenderDisplayType.DEADLINES) {
            for (Deadline temp : this.getDeadlines()) {
                if (temp.getEnd().toLocalDate().compareTo(date) == 0) {
                    items.add(temp);
                }
            }
        } else if(type == CalenderDisplayType.MILESTONES) {
            for (Milestone temp : this.milestones) {
                if (temp.getEnd().toLocalDate().compareTo(date) == 0) {
                    items.add(temp);
                }
            }
        } else if(type == CalenderDisplayType.TASKS){
            for (Task temp : this.tasks) {
                if (temp.getEnd().toLocalDate().compareTo(date) == 0) {
                    items.add(temp);
                }
            }
        }
        return items;
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
    public void addMilestone(Milestone milestone) { milestones.add(milestone); }

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
    public void saveFile() {
        try {
            FileOutputStream fileOut = new FileOutputStream("src/com/company/model/profile.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            this.saveFileLocation = "src/com/company/model";
            this.properties.setProperty("location", "src/com/company/model/profile.ser");
            updatePropertiesFile();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void saveFile(String location) {

        try {
            System.out.println(location);
            FileOutputStream fileOut = new FileOutputStream(location);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            this.saveFileLocation = location;
            this.properties.setProperty("location", location);
            updatePropertiesFile();
        } catch (IOException i) {
            System.out.println("Save failed!" + " : " + location);
            i.printStackTrace();
        }
    }

    public void updatePropertiesFile() throws FileNotFoundException {
        try (OutputStream outputStream = new FileOutputStream("src/com/company/model/config.properties")) {
            this.properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws IOException {
        // test harness
        // File reading and construction
        File file = new File("src/com/company/model/test_semester_profile"); // construct profile
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/com/company/model/config.properties"));
        SemesterProfile semesterProfile = new SemesterProfile(file, properties);
        for(Module module : semesterProfile.getModules()) { // check file is read and inputted correctly
            System.out.println("Type " + module.getCode());
            System.out.println(" Code " + module.getTitle());
        }
        // Add, get, remove
        semesterProfile.addTask(new Task("task1", "18-04-2021 16:00", "18-04-2021 16:00", 0,
                "This be notes", null, null, null, null, null));

        System.out.println(semesterProfile.getTask("tas").getNotes());
        // semesterProfile.removeTask(semesterProfile.getTask("tas"));
        if(semesterProfile.getTask("tas") == null)
            System.out.println("Task not found");
        Dashboard dashboard = new Dashboard(semesterProfile);
        List<LocalDate> dates = dashboard.getDates(LocalDate.of(2021, 4, 11), LocalDate.of(2021, 4, 19));

       ArrayList<Task> tasks = semesterProfile.getTasksFromDate(dates.get(dates.size() - 1));
       for(Task task : tasks) {
           System.out.println(task.getTitle());
       }

    }

}
