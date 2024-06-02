package com.general.controllers;

import com.general.daologic.RequestDAO;
import com.general.entity.DataItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class LogsEditController {
    private final RequestDAO connection = new RequestDAO();
    private int selectedID;

    @FXML
    private ComboBox<DataItem> categoryBox;
    @FXML
    private ComboBox<DataItem> equipmentBox;
    @FXML
    private ComboBox<DataItem> studentBox;
    @FXML
    private DatePicker lendDate;


    public void setData(int selectedStudent) {
        selectedID = selectedStudent;
        try {
            ResultSet logResult = connection.requestData(RequestDAO.REQ_LOGS + " WHERE id = " + selectedStudent);
            if (logResult.next()) {
                lendDate.setValue(logResult.getDate("lenddate").toLocalDate());

                Map<Integer, String> categories = connection.getCategories();
                populateComboBox(categories, categoryBox);

                Map<Integer, String> equipment = connection.getEquipment();
                populateComboBox(equipment, equipmentBox);

                ResultSet equipResult = connection.requestData(RequestDAO.REQ_EQUIPMENT + " WHERE id = " + logResult.getInt("equipment"));
                if (equipResult.next()) {
                    for (DataItem equipmentItem : equipmentBox.getItems()) {
                        if (equipmentItem.getId() == equipResult.getInt("id")) {
                            ResultSet results = connection.requestData("SELECT id, catname FROM category WHERE id = (SELECT categoryid FROM equipment WHERE id = " + equipmentItem.getId() + ")");
                            if (results.next()) {
                                for (DataItem categoryItem : categoryBox.getItems()) {
                                    if (categoryItem.getId() == results.getInt("id")) {
                                        categoryBox.setValue(categoryItem);
                                    }
                                }
                            }
                            equipmentBox.setValue(equipmentItem);
                            break;
                        }
                    }
                }

                Map<Integer, String> students = connection.getStudents();
                populateComboBox(students, studentBox);

                ResultSet studentResult = connection.requestData(RequestDAO.REQ_STUDENTS + " WHERE id = " + logResult.getInt("student"));
                if (studentResult.next()) {
                    for (DataItem item : studentBox.getItems()) {
                        if (item.getId() == studentResult.getInt("id")) {
                            studentBox.setValue(item);
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void submitBtnClick(ActionEvent event) throws SQLException {
        try {
            int affectedRows = new RequestDAO().updateLogs(equipmentBox.getValue().getId(), studentBox.getValue().getId(), Date.valueOf(lendDate.getValue()), selectedID);
            if (affectedRows != 0) {
                showMessage("Datele au fost editate cu success");
            } else {
                showMessage("Nu s-a primit sa se editeze datele");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Stage stage = (Stage) lendDate.getScene().getWindow();
        stage.close();
    }

    private static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mesaj");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void populateComboBox(Map<Integer, String> dataMap, ComboBox<DataItem> comboBox) {
        comboBox.getItems().clear();
        for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
            comboBox.getItems().add(new DataItem(entry.getKey(), entry.getValue()));
        }
    }
}
