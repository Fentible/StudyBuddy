package com.company.model;

import java.time.LocalDateTime;


/*
 * Used to allow the model classes that implement it to use the same method for displaying on the calender
 */
public interface CalenderModelClass {

    String getTitle();
    LocalDateTime getEnd();
}
