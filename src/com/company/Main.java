package com.company;

import com.company.model.SemesterProfile;
import com.company.model.Task;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class Main extends Application {


    private Properties getFileProperties() throws IOException {

        try (InputStream input = new FileInputStream("src/com/company/model/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            Properties properties = new Properties();
            properties.setProperty("location", "src/com/company/model/profile.ser");
            properties.store(new FileWriter("src/com/company/model/config.properties"), "Default file not found, one created");
            return null;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(1000);
        stage.setWidth(1500);
        stage.setResizable(false);
        SemesterProfile semesterProfile = null;
        Properties properties = getFileProperties();
        File inFile;
        if(properties != null) {
            inFile = new File(properties.getProperty("location"));
        } else {
            inFile = new File("src/com/company/model/profile.ser");
        }
        if(inFile.exists()) {
            try {
                System.out.println("Loading saved file");
                FileInputStream fileIn = new FileInputStream(inFile);
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
            System.out.println("New File Being Created");
            File newFile = new File("src/com/company/model/test_semester_profile"); // construct profile
            semesterProfile = new SemesterProfile(newFile, properties);
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
