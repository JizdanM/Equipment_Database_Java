package com.general.controllers;

import com.general.utility.DialogWindowManager;
import javafx.fxml.FXML;
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
            setSystemEnvironmentVariable("ELDBPassword", txtDBPassword.getText());

            DialogWindowManager.showMessage("Datele au fost salvate cu success.");
        } catch (IOException e) {
            e.printStackTrace(System.out);
            DialogWindowManager.showMessage("Datele nu au putut fi salvate.");
        }
    }

    public static void setSystemEnvironmentVariable(String variableName, String variableValue) throws IOException {
        String command = "cmd";
        String argument1 = "/c";
        String argument2 = "setx";

        ProcessBuilder processBuilder = new ProcessBuilder(command, argument1, argument2, variableName, variableValue);
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
