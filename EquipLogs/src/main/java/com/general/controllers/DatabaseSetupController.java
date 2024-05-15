package com.general.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class DatabaseSetupController {

    @FXML
    private TextField txtDBName;
    @FXML
    private TextField txtDBUsername;
    @FXML
    private PasswordField txtDBPassword;


    @FXML
    protected void saveBtnClick() {
        try {
            setSystemEnvironmentVariable("ELDBName", txtDBName.getText());
            setSystemEnvironmentVariable("ELDBUsername", txtDBUsername.getText());
            setDBPassword("ELDBPassword", txtDBPassword.getText());

            showMessage("Datele au fost salvate cu success.");
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Datele nu au putut fi salvate.");
        }
    }

    public static void setSystemEnvironmentVariable(String variableName, String variableValue) throws IOException {
        String command = "cmd /c setx " + variableName + " " + variableValue;

        Process process = Runtime.getRuntime().exec(command);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setDBPassword(String variableName, String variableValue) throws IOException {
        String command = "cmd /c setx " + variableName + " " + variableValue;

        Process process = Runtime.getRuntime().exec(command);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mesaj");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
