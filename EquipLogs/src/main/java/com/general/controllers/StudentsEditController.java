package com.general.controllers;

import com.general.daologic.RequestDAO;
import com.general.utility.DialogWindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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
        Optional<ResultSet> rawData = connection.requestData(RequestDAO.REQ_STUDENTS + " WHERE id = " + selectedStudent);
        if (rawData.isPresent()) try (ResultSet equipResult = rawData.get()) {
            if (equipResult.next()) {
                txtName.setText(equipResult.getString("name"));
                txtSurname.setText(equipResult.getString("surname"));
                txtGroup.setText(equipResult.getString("class"));
                txtEmail.setText(equipResult.getString("email"));
                txtPhoneNr.setText(equipResult.getString("phonenumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }

    @FXML
    private void submitBtnClick() {
        try {
            int affectedRows = new RequestDAO().updateStudents(txtName.getText(), txtSurname.getText(), txtGroup.getText(), txtEmail.getText(), txtPhoneNr.getText(), selectedID);
            if (affectedRows != 0) {
                DialogWindowManager.showMessage("Datele au fost editate cu success");
            } else {
                DialogWindowManager.showMessage("Nu s-a primit sa se editeze datele");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
}
