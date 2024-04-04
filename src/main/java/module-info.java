module com.larrykin.studentgrademanagmentsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires jakarta.mail;
    requires mysql.connector.j;


    opens com.larrykin343.studentgrademanagmentsystem.Controller to javafx.fxml;

    opens com.larrykin343.studentgrademanagmentsystem to javafx.fxml;
    exports com.larrykin343.studentgrademanagmentsystem;
    exports com.larrykin343.studentgrademanagmentsystem.Controller;
    exports com.larrykin343.studentgrademanagmentsystem.Utils;
    opens com.larrykin343.studentgrademanagmentsystem.Utils to javafx.fxml;
}