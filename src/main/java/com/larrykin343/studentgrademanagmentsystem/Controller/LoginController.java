package com.larrykin343.studentgrademanagmentsystem.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    public void loginButtonOnAction(ActionEvent event) {
        System.out.println("Login button clicked");

    }


    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }


}
