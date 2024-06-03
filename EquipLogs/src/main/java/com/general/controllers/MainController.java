package com.general.controllers;

import com.general.daologic.RequestDAO;
import com.general.entity.*;
import com.general.equiplogs.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class MainController {
    private static String selectedTable = "";

    @FXML
    private TableView equipTable;

    @FXML
    private MenuItem btnSetting;
    @FXML
    private MenuItem btnExit;
    @FXML
    private MenuItem btnShowEquipment;
    @FXML
    private MenuItem btnShowCategory;
    @FXML
    private MenuItem btnShowStudents;
    @FXML
    private MenuItem btnShowLogs;

    @FXML
    private Button btnReturn;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private Stage mainStage;

    /*
    /// -----Constructors / Initializers
     */

    @FXML
    private void initialize() {
        // TODO: Find a bette way to hide "Return" button (if possible)
        btnSetting.setOnAction(event -> {
            hideReturnButton();
            settingsBtn();
        });

        btnExit.setOnAction(event -> close());

        btnShowEquipment.setOnAction(event -> {
            hideReturnButton();
            showEquipment();
        });

        btnShowCategory.setOnAction(event -> {
            hideReturnButton();
            showCategory();
        });

        btnShowStudents.setOnAction(event -> {
            hideReturnButton();
            showStudents();
        });

        btnShowLogs.setOnAction(event -> {
            showReturnButton();
            showLogs();
        });

        btnEdit.setOnAction(event -> {
            dataEdit();
        });

        btnDelete.setOnAction(event -> {
            dataDeletion();
        });
    }

    /*
    /// -----Menu buttons functions
     */

    @FXML
    protected void settingsBtn() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainApp.class.getResource("DatabaseSetup.fxml")));
            Scene scene = new Scene(root, 400, 220);

            Stage primaryStage = new Stage();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Database connection settings");
            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.initOwner(equipTable.getScene().getWindow());
            primaryStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: Create a simple setting window (Language / Theme etc.)
    }

    @FXML
    protected void close() {
        System.exit(0);
    }

    @FXML
    protected void showEquipment() {
        try {
            equipTable.getColumns().clear();

            TableColumn<Equipment, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Equipment, String> equipmentColumn = new TableColumn<>("Echipament");
            equipmentColumn.setCellValueFactory(new PropertyValueFactory<>("equipName"));

            TableColumn<Equipment, String> categoryColumn = new TableColumn<>("Categorie");
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

            equipTable.getColumns().addAll(idColumn, equipmentColumn, categoryColumn);
            equipTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            ResultSet output = new RequestDAO().requestData(RequestDAO.REQ_EQUIPMENT_USR);
            ObservableList<Equipment> equipList = FXCollections.observableArrayList();
            // TODO: proparly allign the columns in the table

            while(output.next()){
                Equipment equip = new Equipment(output.getInt("id"), output.getString("equipname"), output.getString("catname"));
                equipList.add(equip);
            }

            equipTable.setItems(equipList);
            selectedTable = "equipment";

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @FXML
    protected void showCategory() {
        try {
            equipTable.getColumns().clear();

            TableColumn<Category, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Category, String> categoryColumn = new TableColumn<>("Categorie");
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

            equipTable.getColumns().addAll(idColumn, categoryColumn);
            equipTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            ResultSet output = new RequestDAO().requestData(RequestDAO.REQ_CATEGORY_ORDERED);
            ObservableList<Category> categoryList = FXCollections.observableArrayList();

            while(output.next()){
                Category category = new Category(output.getInt("id"), output.getString("catname"));
                categoryList.add(category);
            }

            equipTable.setItems(categoryList);
            selectedTable = "category";

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @FXML
    protected void showStudents() {
        try {
            equipTable.getColumns().clear();

            TableColumn<Student, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Student, String> nameColumn = new TableColumn<>("Nume");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Student, String> surnameColumn = new TableColumn<>("Prenume");
            surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

            TableColumn<Student, String> groupColumn = new TableColumn<>("Grupa");
            groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));

            TableColumn<Student, String> emailColumn = new TableColumn<>("Email");
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

            TableColumn<Student, String> phoneNumberColumn = new TableColumn<>("NrTelefon");
            phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNr"));

            equipTable.getColumns().addAll(idColumn, nameColumn, surnameColumn, groupColumn, emailColumn, phoneNumberColumn);
            equipTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            ResultSet output = new RequestDAO().requestData(RequestDAO.REQ_STUDENTS_ORDERED);
            ObservableList<Student> studentList = FXCollections.observableArrayList();

            while(output.next()){
                Student student = new Student(output.getInt("id"), output.getString("name")
                        , output.getString("surname"), output.getString("class")
                        , output.getString("email"), output.getString("phonenumber"));
                studentList.add(student);
            }

            equipTable.setItems(studentList);
            selectedTable = "students";

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @FXML
    protected void showLogs() {
        try {
            equipTable.getColumns().clear();

            TableColumn<Logs, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Logs, String> equipNameColumn = new TableColumn<>("Echipament");
            equipNameColumn.setCellValueFactory(cellData -> {
                Equipment equipment = cellData.getValue().getEquipment();
                if (equipment != null) {
                    return new SimpleStringProperty(equipment.getEquipName());
                }
                return new SimpleStringProperty("");
            });

            TableColumn<Logs, String> categoryNameColumn = new TableColumn<>("Categorie");
            categoryNameColumn.setCellValueFactory(cellData -> {
                Equipment equipment = cellData.getValue().getEquipment();
                if (equipment != null) {
                    return new SimpleStringProperty(equipment.getCategory());
                }
                return new SimpleStringProperty("");
            });

            TableColumn<Logs, String> nameColumn = new TableColumn<>("Nume");
            nameColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue().getStudent();
                if (student != null) {
                    return new SimpleStringProperty(student.getName());
                }
                return new SimpleStringProperty("");
            });

            TableColumn<Logs, String> surnameColumn = new TableColumn<>("Prenume");
            surnameColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue().getStudent();
                if (student != null) {
                    return new SimpleStringProperty(student.getSurname());
                }
                return new SimpleStringProperty("");
            });

            TableColumn<Logs, String> studentClassColumn = new TableColumn<>("Clasa");
            studentClassColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue().getStudent();
                if (student != null) {
                    return new SimpleStringProperty(student.getGroup());
                }
                return new SimpleStringProperty("");
            });

            TableColumn<Logs, String> emailColumn = new TableColumn<>("Email");
            emailColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue().getStudent();
                if (student != null) {
                    return new SimpleStringProperty(student.getEmail());
                }
                return new SimpleStringProperty("");
            });

            TableColumn<Logs, String> phoneNumberColumn = new TableColumn<>("NrTelefon");
            phoneNumberColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue().getStudent();
                if (student != null) {
                    return new SimpleStringProperty(student.getPhoneNr());
                }
                return new SimpleStringProperty("");
            });

            TableColumn<Logs, Date> lendDateColumn = new TableColumn<>("Data imprumutarii");
            lendDateColumn.setCellValueFactory(new PropertyValueFactory<>("lendDate"));

            TableColumn<Logs, Boolean> returnedColumn = new TableColumn<>("Returnat");
            returnedColumn.setCellValueFactory(new PropertyValueFactory<>("returned"));

            TableColumn<Logs, Date> returnDateColumn = new TableColumn<>("Data returnării");
            returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

            equipTable.getColumns().addAll(idColumn, equipNameColumn, categoryNameColumn, nameColumn, surnameColumn
                    , studentClassColumn, emailColumn, phoneNumberColumn, lendDateColumn, returnedColumn, returnDateColumn);
            equipTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            ResultSet output = new RequestDAO().requestData(RequestDAO.REQ_LOGS_USR);
            ObservableList<Logs> logList = FXCollections.observableArrayList();

            while(output.next()){
                Logs log = new Logs(output.getInt("log")
                        , new Equipment(output.getInt("equipment"), output.getString("equipname")
                                , output.getString("catname"))
                        , new Student(output.getInt("student"), output.getString("name")
                                , output.getString("surname"), output.getString("class")
                                , output.getString("email"), output.getString("phonenumber"))
                        , output.getDate("lenddate"), output.getBoolean("returned")
                        , output.getDate("returndate"));
                logList.add(log);
            }

            equipTable.setItems(logList);
            selectedTable = "logs";

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @FXML
    protected void dataInsertion() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainApp.class.getResource("InsertionWindow.fxml")));
            Scene scene = new Scene(root, 320, 344);

            Stage primaryStage = new Stage();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Data Insertion");
            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.initOwner(equipTable.getScene().getWindow());
            primaryStage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void dataEdit() {
        try {
            DeletableEntity selectedEquipment = (DeletableEntity) equipTable.getSelectionModel().getSelectedItem();

            if (selectedEquipment != null) {
                switch (selectedTable) {
                    case "equipment": {
                        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainApp.class.getResource("EquipmentEditWindow.fxml")));
                        Parent root = loader.load();

                        EquipmentEditController controller = loader.getController();
                        controller.setData(selectedEquipment.getId());

                        Scene scene = new Scene(root, 320, 344);

                        Stage primaryStage = new Stage();
                        primaryStage.setResizable(false);
                        primaryStage.setTitle("Editeaza datele");
                        primaryStage.setScene(scene);
                        primaryStage.initModality(Modality.WINDOW_MODAL);
                        primaryStage.initOwner(equipTable.getScene().getWindow());
                        primaryStage.showAndWait();

                        showEquipment();
                    }
                    break;
                    case "category": {
                        try {
                            final RequestDAO connection = new RequestDAO();
                            ResultSet resultSet = connection.requestData(RequestDAO.REQ_CATEGORY + " WHERE id = " + selectedEquipment.getId());
                            if (resultSet.next()) {
                                Optional<String> result = requestString(resultSet.getString("catname"), "Editarea categoriei", "Introduceți numele nou pentru categoria editată", "Categoria: ", "Ați introdus categoria corectă?");

                                if (result.isPresent()){
                                    int affectedRows = connection.updateCategory(result.get(), selectedEquipment.getId());
                                    if (affectedRows != 0) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Editare cu success");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Editarea a fost salvata");
                                        alert.showAndWait();
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        showCategory();
                    }
                    break;
                    case "students": {
                        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainApp.class.getResource("StudentsEditWindow.fxml")));
                        Parent root = loader.load();

                        StudentsEditController controller = loader.getController();
                        controller.setData(selectedEquipment.getId());

                        Scene scene = new Scene(root, 320, 344);

                        Stage primaryStage = new Stage();
                        primaryStage.setResizable(false);
                        primaryStage.setTitle("Editeaza datele");
                        primaryStage.setScene(scene);
                        primaryStage.initModality(Modality.WINDOW_MODAL);
                        primaryStage.initOwner(equipTable.getScene().getWindow());
                        primaryStage.showAndWait();

                        showStudents();
                    }
                    break;
                    case "logs": {
                        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainApp.class.getResource("LogsEditWindow.fxml")));
                        Parent root = loader.load();

                        LogsEditController controller = loader.getController();
                        controller.setData(selectedEquipment.getId());

                        Scene scene = new Scene(root, 320, 344);

                        Stage primaryStage = new Stage();
                        primaryStage.setResizable(false);
                        primaryStage.setTitle("Editeaza datele");
                        primaryStage.setScene(scene);
                        primaryStage.initModality(Modality.WINDOW_MODAL);
                        primaryStage.initOwner(equipTable.getScene().getWindow());
                        primaryStage.showAndWait();

                        showLogs();
                    }
                    break;
                }
            } else {
                showError("Nu ati selectat logul!");
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @FXML
    protected void dataDeletion() {
        try {
            DeletableEntity selectedEquipment = (DeletableEntity) equipTable.getSelectionModel().getSelectedItem();

            if (selectedEquipment != null) {
                switch (selectedTable) {
                    case "equipment": {
                        Optional<ButtonType> result = deleteConfirmationWindow();

                        if (result.isPresent() && result.get() == ButtonType.OK){
                            int updatedLines = new RequestDAO().executeUpdateStatement(RequestDAO.DELETE_EQUIPMENT + selectedEquipment.getId());
                            if (updatedLines != 0) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Stergere");
                                alert.setContentText("Datele au fost sterse cu success");
                                alert.showAndWait();

                                showEquipment();
                            }
                        }
                    }
                        break;
                    case "category": {
                        Optional<ButtonType> result = deleteConfirmationWindow();

                        if (result.isPresent() && result.get() == ButtonType.OK){
                            int updatedLines = new RequestDAO().executeUpdateStatement(RequestDAO.DELETE_CATEGORY + selectedEquipment.getId());
                            if (updatedLines != 0) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Stergere");
                                alert.setContentText("Datele au fost sterse cu success");
                                alert.showAndWait();

                                showCategory();
                            }
                        }
                    }
                        break;
                    case "students": {
                        Optional<ButtonType> result = deleteConfirmationWindow();

                        if (result.isPresent() && result.get() == ButtonType.OK){
                            int updatedLines = new RequestDAO().executeUpdateStatement(RequestDAO.DELETE_STUDENT + selectedEquipment.getId());
                            if (updatedLines != 0) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Stergere");
                                alert.setContentText("Datele au fost sterse cu success");
                                alert.showAndWait();

                                showStudents();
                            }
                        }
                    }
                        break;
                    case "logs": {
                        Optional<ButtonType> result = deleteConfirmationWindow();

                        if (result.isPresent() && result.get() == ButtonType.OK){
                            int updatedLines = new RequestDAO().executeUpdateStatement(RequestDAO.DELETE_LOGS + selectedEquipment.getId());
                            if (updatedLines != 0) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Stergere");
                                alert.setContentText("Datele au fost sterse cu success");
                                alert.showAndWait();

                                showLogs();
                            }
                        }
                    }
                    break;
                }
            } else {
                showError("Nu ati selectat logul!");
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /*
    /// -----Interaction buttons functions
     */

    @FXML
    protected void returnedBtnClick() {
        try {
            Logs selectedLog = (Logs) equipTable.getSelectionModel().getSelectedItem();

            if (selectedLog != null) {
                Optional<LocalDate> result = requestDate(LocalDate.now(), "Introduceți data", "Introduceți data returnării echipamentului", "Ați introdus data corectă?");
                int updatedDate = 0;
                if (result.isPresent()) {
                    updatedDate = new RequestDAO().executeUpdateStatement(RequestDAO.RETURN_DATE + "'" + result.get() + "' WHERE id = " + selectedLog.getId());
                }

                int id = selectedLog.getId();

                int updatedLines = new RequestDAO().executeUpdateStatement(RequestDAO.RETURN_EQUIPMENT + id);
                if (updatedLines != 0 && updatedDate != 0){
                    showMessage("Echipamentul a fost returnat");
                    showLogs();
                } else {
                    showError("Aplicatia nu a putut edita baza de date");
                }
            } else {
                showError("Nu ati selectat logul!");
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @FXML
    protected void refreshBtnClick() {
        switch (selectedTable) {
            case "equipment": {
                showEquipment();
            }
            break;
            case "category": {
                showCategory();
            }
            break;
            case "students": {
                showStudents();
            }
            break;
            case "logs": {
                showLogs();
            }
            break;
        }
    }

    /*
    /// -----Helper functions
    */

    @FXML
    protected void showReturnButton() {
        btnReturn.setOpacity(1);
        btnReturn.setDisable(false);
    }

    @FXML
    private void hideReturnButton() {
        btnReturn.setOpacity(0);
        btnReturn.setDisable(true);
    }

    private static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eroare");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mesaj");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static Optional<ButtonType> deleteConfirmationWindow() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmare");
        confirmationAlert.setHeaderText("Sunteti siguri ca doriti sa stergeti inregistrarea?");
        confirmationAlert.setContentText("Confirmati actiunea.");

        return confirmationAlert.showAndWait();
    }

    public void setStage(Stage stage){
        mainStage = stage;
    }

    private static Optional<String> requestString(String initialText, String title, String header, String context, String confirmation) {
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
}