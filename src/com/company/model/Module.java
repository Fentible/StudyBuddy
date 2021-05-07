package com.company.model;

import com.opencsv.CSVWriter;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.util.ArrayList;

public class Module implements Serializable {


    private final String title;
    private final String code;
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

    public void exportDataCVS() throws IOException {

        File outFile = new File(this.getTitle() + "\\out.csv");
        try {
            Files.deleteIfExists(outFile.toPath());
        } catch (FileSystemException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to access file for exporting" +
                    " please ensure it is not open in another application");
            alert.showAndWait();
            return;
        }
        CSVWriter outWriter = new CSVWriter(new FileWriter(this.getTitle() + ".csv"));
        outWriter.writeNext(new String[] {"Tasks:"});
        outWriter.writeNext(new String[]{"Task Title", "Module Code", "Start Date", "End Date",
                "Progress", "Dependencies", "Milestones" });
        for (Task task : tasks) {
            outWriter.writeNext(task.toCSV());
        }
        outWriter.writeNext(new String[] {"Milestones:"});
        outWriter.writeNext(new String[]{"Milestone Title", "Related event title", "End date", "Completion",
                "Required Tasks"});
        for (Milestone milestone : milestones) {
            outWriter.writeNext(milestone.toCSV());
        }
        outWriter.writeNext(new String[] {"Deadlines:"});
        outWriter.writeNext(new String[]{"Deadline Title", "Module code", "End date"});
        for (Deadline deadline : this.getDeadlines()) {
            outWriter.writeNext(deadline.toCSV());
        }

        outWriter.close();
    }



}
