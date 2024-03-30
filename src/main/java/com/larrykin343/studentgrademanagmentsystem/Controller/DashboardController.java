package com.larrykin343.studentgrademanagmentsystem.Controller;

import com.larrykin343.studentgrademanagmentsystem.Utils.DatabaseConn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {
    @FXML
    public TreeView<String> leftTreeView;
    @FXML
    public Button logoutMainApplicationButton;
    public ListView<String> dashboardListView;

    public Button studentInfoSaveButton;
    public Button studentInfoCancelButton;

    public TextField studentInfoRegNoTextField;
    public TextField studentInfoNameTextField;
    public TextField studentInfoIdTextField;
    public TextField studentInfoEmailTextField;
    public MenuItem gradeReportMenu;
    public Label studentInfoLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //! creating a tree view in the main application
        TreeItem<String> rootItem = new TreeItem<>("leftTreeView");

        TreeItem<String> branchItem = new TreeItem<>("Students");
        TreeItem<String> branchItem2 = new TreeItem<>("Courses");

        TreeItem<String> leafItem = new TreeItem<>("//:TODO: Add Students");
        branchItem.getChildren().addAll(leafItem);
        TreeItem<String> leafItem2 = new TreeItem<>("//:TODO: Add Courses");
        branchItem2.getChildren().addAll(leafItem2);
        rootItem.getChildren().addAll(branchItem, branchItem2);
        leftTreeView.setShowRoot(false);
        leftTreeView.setRoot(rootItem);

        //!populate the list view
        DatabaseConn connectNow = new DatabaseConn();//Creating an instance of the DatabaseConn class
        Connection connectDB = connectNow.getConnection(); //this is the connection to the database

        try {
            String studentList = "SELECT * FROM students";
            Statement statement = connectDB.createStatement();// creating a statement
            ResultSet resultSet = statement.executeQuery(studentList);//executing the query
            while (resultSet.next()) {
                String studentID = resultSet.getString("id");
                String studentReg = resultSet.getString("reg");
                String studentName = resultSet.getString("name");
                String studentId = resultSet.getString("student_id");
                String email = resultSet.getString("email");

                String studentListOutput = studentID + ".    " + studentReg + "  " + studentName+ "  " +studentId+ "  " +email;
                dashboardListView.getItems().add(studentListOutput);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    //!method to handle the mouse click event when the tree item is clicked
    public void mouseClicked() {// for rightClick use ContextMenuEvent/ for left click use MouseEvent
        TreeItem<String> item = leftTreeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            System.out.println(item.getValue());
        }
    }

    //!method to navigate you to the grade report
    public void gradeReport() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/GradeReport.fxml"));
            Stage gradeReportStage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            gradeReportStage.setScene(scene);
            gradeReportStage.setTitle("Grade Report");
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/digitalLibrary.png")));
            gradeReportStage.getIcons().add(icon);
            gradeReportStage.show();


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    //!method to handle the logout button
    public void logoutMainApplication(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?: ");
        alert.setContentText("Press OK to confirm, or Cancel to go back.");

        if (alert.showAndWait().get().getText().equals("OK")) {
            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.setTitle("Student Grade Management System");
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/digitalLibrary.png")));
                stage.getIcons().add(icon);
                stage.show();
                Stage mainApplicationStage = (Stage) logoutMainApplicationButton.getScene().getWindow();
                mainApplicationStage.close();

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();

            }
        }
    }



    //! method to add students
    public void showAddForm() {
        studentInfoLabel.setText("Enter the student details above and click save to add the student to the database");
        studentInfoSaveButton.setVisible(true);
        studentInfoCancelButton.setVisible(true);
    }

    public void studentInfoSaveButtonOnAction(ActionEvent event) {
        if (!studentInfoRegNoTextField.getText().isBlank() && !studentInfoNameTextField.getText().isBlank()
                && !studentInfoIdTextField.getText().isBlank() && !studentInfoEmailTextField.getText().isBlank()) {
            addStudent();
        } else {

            studentInfoLabel.setText("FILL ALL THE FIELDS");
        }

    }
    public void setStudentInfoCancelButtonOnAction(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel add student");
        alert.setHeaderText("Are you sure you want to cancel Add student: ");
        alert.setContentText("Press OK to confirm, or Cancel to go back.");

        if(alert.showAndWait().get().getText().equals("OK")){
            studentInfoSaveButton.setVisible(false);
            studentInfoCancelButton.setVisible(false);
            studentInfoLabel.setText("");
        }
    }

    public void addStudent() {
        DatabaseConn connectNow = new DatabaseConn();
        Connection connectDB = connectNow.getConnection();

        String regNo = studentInfoRegNoTextField.getText();
        String name = studentInfoNameTextField.getText();
        String id = studentInfoIdTextField.getText();
        String email = studentInfoEmailTextField.getText();


        String insertQuery = "INSERT INTO students (reg, name, student_id, email) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(insertQuery);

            preparedStatement.setString(1, regNo);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, id);
            preparedStatement.setString(4, email);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                studentInfoLabel.setText("");
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Student information added successfully.");

                // Set a timeline to automatically close the alert after 1 second
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> successAlert.close()));
                timeline.setCycleCount(1);
                timeline.play();

                successAlert.showAndWait();
                //?Updating the listview
                updateStudentList();
            } else {
                studentInfoLabel.setText("");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText("Failed to add student information.");
                alert.setContentText("An error occurred while adding student information. Please try again.");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            ex.getCause();
        }
    }

    private void updateStudentList() {
        dashboardListView.getItems().clear();
        DatabaseConn connectNow = new DatabaseConn();//Creating an instance of the DatabaseConn class
        Connection connectDB = connectNow.getConnection(); //this is the connection to the database

        try {
            String studentList = "SELECT * FROM students";
            Statement statement = connectDB.createStatement();// creating a statement
            ResultSet resultSet = statement.executeQuery(studentList);//executing the query
            while (resultSet.next()) {
                String studentID = resultSet.getString("id");
                String studentReg = resultSet.getString("reg");
                String studentName = resultSet.getString("name");
                String studentId = resultSet.getString("student_id");
                String email = resultSet.getString("email");

                String studentListOutput = studentID + ".    " + studentReg + "  " + studentName+ "  " +studentId+ "  " +email;
                dashboardListView.getItems().add(studentListOutput);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }


}
