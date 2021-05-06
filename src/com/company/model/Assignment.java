package com.company.model;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

/*
 * Loaded in from the semester file
 * not described much on our documentation
 */
public class Assignment extends Deadline {



    public Assignment(String title, String dueDate, Module module, int weighting) { super(dueDate, title, module, weighting); }
    public static void main(String[] args) {

        //Assignment assignment = new Assignment("SoftEng Report", "13-06-2021 12:00");
        //System.out.println(assignment.getDueDate());
    }

}
