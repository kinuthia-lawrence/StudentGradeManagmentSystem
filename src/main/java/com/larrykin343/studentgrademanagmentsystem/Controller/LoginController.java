package com.larrykin343.studentgrademanagmentsystem.Controller;

import com.larrykin343.studentgrademanagmentsystem.Utils.DatabaseConn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class LoginController {
    @FXML
    public TextField UsernameTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public Label invalidLoginsLabel;
    @FXML
    public Button loginButton;
    @FXML
    public Button cancelButton;


    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    public void loginButtonOnAction(ActionEvent event) {
        if (!UsernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
            checkLoginCredentials();
        } else {
            invalidLoginsLabel.setText("Please enter your username and password !!!");
        }

        //! Set accelerator for login button (Shift+E)
        KeyCombination keyCombination = new KeyCodeCombination(KeyCode.E, KeyCombination.SHIFT_DOWN);
        loginButton.getScene().getAccelerators().put(keyCombination, this::mainApplication);
    }

    //TODO: check if the login credentials are correct
    //if the login credentials are correct, the user will be taken to the main application
    //if the login credentials are incorrect, the user will be prompted to enter the correct login credentials
    private void checkLoginCredentials() {
        DatabaseConn connectNow = new DatabaseConn();//Creating an instance of the DatabaseConn class
        Connection connectDB = connectNow.getConnection(); //this is the connection to the database

        //!taking the user inputs
        String username = UsernameTextField.getText();
        String password = passwordTextField.getText();

        //!this is the query to check if the login credentials are correct
        String verifyLogin = "SELECT count(1) FROM admin_account WHERE username = '" +
                username + "' AND password = '" + password + "'";
        try {
            Statement statement = connectDB.createStatement();//creating a statement
            ResultSet queryResult = statement.executeQuery(verifyLogin);//executing the query

            //? the queryResult will return a 1 if the login credentials are correct
            //? and a 0 if the login credentials are incorrect
            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    invalidLoginsLabel.setText("Login Successful");
                    mainApplication();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();
                } else {
                    invalidLoginsLabel.setText("Invalid Login. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void mainApplication() {
        try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Dashboard.fxml"));
        Stage mainApplicationStage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        mainApplicationStage.setScene(scene);
        mainApplicationStage.setTitle("Student Grade Management System");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/digitalLibrary.png")));
        mainApplicationStage.getIcons().add(icon);
        mainApplicationStage.show();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}