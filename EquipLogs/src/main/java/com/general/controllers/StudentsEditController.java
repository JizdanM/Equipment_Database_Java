package com.general.controllers;

import com.general.daologic.RequestDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentsEditController {
    private final RequestDAO connection = new RequestDAO();
    private int selectedID;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtGroup;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhoneNr;


    public void setData(int selectedStudent) {
        selectedID = selectedStudent;
        try {
            ResultSet equipResult = connection.requestData(RequestDAO.REQ_STUDENTS + " WHERE id = " + selectedStudent);
            if (equipResult.next()) {
                txtName.setText(equipResult.getString("name"));
                txtSurname.setText(equipResult.getString("surname"));
                txtGroup.setText(equipResult.getString("class"));
                txtEmail.setText(equipResult.getString("email"));
                txtPhoneNr.setText(equipResult.getString("phonenumber"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void submitBtnClick(ActionEvent event) throws SQLException {
        try {
            int affectedRows = new RequestDAO().updateStudents(txtName.getText(), txtSurname.getText(), txtGroup.getText(), txtEmail.getText(), txtPhoneNr.getText(), selectedID);
            if (affectedRows != 0) {
                showMessage("Datele au fost editate cu success");
            } else {
                showMessage("Nu s-a primit sa se editeze datele");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }

    private static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mesaj");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
