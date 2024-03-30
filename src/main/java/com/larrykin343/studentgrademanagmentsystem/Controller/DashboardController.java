package com.larrykin343.studentgrademanagmentsystem.Controller;

import com.larrykin343.studentgrademanagmentsystem.Utils.DatabaseConn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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

                String studentListOutput = studentID +".    "+studentReg+"  "+ studentName;
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
    public void addStudent(){

    }


}
