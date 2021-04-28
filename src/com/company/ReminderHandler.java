package com.company;

import com.company.model.SemesterProfile;
import javafx.application.Platform;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class ReminderHandler {

    private SemesterProfile semesterProfile;

    public void start(SemesterProfile passedSemesterProfile) {
        semesterProfile = passedSemesterProfile;
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                checkReminders();
            }
        };
        Timer timer = new Timer();
        timer.schedule (timertask,0,TimeUnit.MINUTES.toMillis(5));
    }
    private void checkReminders() {
        System.out.println("nope");
        if(semesterProfile.getReminders() != null) {
            for (Reminder reminder : semesterProfile.getReminders()) {
                if ((reminder.getDate().equals(LocalDate.now()) && reminder.getTime().equals(LocalTime.now().minusMinutes(15)) && !reminder.shown) || !reminder.shown) {
                    Platform.runLater(() -> ReminderAlertBox.Display(reminder.getRelatedEvent().getTitle(), reminder.getTime().toString(), semesterProfile.getStyle()));
                    reminder.setShown();
                }
            }
        }
    }



}
