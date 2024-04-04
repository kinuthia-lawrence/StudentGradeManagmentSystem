package com.larrykin343.studentgrademanagmentsystem.Utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;

public class EmailSender {
    DatabaseConn connectNow = new DatabaseConn();//Creating an instance of the DatabaseConn class
    Connection connectDB = connectNow.getConnection(); //this is the connection to the database

    private static final String SMTP_HOST = Credentials.getSmtpHost();
    private static final String SMTP_PORT = Credentials.getSmtpPort();
    private static final String EMAIL_USERNAME = Credentials.getEmailUsername();
    private static final String EMAIL_PASSWORD = Credentials.getEmailPassword();


    public void sendStudentResultsByEmail() {
        try {
            String query = "SELECT * FROM students_results";
            PreparedStatement statement = connectDB.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String reg = resultSet.getString("reg").toUpperCase();
                String email = resultSet.getString("email");
                String year = resultSet.getString("year");

                String[] unitCodes = {
                        resultSet.getString("unit1Code"),
                        resultSet.getString("unit2Code"),
                        resultSet.getString("unit3Code"),
                        resultSet.getString("unit4Code"),
                        resultSet.getString("unit5Code")
                };
                double[] marks = {
                        resultSet.getDouble("unit1Marks"),
                        resultSet.getDouble("unit2Marks"),
                        resultSet.getDouble("unit3Marks"),
                        resultSet.getDouble("unit4Marks"),
                        resultSet.getDouble("unit5Marks")
                };
                String finalGrade = resultSet.getString("finalGrade");

                // Compose email content
                String subject = "Your Semester Results for Year " + year;
                String body = "Dear Student, " + reg + "\n\n"
                        + "Here are your semester results:\n\n"
                        + "Unit Codes: " + String.join(", ", unitCodes) + "\n"
                        + "Marks: " + String.join(", ", Arrays.stream(marks).mapToObj(String::valueOf).toArray(String[]::new)) + "\n"
                        + "Final Grade: " + finalGrade + "\n\n"
                        + "Best regards,\n"
                        + "Student Grade Management System";

                // Send email
                sendEmail(email, subject, body);

                Alert alert= new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Email Sent");
                alert.setHeaderText("Email Sent Successfully");
                alert.showAndWait();

                //set timeline for alert
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.10), evt -> alert.hide()));
                timeline.setCycleCount(1);
                timeline.play();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String recipient, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully to: " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}