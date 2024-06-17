package com.general.controllers;

import com.general.daologic.RequestDAO;
import com.general.entity.DataItem;
import com.general.utility.DialogWindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class LogsEditController {
    private int selectedID;

    @FXML
    private ComboBox<DataItem> categoryBox;
    @FXML
    private ComboBox<DataItem> equipmentBox;
    @FXML
    private ComboBox<DataItem> studentBox;
    @FXML
    private DatePicker lendDate;
    @FXML
    private TextArea txtNotes;

    @FXML
    private void initialize () {
        AtomicBoolean firstChange = new AtomicBoolean(true);

        categoryBox.setOnAction(x -> {
            if (firstChange.get()) {
                firstChange.set(false);
            }

            try {
                Map<Integer, String> equipment = RequestDAO.getEquipmentByCategory(categoryBox.getSelectionModel().getSelectedItem().getId());
                populateComboBox(equipment, equipmentBox);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setData(int selectedLog) {
        selectedID = selectedLog;

        Optional<ResultSet> rawData = RequestDAO.requestData(RequestDAO.REQ_LOGS + " WHERE id = " + selectedLog);
        if (rawData.isPresent()) {
            ResultSet logResult = rawData.get();

            try {
                lendDate.setValue(logResult.getDate("lenddate").toLocalDate());
                txtNotes.setText(logResult.getString("notes"));

                Map<Integer, String> categories = RequestDAO.getCategories();
                populateComboBox(categories, categoryBox);

                Map<Integer, String> equipment = RequestDAO.getEquipment();
                populateComboBox(equipment, equipmentBox);

                Map<Integer, String> students = RequestDAO.getStudents();
                populateComboBox(students, studentBox);

                Optional<ResultSet> rawDataStudent = RequestDAO.requestData(RequestDAO.REQ_STUDENTS + " WHERE id = " + logResult.getInt("student"));
                if (rawDataStudent.isPresent()) try (ResultSet studentResult = rawDataStudent.get()) {
                    for (DataItem item : studentBox.getItems()) {
                        if (item.getId() == studentResult.getInt("id")) {
                            studentBox.setValue(item);
                            break;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                    DialogWindowManager.showError("Datele nu au putut fi extrase");
                }

                Optional<ResultSet> rawDataEquipment = RequestDAO.requestData(RequestDAO.REQ_EQUIPMENT + " WHERE id = " + logResult.getInt("equipment"));
                if (rawDataEquipment.isPresent()) {
                    ResultSet equipResult = rawDataEquipment.get();

                    Optional<ResultSet> rawDataCategory = RequestDAO.requestData("SELECT id, catname FROM category WHERE id = (SELECT categoryid FROM equipment WHERE id = " + equipResult.getInt("id") + ")");
                    if (rawDataCategory.isPresent()) try (ResultSet results = rawDataCategory.get()) {
                        for (DataItem categoryItem : categoryBox.getItems()) {
                            if (categoryItem.getId() == results.getInt("id")) {
                                categoryBox.setValue(categoryItem);
                            }
                        }

                        Map<Integer, String> equipmentByCategory = RequestDAO.getEquipmentByCategory(categoryBox.getSelectionModel().getSelectedItem().getId());
                        populateComboBox(equipmentByCategory, equipmentBox);

                        for (DataItem equipmentItem : equipmentBox.getItems()) {
                            if (equipmentItem.getId() == equipResult.getInt("id")) {
                                equipmentBox.setValue(equipmentItem);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace(System.out);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void submitBtnClick() {
        try {
            if (equipmentBox.getSelectionModel().getSelectedIndex() != -1) {
                int affectedRows = 0;
                if (txtNotes.getText() == null) {
                    affectedRows = RequestDAO.updateLogs(equipmentBox.getValue().getId(), studentBox.getValue().getId(), Date.valueOf(lendDate.getValue()), "", selectedID);
                } else {
                    affectedRows = RequestDAO.updateLogs(equipmentBox.getValue().getId(), studentBox.getValue().getId(), Date.valueOf(lendDate.getValue()), txtNotes.getText(), selectedID);
                }

                if (affectedRows != 0) {
                    DialogWindowManager.showMessage("Datele au fost editate cu success");
                } else {
                    DialogWindowManager.showMessage("Nu s-a primit sa se editeze datele");
                }

                Stage stage = (Stage) lendDate.getScene().getWindow();
                stage.close();
            } else {
                DialogWindowManager.showError("Alegeţi echipamentul!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void populateComboBox(Map<Integer, String> dataMap, ComboBox<DataItem> comboBox) {
        comboBox.getItems().clear();
        for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
            comboBox.getItems().add(new DataItem(entry.getKey(), entry.getValue()));
        }
    }
}
