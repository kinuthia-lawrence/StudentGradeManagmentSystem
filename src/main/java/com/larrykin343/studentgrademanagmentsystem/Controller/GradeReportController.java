package com.larrykin343.studentgrademanagmentsystem.Controller;

import com.larrykin343.studentgrademanagmentsystem.Utils.DatabaseConn;
import com.larrykin343.studentgrademanagmentsystem.Utils.EmailSender;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class GradeReportController implements Initializable {

    public Button closeGradeReportButton;


    @FXML
    private TableView<ObservableList<String>> gradeTableView;

    @FXML
    private TableColumn<ObservableList<String>, String> idColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> regColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> emailColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> gradeColumn;

    DatabaseConn connectNow = new DatabaseConn();
    Connection connectDB = connectNow.getConnection();

    public void exportResults(ActionEvent actionEvent) {
        EmailSender emailSender = new EmailSender();
        emailSender.sendStudentResultsByEmail();
    }

    public void closeWindow(ActionEvent event) {
        closeGradeReportButton.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Statement stmt = connectDB.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, reg, email, finalGrade FROM students_results");

            // Set the cell value factories for each column
            idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
            regColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
            emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
            gradeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));

            // Clear existing items in the TableView
            gradeTableView.getItems().clear();

            // Populate the TableView with data from the ResultSet
            while (rs.next()) {
                ObservableList<String> rowData = FXCollections.observableArrayList();
                rowData.add(rs.getString("id"));
                rowData.add(rs.getString("reg"));
                rowData.add(rs.getString("email"));
                rowData.add(rs.getString("finalGrade"));

                gradeTableView.getItems().add(rowData);
            }

            connectDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
