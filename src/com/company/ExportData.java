package com.company;

import com.company.model.*;
import com.company.model.Module;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExportData {

    private static byte flag = 0x00;
    private static File file;

    private static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }

    public static void Display(SemesterProfile semesterProfile) {

        Stage window = new Stage();

        // Block input to other window until this popup is closed
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Export Data");
        window.setMinWidth(350);
        window.setMinHeight(250);
        Label label = new Label("Please select which datasets you which to export");
        Label exportLocation = new Label();
        Button closeButton = new Button("Close");
        Button exportButton = new Button("Export");
        CheckBox exportTasks = new CheckBox("Tasks");
        CheckBox exportMilestones = new CheckBox("Milestones");
        CheckBox exportActivities = new CheckBox("Activities");
        CheckBox exportDeadlines = new CheckBox("Deadlines");
        Button modulePDF = new Button("Module PDFs");

        modulePDF.setOnAction(e -> {
            for(Module module : semesterProfile.getModules()) {
                String pdfSaveLocation ="";
                if(file != null) {
                    pdfSaveLocation = file.getAbsolutePath() + "\\" + module.getTitle() + ".pdf";
                } else {
                    pdfSaveLocation = module.getTitle() + ".pdf";
                }
                System.out.println(pdfSaveLocation);
                Document document = new Document();
                ModuleCharts.exportChartNoShow(ModuleCharts.createGanttChartNoShow(module));
                try {
                    File outFile = new File(pdfSaveLocation);
                    Files.deleteIfExists(outFile.toPath());
                    PdfWriter.getInstance(document, new FileOutputStream(pdfSaveLocation));
                    module.exportDataCVS();
                    //CSVReader csvReader = new CSVReader(new FileReader(module.getTitle() + ".csv"));
                } catch (DocumentException | IOException ex) {
                    ex.printStackTrace();
                }
                document.open();
                PdfPTable imageTable = new PdfPTable(1);
                imageTable.setWidthPercentage(100);
                PdfPTable tasksTable = new PdfPTable(5);
                PdfPTable milestonesTable = new PdfPTable(4);
                PdfPTable deadlinesTable = new PdfPTable(2);

                tasksTable.addCell("Title");
                tasksTable.addCell("Progress");
                tasksTable.addCell("Due date");
                tasksTable.addCell("Dependencies");
                tasksTable.addCell("Milestones");
                tasksTable.setHeaderRows(1);
                ArrayList<Task> taskList = new ArrayList<>();
                Optional.ofNullable(module.getTasks()).ifPresent(taskList::addAll);
                for(Task task : taskList) {
                    tasksTable.addCell(task.getTitle());
                    tasksTable.addCell(Integer.toString(task.getProgress()));
                    tasksTable.addCell(task.getEnd().toString());
                    tasksTable.addCell(task.getDependencies() == null ? "" : task.getDependencies().stream().map(Task::getTitle).collect(Collectors.joining()));
                    tasksTable.addCell(task.getMilestones() == null ? "" : task.getMilestones().stream().map(Milestone::getTitle).collect(Collectors.joining()));
                }
                milestonesTable.addCell(new Paragraph("Milestones"));
                milestonesTable.addCell("Title");
                milestonesTable.addCell("Completion");
                milestonesTable.addCell("Due date");
                milestonesTable.addCell("Required Tasks");
                milestonesTable.setHeaderRows(1);
                ArrayList<Milestone> milestoneList = new ArrayList<>();
                Optional.ofNullable(module.getMilestones()).ifPresent(milestoneList::addAll);
                for(Milestone milestone : milestoneList) {
                    milestonesTable.addCell(milestone.getTitle());
                    milestonesTable.addCell(Integer.toString(milestone.getCompletion()));
                    milestonesTable.addCell(milestone.getEnd().toString());
                    milestonesTable.addCell(milestone.getRequiredTasks() == null ? "" : milestone.getRequiredTasks().stream().map(Task::getTitle).collect(Collectors.joining()));
                }
                deadlinesTable.addCell("Title");
                deadlinesTable.addCell("Due date");
                deadlinesTable.setHeaderRows(1);
                ArrayList<Deadline> deadlineList = new ArrayList<>();
                Optional.ofNullable(module.getDeadlines()).ifPresent(deadlineList::addAll);
                for(Deadline deadline : deadlineList) {
                    deadlinesTable.addCell(deadline.getTitle());
                    deadlinesTable.addCell(deadline.getEnd().toString());
                }
                imageTable.setSpacingAfter(15f);
                tasksTable.setSpacingAfter(15f);
                deadlinesTable.setSpacingAfter(15f);
                milestonesTable.setSpacingAfter(15f);
                try {
                    imageTable.addCell(createImageCell(module.getTitle() + "_Chart.png"));
                    document.add(imageTable);
                    document.add(tasksTable);
                    document.add(milestonesTable);
                    document.add(deadlinesTable);
                } catch (DocumentException | IOException ex) {
                    ex.printStackTrace();
                }

                document.close();
            }
        });
        HBox buttons = new HBox(8);
        buttons.setAlignment(Pos.CENTER);

        DirectoryChooser exportDirectory = new DirectoryChooser();
        File initialDir = new File("src/com/company");
        exportDirectory.setInitialDirectory(initialDir);
        Button directoryButton = new Button("Choose Directory");
        directoryButton.setOnAction(e -> {
            file = exportDirectory.showDialog(window);
            exportLocation.setText(file == null ? "" : file.getAbsolutePath());
        });
        buttons.getChildren().addAll(closeButton, directoryButton, exportButton, modulePDF);
        VBox exportOptions = new VBox(8);
        exportOptions.setAlignment(Pos.CENTER_LEFT);
        exportOptions.setPadding(new Insets(15, 0, 15, 125));
        exportOptions.getChildren().addAll(exportTasks, exportMilestones, exportDeadlines, exportActivities);

        closeButton.setOnAction(event -> window.close());
        exportButton.setOnAction(e -> {
            if(exportTasks.isSelected()) { flag |= 1 << 0; }
            if(exportMilestones.isSelected()) { flag |= 1 << 1; }
            if(exportDeadlines.isSelected()) { flag |= 1 << 2; }
            if(exportActivities.isSelected()) { flag |= 1 << 3; }
           // System.out.println(Byte.toString(flag));
            if(flag != 0x00) {
                try {
                    semesterProfile.exportDataCVS(flag, file == null ? exportDirectory.getInitialDirectory().getAbsolutePath() : file.getAbsolutePath());
                    flag = 0x00;
                } catch (IOException ioException) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to access file for exporting" +
                            " please ensure it is not open in another application");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, label.getText());
                alert.showAndWait();
            }
        });


        VBox layout = new VBox(10);
        layout.getChildren().addAll(exportOptions, buttons, exportLocation, label);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(semesterProfile.getStyle());
        scene.setUserAgentStylesheet(semesterProfile.getStyle());
        window.setScene(scene);
        window.showAndWait();

    }
}
