package com.general.controllers;

import com.general.daologic.RequestDAO;
import com.general.entity.DataItem;
import com.general.utility.DialogWindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private ComboBox<DataItem> equipCategoryBox;
    @FXML
    private ComboBox<DataItem> equipmentBox;
    @FXML
    private ComboBox<DataItem> studentBox;
    @FXML
    private DatePicker lendDate;

    MainController parentController;

    public void setParentController(MainController parentController) {
        this.parentController = parentController;
    }

    public InsertionWindowController() {

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

                        parentController.showCategory();
                        Stage stage = (Stage) equipPane.getScene().getWindow();
                        stage.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(System.out);
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
            lendDate.setValue(LocalDate.now());

            equipPane.setOpacity(0);
            equipPane.setDisable(true);
            studentPane.setOpacity(0);
            studentPane.setDisable(true);
            logPane.setOpacity(1);
            logPane.setDisable(false);

            insertionType = "logs";

            try {
                Map<Integer, String> categories = connection.getCategories();
                populateComboBox(categories, equipCategoryBox);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                Map<Integer, String> students = connection.getStudents();
                populateComboBox(students, studentBox);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            AtomicBoolean firstChange = new AtomicBoolean(true);
            equipCategoryBox.setOnAction(x -> {
                if (firstChange.get()) {
                    equipmentBox.setDisable(false);
                    firstChange.set(false);
                }

                try {
                    Map<Integer, String> equipment = connection.getEquipmentByCategory(equipCategoryBox.getSelectionModel().getSelectedItem().getId());
                    populateComboBox(equipment, equipmentBox);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            btnLogs.hide();
        });
    }

    @FXML
    private void submitBtnClick() throws SQLException {
        int affectedRows;

        switch (insertionType){

            case "equipment":{
                if (!txtEquipName.getText().isEmpty() && categoryBox.getSelectionModel().getSelectedIndex() != -1) {
                    affectedRows = connection.insertEquipment(txtEquipName.getText(), categoryBox.getValue().getId());
                    if (affectedRows != 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Inserare cu success");
                        alert.setHeaderText(null);
                        alert.setContentText("Echipamentul nou a fost salvat");
                        alert.showAndWait();

                        parentController.showEquipment();
                        Stage stage = (Stage) equipPane.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    DialogWindowManager.showMessage("Introduceti datele!");
                }
            }
            break;

            case "student":{
                if (!txtName.getText().isEmpty() && !txtSurname.getText().isEmpty() && !txtGroup.getText().isEmpty() && !txtEmail.getText().isEmpty() && !txtPhoneNr.getText().isEmpty()) {
                    affectedRows = connection.insertStudent(txtName.getText(), txtSurname.getText(), txtGroup.getText(), txtEmail.getText(), txtPhoneNr.getText());
                    if (affectedRows != 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Inserare cu success");
                        alert.setHeaderText(null);
                        alert.setContentText("Studentul nou a fost salvat");
                        alert.showAndWait();

                        parentController.showStudents();
                        Stage stage = (Stage) equipPane.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    DialogWindowManager.showMessage("Introduceti datele!");
                }
            }
            break;

            case "logs":{
                try {
                    if (equipmentBox.getSelectionModel().getSelectedIndex() != -1 && studentBox.getSelectionModel().getSelectedIndex() != -1) {
                        affectedRows = connection.insertLog(equipmentBox.getValue().getId(), studentBox.getValue().getId(), Date.valueOf(lendDate.getValue()));
                        if (affectedRows != 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Inserare cu success");
                            alert.setHeaderText(null);
                            alert.setContentText("Logul nou a fost salvat");
                            alert.showAndWait();

                            parentController.showLogs();
                            Stage stage = (Stage) equipPane.getScene().getWindow();
                            stage.close();
                        } else {
                            DialogWindowManager.showMessage("Introduceti datele!");
                        }
                    }

                } catch (SQLException e) {
                    if (e.getSQLState().equals("23505")) {
                        DialogWindowManager.showMessage("Echipamentul nu a fost returnat!");
                    }
                }
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
