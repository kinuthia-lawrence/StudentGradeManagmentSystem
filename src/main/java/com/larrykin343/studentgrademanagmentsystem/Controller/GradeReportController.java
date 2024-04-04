package com.larrykin343.studentgrademanagmentsystem.Controller;

import com.larrykin343.studentgrademanagmentsystem.Utils.EmailSender;
import javafx.event.ActionEvent;

public class GradeReportController {


    public void exportResults(ActionEvent actionEvent) {
        EmailSender emailSender = new EmailSender();
        emailSender.sendStudentResultsByEmail();
    }
}
