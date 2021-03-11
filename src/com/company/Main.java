package com.company;

import com.company.model.SemesterProfile;
import com.company.model.Task;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        stage.setHeight(1000);
        stage.setWidth(1500);
        stage.setResizable(false);
        File file = new File("src/com/company/model/test_semester_profile"); // construct profile
        SemesterProfile semesterProfile = new SemesterProfile(file);
        semesterProfile.addTask(new Task("Title", "15-04-2021 16:00", "15-04-2021 16:00",
                0, "", null, null, null, null, null));
        semesterProfile.addTask(new Task("Title2", "15-04-2021 16:00", "15-04-2021 16:00",
                0, "", null, null, null, null, null));
        semesterProfile.addTask(new Task("Title", "16-04-2021 16:00", "16-04-2021 16:00",
                0, "", null, null, null, null, null));
        Dashboard dashboard = new Dashboard(semesterProfile);
        dashboard.start(stage);

    }

    public static void main(String[] args) throws FileNotFoundException { launch(args); }
}
