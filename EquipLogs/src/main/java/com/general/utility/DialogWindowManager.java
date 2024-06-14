package com.general.utility;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

public class DialogWindowManager {
    public static Optional<ButtonType> deleteConfirmationWindow() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmare");
        confirmationAlert.setHeaderText("Sunteti siguri ca doriti sa stergeti inregistrarea?");
        confirmationAlert.setContentText("Confirmati actiunea.");

        return confirmationAlert.showAndWait();
    }

    public static Optional<String> requestString(String initialText, String title, String header, String context, String confirmation) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(context);
        dialog.getEditor().setText(initialText);

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(category -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmare");
            confirmationAlert.setHeaderText(confirmation);
            confirmationAlert.setContentText("Ati introdus: " + category);

            confirmationAlert.showAndWait();
        });
        return result;
    }

    public static Optional<LocalDate> requestDate(LocalDate initialDate, String title, String header, String confirmation) {
        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        DatePicker datePicker = new DatePicker();

        datePicker.setValue(initialDate);

        VBox vbox = new VBox(datePicker);
        dialog.getDialogPane().setContent(vbox);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return datePicker.getValue();
            }
            return null;
        });

        Optional<LocalDate> result = dialog.showAndWait();

        result.ifPresent(date -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmare");
            confirmationAlert.setHeaderText(confirmation);
            confirmationAlert.setContentText("Ati introdus: " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            confirmationAlert.showAndWait();
        });

        return result;
    }

    public static Optional<String> showComboBoxDialog(Map<Integer, String> options) {
        if (options.isEmpty()) {
            showMessage("Eroare la încărcarea datelor.");
            return Optional.empty();
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Selectează categoria");
        dialog.setHeaderText("Selectează dupa ce categorie dorești să cauți echipamentul:");

        ButtonType okButtonType = new ButtonType("OK", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(options.values());
        comboBox.getSelectionModel().select(0);

        VBox dialogLayout = new VBox(10, comboBox);
        dialogLayout.setPadding(new javafx.geometry.Insets(10));
        dialog.getDialogPane().setContent(dialogLayout);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return comboBox.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Optional<Triple<LocalDate, LocalDate, String>> showDateRangePickerDialog() {
        Dialog<Triple<LocalDate, LocalDate, String>> dialog = new Dialog<>();
        dialog.setTitle("Alege data");
        dialog.setHeaderText("Alegeți data sau datele după care căutați logul");

        ButtonType confirmButtonType = new ButtonType("Caută", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType);

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setDisable(true);

        RadioButton ascendingRadioButton = new RadioButton("Crescător");
        RadioButton descendingRadioButton = new RadioButton("Descrescător");
        RadioButton intervalRadioButton = new RadioButton("Interval");
        ToggleGroup toggleGroup = new ToggleGroup();
        ascendingRadioButton.setToggleGroup(toggleGroup);
        descendingRadioButton.setToggleGroup(toggleGroup);
        intervalRadioButton.setToggleGroup(toggleGroup);
        ascendingRadioButton.setSelected(true);

        ascendingRadioButton.setOnAction(e -> {endDatePicker.setDisable(true); endDatePicker.setValue(null);});
        descendingRadioButton.setOnAction(e -> {endDatePicker.setDisable(true); endDatePicker.setValue(null);});
        intervalRadioButton.setOnAction(e -> {endDatePicker.setDisable(false); endDatePicker.setValue(null);});

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 40, 10, 10));
        grid.setAlignment(Pos.CENTER);

        grid.add(ascendingRadioButton, 0, 0);
        grid.add(descendingRadioButton, 1, 0);
        grid.add(intervalRadioButton, 2, 0);
        grid.add(new Label("Prima dată:"), 0, 1);
        grid.add(startDatePicker, 1, 1);
        grid.add(new Label("A doua dată:"), 0, 2);
        grid.add(endDatePicker, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                String sortOrder = intervalRadioButton.isSelected() ? "interval"
                        : ascendingRadioButton.isSelected() ? "ascending" : "descending";
                LocalDate startDate = startDatePicker.getValue();
                if (startDate == null) {
                    if (sortOrder.equals("ascending") || sortOrder.equals("descending")) {
                        showError("Selectați o dată!");
                    } else {
                        showError("Selectați prima dată a intervalului!");
                    }
                    return null;
                }
                LocalDate endDate = endDatePicker.getValue();
                if (sortOrder.equals("interval") && endDate == null) {
                    showError("Selectati a doua data!");
                    return null;
                }
                return new Triple<>(startDate, endDate, sortOrder);
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eroare");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mesaj");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public record Triple<F, S, T>(F first, S second, T third) {
    }
}
