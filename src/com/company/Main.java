package com.company;

import com.company.model.SemesterProfile;
import com.company.model.Task;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(1000);
        stage.setWidth(1500);
        stage.setResizable(false);
        SemesterProfile semesterProfile = null;
        File inFile = new File("src/com/company/model/profile.ser");
        if(inFile.exists()) {
            try {
                FileInputStream fileIn = new FileInputStream("src/com/company/model/profile.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                semesterProfile = (SemesterProfile) in.readObject();
                in.close();
                fileIn.close();
            } catch (IOException i) {
                i.printStackTrace();
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();
                return;
            }
        } else { // If not save is found, try to find semester profile
            // Will allow user to load file and specify location from dashboard or splash screen eventually
            File newFile = new File("src/com/company/model/test_semester_profile"); // construct profile
            semesterProfile = new SemesterProfile(newFile);
            semesterProfile.addTask(new Task("Title", "15-04-2021 16:00", "15-04-2021 16:00",
                    0, "", null, null, null, null, null));
            semesterProfile.addTask(new Task("Title", "16-04-2021 16:00", "16-04-2021 16:00",
                    0, "", null, null, null, null, null));
        }
        Dashboard dashboard = new Dashboard(semesterProfile);
        dashboard.start(stage);

    }

    public static void main(String[] args) throws FileNotFoundException { launch(args); }
}
