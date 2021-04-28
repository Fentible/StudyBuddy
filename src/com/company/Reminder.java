package com.company;

import com.company.model.CalenderModelClass;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Reminder implements Serializable {

    private CalenderModelClass relatedEvent;
    private LocalDate date;
    private LocalTime time;
    public boolean shown;

    public Reminder(CalenderModelClass relatedEvent) {
        this.relatedEvent = relatedEvent;
        this.date = relatedEvent.getEnd().toLocalDate();
        this.time = relatedEvent.getEnd().toLocalTime();
    }

    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public CalenderModelClass getRelatedEvent() { return relatedEvent; }

    public void setShown() {
        this.shown = true;
    }
}

