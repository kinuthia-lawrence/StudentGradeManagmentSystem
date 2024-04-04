package com.larrykin343.studentgrademanagmentsystem.Controller;

import com.larrykin343.studentgrademanagmentsystem.Utils.EmailSender;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class GradeReportController {


    public Button closeGradeReportButton;

    public void exportResults(ActionEvent actionEvent) {
        EmailSender emailSender = new EmailSender();
        emailSender.sendStudentResultsByEmail();
    }

    public void closeWindow(ActionEvent event) {
        closeGradeReportButton.getScene().getWindow().hide();

    }
}
