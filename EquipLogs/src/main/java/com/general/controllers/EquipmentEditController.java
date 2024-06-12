package com.general.controllers;

import com.general.daologic.RequestDAO;
import com.general.entity.DataItem;
import com.general.utility.DialogWindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class EquipmentEditController {

    private final RequestDAO connection = new RequestDAO();
    private int selectedID;

    @FXML
    private TextArea txtEquipName;
    @FXML
    private ComboBox<DataItem> categoryBox;


    public void setData(int selectedEquipment) {
        selectedID = selectedEquipment;
        Optional<ResultSet> rawData = connection.requestData(RequestDAO.REQ_EQUIPMENT + " WHERE id = " + selectedEquipment);
        if (rawData.isPresent()) try (ResultSet equipResult = rawData.get()) {
            if (equipResult.next()) {
                txtEquipName.setText(equipResult.getString("equipname"));

                Map<Integer, String> categories = connection.getCategories();

                populateComboBox(categories, categoryBox);

                Optional<ResultSet> rawDataCategory = connection.requestData(RequestDAO.REQ_CATEGORY + " WHERE id = " + equipResult.getInt("categoryid"));
                if (rawDataCategory.isPresent()) try (ResultSet catResult = rawDataCategory.get()) {
                    if (catResult.next()) {
                        for (DataItem item : categoryBox.getItems()) {
                            if (item.getId() == catResult.getInt("id")) {
                                categoryBox.setValue(item);
                                break;
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace(System.out);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    @FXML
    private void submitBtnClick() throws SQLException {
        int affectedRows = new RequestDAO().updateEquipment(txtEquipName.getText(), categoryBox.getValue().getId(), selectedID);
        if (affectedRows != 0) {
            DialogWindowManager.showMessage("Datele au fost editate cu success");
        } else {
            DialogWindowManager.showMessage("Nu s-a primit sa se editeze datele");
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
}
