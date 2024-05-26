package com.general.controllers;

import com.general.daologic.RequestDAO;
import com.general.entity.DataItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class EquipmentEditController {

    private final RequestDAO connection = new RequestDAO();
    private int selectedID;

    @FXML
    private TextArea txtEquipName;
    @FXML
    private ComboBox<DataItem> categoryBox;


    public void setData(int selectedEquipment) {
        selectedID = selectedEquipment;
        try {
            ResultSet equipResult = connection.requestData(RequestDAO.REQ_EQUIPMENT + " WHERE id = " + selectedEquipment);
            if (equipResult.next()) {
                txtEquipName.setText(equipResult.getString("equipname"));

                Map<Integer, String> categories = connection.getCategories();

                populateComboBox(categories, categoryBox);

                ResultSet catResult = connection.requestData(RequestDAO.REQ_CATEGORY + " WHERE id = " + equipResult.getInt("categoryid"));
                if (catResult.next()) {
                    for (DataItem item : categoryBox.getItems()) {
                        if (item.getId() == catResult.getInt("id")) {
                            categoryBox.setValue(item);
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
        int affectedRows = new RequestDAO().updateEquipment(txtEquipName.getText(), categoryBox.getValue().getId(), selectedID);
        if (affectedRows != 0) {
            showMessage("Datele au fost editate cu success");
        } else {
            showMessage("Nu s-a primit sa se editeze datele");
        }

        Stage stage = (Stage) txtEquipName.getScene().getWindow();
        stage.close();
    }

    private void populateComboBox(Map<Integer, String> dataMap, ComboBox<DataItem> comboBox) {
        comboBox.getItems().clear();
        for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
            comboBox.getItems().add(new DataItem(entry.getKey(), entry.getValue()));
        }
    }

    private static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mesaj");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
