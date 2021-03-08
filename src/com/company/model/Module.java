package com.company.model;

import java.util.ArrayList;

public class Module {


    private String type;
    private String code;
    private ArrayList<Task> tasks;
    private ArrayList<Milestone> milestones;
    private ArrayList<Deadline> deadlines;

    /* Constructor used when reading in the file */
    public Module(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public String getType() { return this.type; }
    public String getCode() { return this.code; }
    /* displayGantt() */

}
