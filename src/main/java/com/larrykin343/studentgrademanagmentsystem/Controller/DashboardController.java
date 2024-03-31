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
    public boolean isAddStudent = false;
    public boolean isUpdateStudent = false;
    public boolean isDeleteStudent = false;

    private TreeItem<String> branchItem;

    //?Method to change the text of the button
    public void changeButtonText(String newText) {
        studentInfoSaveButton.setText(newText);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //! creating a tree view in the main application
        TreeItem<String> rootItem = new TreeItem<>("leftTreeView");

        branchItem = new TreeItem<>("Students");
        TreeItem<String> branchItem2 = new TreeItem<>("Courses");


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

                String studentListOutput = studentID + ".    " + studentReg + "  " + studentName + "  " + studentId + "  " + email;
                dashboardListView.getItems().add(studentListOutput);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        //!populate the tree view
        try {
            String studentListQuery = "SELECT * FROM students"; // Query to retrieve all columns from students table
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(studentListQuery);

            while (resultSet.next()) {
                String studentID = resultSet.getString("id");
                String studentReg = resultSet.getString("reg");
                String studentName = resultSet.getString("name");
                String studentGrade = resultSet.getString("grade");
                TreeItem<String> studentItem = new TreeItem<>(studentID + "." + studentReg + " " + studentName + " " + studentGrade);
                branchItem.getChildren().add(studentItem); // Add each student as a child to branchItem
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            ex.getCause();
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


    //! METHOD TO ADD STUDENT
    public void showAddForm() {
        studentInfoLabel.setText("Enter the student details above and click \"Save\" to save the student to the database");
        studentInfoSaveButton.setVisible(true);
        studentInfoCancelButton.setVisible(true);
        isAddStudent = true;
        isUpdateStudent = false;
        isDeleteStudent = false;
    }


    public void studentInfoSaveButtonOnAction(ActionEvent event) {
        if (isAddStudent) {
            if (!studentInfoRegNoTextField.getText().isBlank() && !studentInfoNameTextField.getText().isBlank()
                    && !studentInfoIdTextField.getText().isBlank() && !studentInfoEmailTextField.getText().isBlank()) {
                addStudent();

            } else {

                studentInfoLabel.setText("FILL ALL THE FIELDS");
            }
        }else if (isUpdateStudent){
            if (!studentInfoRegNoTextField.getText().isBlank() && !studentInfoNameTextField.getText().isBlank()
                    && !studentInfoIdTextField.getText().isBlank() && !studentInfoEmailTextField.getText().isBlank()) {
                updateStudent();

            } else {

                studentInfoLabel.setText("FILL ALL THE FIELDS");
            }
        }else if(isDeleteStudent){
            if (!studentInfoRegNoTextField.getText().isBlank()) {
                deleteStudent();

            } else {

                studentInfoLabel.setText("FILL THE  REGISTRATION NUMBER FIELDS");
            }
        }

    }


    public void setStudentInfoCancelButtonOnAction(ActionEvent event) {
        if (isAddStudent) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancel add student");
            alert.setHeaderText("Are you sure you want to cancel Add student: ");
            alert.setContentText("Press OK to confirm, or Cancel to go back.");

            if (alert.showAndWait().get().getText().equals("OK")) {
                studentInfoSaveButton.setVisible(false);
                studentInfoCancelButton.setVisible(false);
                studentInfoLabel.setText("");
                studentInfoRegNoTextField.clear();
                studentInfoNameTextField.clear();
                studentInfoEmailTextField.clear();
                studentInfoIdTextField.clear();
            }
        }
    }

    public void addStudent() {
        DatabaseConn connectNow = new DatabaseConn();
        Connection connectDB = connectNow.getConnection();

        String regNo = studentInfoRegNoTextField.getText().toUpperCase();
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
                studentInfoSaveButton.setVisible(false);
                studentInfoCancelButton.setVisible(false);
                studentInfoLabel.setText("");
                studentInfoRegNoTextField.clear();
                studentInfoNameTextField.clear();
                studentInfoEmailTextField.clear();
                studentInfoIdTextField.clear();
                //?Updating the listview and treeview
                updateStudentList();
                updateTreeView();
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

                String studentListOutput = studentID + ".    " + studentReg + "  " + studentName + "  " + studentId + "  " + email;
                dashboardListView.getItems().add(studentListOutput);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    public void updateTreeView() {
        DatabaseConn connectNow = new DatabaseConn();//Creating an instance of the DatabaseConn class
        Connection connectDB = connectNow.getConnection(); //this is the connection to the database

        if(branchItem == null){
            branchItem = new TreeItem<>("Students");
        }
        // Clear the existing children of the branchItem
        branchItem.getChildren().clear();

        try {
            String studentListQuery = "SELECT * FROM students"; // Query to retrieve all columns from students table
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(studentListQuery);

            while (resultSet.next()) {
                String studentID = resultSet.getString("id");
                String studentReg = resultSet.getString("reg");
                String studentName = resultSet.getString("name");
                String studentGrade = resultSet.getString("grade");
                TreeItem<String> studentItem = new TreeItem<>(studentID + "." + studentReg + " " + studentName + " " + studentGrade);
                branchItem.getChildren().add(studentItem); // Add each student as a child to branchItem
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            ex.getCause();
        }
    }



    //! METHOD TO UPDATE STUDENT
    public void showUpdateForm() {
        isAddStudent = false;
        isUpdateStudent = true;
        isDeleteStudent = false;
        studentInfoLabel.setText("Enter the student details above and click \"Update\" to update the student details in the database");
        studentInfoSaveButton.setVisible(true);
        studentInfoCancelButton.setVisible(true);
        changeButtonText("Update");
    }


    private void updateStudent() {
        DatabaseConn connectNow = new DatabaseConn();
        Connection connectDB = connectNow.getConnection();

        String regNo = studentInfoRegNoTextField.getText();
        String name = studentInfoNameTextField.getText();
        String id = studentInfoIdTextField.getText();
        String email = studentInfoEmailTextField.getText();

        // Check if a student with the specified registration number exists
        String searchQuery = "SELECT * FROM students WHERE reg = ?";
        try {
            PreparedStatement searchStatement = connectDB.prepareStatement(searchQuery);
            searchStatement.setString(1, regNo);
            ResultSet resultSet = searchStatement.executeQuery();

            if (resultSet.next()) {
                // Update the existing student's information
                int studentID = resultSet.getInt("id");
                String updateQuery = "UPDATE students SET name = ?, student_id = ?, email = ? WHERE id = ?";
                PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                updateStatement.setString(1, name);
                updateStatement.setString(2, id);
                updateStatement.setString(3, email);
                updateStatement.setInt(4, studentID);

                int rowsAffected = updateStatement.executeUpdate();
                if (rowsAffected > 0) {
                    // Student information updated successfully
                    studentInfoLabel.setText("");
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Student information updated successfully.");

                    // Set a timeline to automatically close the alert after 1 second
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> successAlert.close()));
                    timeline.setCycleCount(1);
                    timeline.play();

                    successAlert.showAndWait();

                    // Clear input fields and hide buttons
                    studentInfoSaveButton.setVisible(false);
                    studentInfoCancelButton.setVisible(false);
                    studentInfoRegNoTextField.clear();
                    studentInfoNameTextField.clear();
                    studentInfoEmailTextField.clear();
                    studentInfoIdTextField.clear();

                    //?Updating the listview and treeview
                    updateStudentList();
                    updateTreeView();
                } else {
                    // Error occurred while updating student information
                    studentInfoLabel.setText("");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed");
                    alert.setHeaderText("Failed to update student information.");
                    alert.setContentText("An error occurred while updating student information. Please try again.");
                    alert.showAndWait();
                }
            } else {
                // Student with the specified registration number does not exist
                studentInfoLabel.setText("");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Student with registration number " + regNo + " not found.");
                alert.setContentText("Please enter a valid registration number.");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            ex.getCause();
        }
    }


    //!METHOD TO DELETE STUDENT
    public void showDeleteForm() {
        studentInfoLabel.setText("Enter the student Registration Number above and click \"Delete\" to delete the student from the database");
        studentInfoSaveButton.setVisible(true);
        studentInfoCancelButton.setVisible(true);
        changeButtonText("Delete");
        isDeleteStudent = true;
        isAddStudent = false;
        isUpdateStudent = false;
    }

    private void deleteStudent() {
// Get the registration number from the text field
        String regNo = studentInfoRegNoTextField.getText();

        // Confirm deletion with a dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete the student with registration number    \"" + regNo + "\"?");
        confirmAlert.setContentText("This action cannot be undone.");

        // Show the confirmation dialog and proceed with deletion if confirmed
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                DatabaseConn connectNow = new DatabaseConn();
                Connection connectDB = connectNow.getConnection();

                // SQL query to delete the student with the specified registration number
                String deleteQuery = "DELETE FROM students WHERE reg = ?";
                try {
                    PreparedStatement deleteStatement = connectDB.prepareStatement(deleteQuery);
                    deleteStatement.setString(1, regNo);

                    // Execute the deletion query
                    int rowsAffected = deleteStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Student deleted successfully
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText("Student with registration number   \"" + regNo + " \" deleted successfully.");

                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> successAlert.close()));
                        timeline.setCycleCount(1);
                        timeline.play();

                        successAlert.showAndWait();

                        //? Update the list view and treeview after deletion
                        updateStudentList();
                        updateTreeView();
                    } else {
                        // No student found with the specified registration number
                        Alert notFoundAlert = new Alert(Alert.AlertType.ERROR);
                        notFoundAlert.setTitle("Error");
                        notFoundAlert.setHeaderText("Student with registration number " + regNo + " not found.");
                        notFoundAlert.setContentText("No student found with the specified registration number.");
                        notFoundAlert.showAndWait();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    ex.getCause();
                }
            }
        });
    }


}
