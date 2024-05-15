package com.general.controllers;

import com.general.daologic.RequestDAO;
import com.general.entity.DataItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class InsertionWindowController {

    private final RequestDAO connection = new RequestDAO();
    private String insertionType;

    @FXML
    private AnchorPane equipPane;
    @FXML
    private AnchorPane studentPane;
    @FXML
    private AnchorPane logPane;

    @FXML
    private Menu btnCategory;
    @FXML
    private Menu btnEquipment;
    @FXML
    private Menu btnStudent;
    @FXML
    private Menu btnLogs;

    @FXML
    private TextArea txtEquipName;
    @FXML
    private ComboBox<DataItem> categoryBox;
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
    @FXML
    private ComboBox<DataItem> equipmentBox;
    @FXML
    private ComboBox<DataItem> studentBox;
    @FXML
    private DatePicker lendDate;

    public InsertionWindowController() throws IOException {
    }

    @FXML
    private void initialize(){

        btnCategory.setOnShown(event -> {
            insertionType = "category";

            try {
                Optional<String> result = requestString();

                if (result.isPresent()){
                    int affectedRows = connection.insertCategory(result.get());
                    if (affectedRows != 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Inserare cu success");
                        alert.setHeaderText(null);
                        alert.setContentText("Categoria noua a fost salvata");
                        alert.showAndWait();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            btnCategory.hide();
        });

        btnEquipment.setOnShown(event -> {
            studentPane.setOpacity(0);
            studentPane.setDisable(true);
            logPane.setOpacity(0);
            logPane.setDisable(true);
            equipPane.setOpacity(1);
            equipPane.setDisable(false);

            insertionType = "equipment";

            try {
                Map<Integer, String> categories = connection.getCategories();
                populateComboBox(categories, categoryBox);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            btnEquipment.hide();
        });

        btnStudent.setOnShown(event -> {
            equipPane.setOpacity(0);
            equipPane.setDisable(true);
            logPane.setOpacity(0);
            logPane.setDisable(true);
            studentPane.setOpacity(1);
            studentPane.setDisable(false);

            insertionType = "student";

            btnStudent.hide();
        });

        btnLogs.setOnShown(event -> {
            equipPane.setOpacity(0);
            equipPane.setDisable(true);
            studentPane.setOpacity(0);
            studentPane.setDisable(true);
            logPane.setOpacity(1);
            logPane.setDisable(false);

            insertionType = "logs";

            try {
                Map<Integer, String> equipment = connection.getEquipment();
                populateComboBox(equipment, equipmentBox);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                Map<Integer, String> students = connection.getStudents();
                populateComboBox(students, studentBox);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            btnLogs.hide();
        });
    }

    @FXML
    private void submitBtnClick(ActionEvent event) throws SQLException {
        int affectedRows;

        switch (insertionType){

            case "equipment":
                affectedRows = connection.insertEquipment(txtEquipName.getText(), categoryBox.getValue().getId());
                if (affectedRows != 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Inserare cu success");
                    alert.setHeaderText(null);
                    alert.setContentText("Echipamentul nou a fost salvat");
                    alert.showAndWait();
                }
                break;

            case "student":
                affectedRows = connection.insertStudent(txtName.getText(), txtSurname.getText(), txtGroup.getText(), txtEmail.getText(), txtPhoneNr.getText());
                if (affectedRows != 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Inserare cu success");
                    alert.setHeaderText(null);
                    alert.setContentText("Studentul nou a fost salvat");
                    alert.showAndWait();
                }
                break;

            case "logs":
                affectedRows = connection.insertLog(equipmentBox.getValue().getId(), studentBox.getValue().getId(), Date.valueOf(lendDate.getValue()));
                if (affectedRows != 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Inserare cu success");
                    alert.setHeaderText(null);
                    alert.setContentText("Logul nou a fost salvat");
                    alert.showAndWait();
                }
                break;
        }
    }

    private static Optional<String> requestString() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Categorie noua");
        dialog.setHeaderText("Introduceti numele categoriei noi");
        dialog.setContentText("Categoria: ");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(category -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmare");
            confirmationAlert.setHeaderText("Ati introdus categoria corect?");
            confirmationAlert.setContentText("Denumirea categoriei: " + category);

            confirmationAlert.showAndWait();
        });
        return result;
    }

    private void populateComboBox(Map<Integer, String> dataMap, ComboBox<DataItem> comboBox) {
        comboBox.getItems().clear();
        for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
            comboBox.getItems().add(new DataItem(entry.getKey(), entry.getValue()));
        }
    }
}
