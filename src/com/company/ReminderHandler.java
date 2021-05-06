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
        timer.schedule (timertask,0,TimeUnit.MINUTES.toMillis(1));
    }
    private void checkReminders() {
        if(semesterProfile.getReminders() != null && !semesterProfile.isMuted()) {
            for (Reminder reminder : semesterProfile.getReminders()) {
                if ((reminder.getDate().equals(LocalDate.now()) && LocalTime.now().isAfter(reminder.getTime().minusMinutes(15)) && !reminder.shown)) {
                    //System.out.println("worked");
                    Platform.runLater(() -> ReminderAlertBox.Display(reminder, semesterProfile));
                    reminder.setShown();
                }
            }
        }
    }



}
