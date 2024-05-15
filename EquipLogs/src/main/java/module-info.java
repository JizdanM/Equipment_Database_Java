module com.general.equiplogs {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.postgresql.jdbc;
    requires jdk.unsupported;

    opens com.general.equiplogs to javafx.fxml;
    opens com.general.entity;
    exports com.general.equiplogs;
    exports com.general.controllers;
    opens com.general.controllers to javafx.fxml;
}