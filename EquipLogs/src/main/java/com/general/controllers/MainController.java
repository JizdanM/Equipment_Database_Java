package com.general.controllers;

import com.general.daologic.RequestDAO;
import com.general.entity.*;
import com.general.equiplogs.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MainController {
    private static String selectedTable = "";

    @FXML
    private TableView<Equipment> equipTable;
    @FXML
    TableColumn<Equipment, Integer> equipIDColumn = new TableColumn<>("ID");
    @FXML
    TableColumn<Equipment, String> equipNameColumn = new TableColumn<>("Echipament");
    @FXML
    TableColumn<Equipment, String> equipCatColumn = new TableColumn<>("Categorie");

    @FXML
    private TableView<Category> catTable;
    @FXML
    TableColumn<Category, Integer> catIDColumn = new TableColumn<>("ID");
    @FXML
    TableColumn<Category, String> catNameColumn = new TableColumn<>("Categorie");

    @FXML
    private TableView<Student> studTable;
    @FXML
    TableColumn<Student, Integer> studIDColumn = new TableColumn<>("ID");
    @FXML
    TableColumn<Student, String> studNameColumn = new TableColumn<>("Nume");
    @FXML
    TableColumn<Student, String> studSurnameColumn = new TableColumn<>("Prenume");
    @FXML
    TableColumn<Student, String> studGroupColumn = new TableColumn<>("Grupa");
    @FXML
    TableColumn<Student, String> studEmailColumn = new TableColumn<>("Email");
    @FXML
    TableColumn<Student, String> studPhoneNumberColumn = new TableColumn<>("NrTelefon");

    @FXML
    private TableView<Logs> logTable;
    @FXML
    TableColumn<Logs, Integer> logIDColumn = new TableColumn<>("ID");
    @FXML
    TableColumn<Logs, String> logEquipColumn = new TableColumn<>("Echipament");
    @FXML
    TableColumn<Logs, String> logCatColumn = new TableColumn<>("Categorie");
    @FXML
    TableColumn<Logs, String> logStudNameColumn = new TableColumn<>("Nume");
    @FXML
    TableColumn<Logs, String> logStudSurnameColumn = new TableColumn<>("Prenume");
    @FXML
    TableColumn<Logs, String> studentClassColumn = new TableColumn<>("Clasa");
    @FXML
    TableColumn<Logs, String> logEmailColumn = new TableColumn<>("Email");
    @FXML
    TableColumn<Logs, String> logPhoneNumberColumn = new TableColumn<>("NrTelefon");
    @FXML
    TableColumn<Logs, Date> logLendDateColumn = new TableColumn<>("Data imprumutarii");
    @FXML
    TableColumn<Logs, Boolean> logReturnedColumn = new TableColumn<>("Returnat");
    @FXML
    TableColumn<Logs, Date> logReturnDateColumn = new TableColumn<>("Data returnării");

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
    private TextField searchField;

    @FXML
    private Button btnReturn;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private Stage mainStage;

    private ObservableList<Equipment> equipmentCache;
    private ObservableList<Category> categroyCache;
    private ObservableList<Student> studentCache;
    private ObservableList<Logs> logsCache;

    /*
    /// -----Constructors / Initializers
     */

    @FXML
    private void initialize() {
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

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchTable(oldValue, newValue);
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
            e.printStackTrace(System.out);
        }

        // TODO: Create a simple setting window (Language / Theme etc.)
    }

    @FXML
    protected void close() {
        System.exit(0);
    }

    @FXML
    protected void showEquipment() {
        catTable.setDisable(true);
        catTable.setVisible(false);
        studTable.setDisable(true);
        studTable.setVisible(false);
        logTable.setDisable(true);
        logTable.setVisible(false);

        equipTable.setVisible(true);
        equipTable.setDisable(false);
        try {
            ResultSet output = new RequestDAO().requestData(RequestDAO.REQ_EQUIPMENT_USR);
            ObservableList<Equipment> equipList = FXCollections.observableArrayList();

            equipTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            while(output.next()){
                Equipment equip = new Equipment(output.getInt("id"), output.getString("equipname"), output.getString("catname"));
                equipList.add(equip);
            }

            equipIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            equipNameColumn.setCellValueFactory(new PropertyValueFactory<>("equipName"));
            equipCatColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

            equipTable.setItems(equipList);
            selectedTable = "equipment";
            equipmentCache = equipList;

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @FXML
    protected void showCategory() {
        equipTable.setDisable(true);
        equipTable.setVisible(false);
        studTable.setDisable(true);
        studTable.setVisible(false);
        logTable.setDisable(true);
        logTable.setVisible(false);

        catTable.setDisable(false);
        catTable.setVisible(true);
        try {
            catIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            catNameColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

            ResultSet output = new RequestDAO().requestData(RequestDAO.REQ_CATEGORY_ORDERED);
            ObservableList<Category> categoryList = FXCollections.observableArrayList();

            catTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            while(output.next()){
                Category category = new Category(output.getInt("id"), output.getString("catname"));
                categoryList.add(category);
            }

            catTable.setItems(categoryList);
            selectedTable = "category";
            categroyCache = categoryList;

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @FXML
    protected void showStudents() {
        equipTable.setDisable(true);
        equipTable.setVisible(false);
        catTable.setDisable(true);
        catTable.setVisible(false);
        logTable.setDisable(true);
        logTable.setVisible(false);

        studTable.setDisable(false);
        studTable.setVisible(true);
        try {
            ResultSet output = new RequestDAO().requestData(RequestDAO.REQ_STUDENTS_ORDERED);
            ObservableList<Student> studentList = FXCollections.observableArrayList();

            studTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            while(output.next()){
                Student student = new Student(output.getInt("id"), output.getString("name")
                        , output.getString("surname"), output.getString("class")
                        , output.getString("email"), output.getString("phonenumber"));
                studentList.add(student);
            }

            studIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            studNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            studSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
            studGroupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
            studEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            studPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNr"));

            studTable.setItems(studentList);
            selectedTable = "students";
            studentCache = studentList;

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @FXML
    protected void showLogs() {
        equipTable.setDisable(true);
        equipTable.setVisible(false);
        catTable.setDisable(true);
        catTable.setVisible(false);
        studTable.setDisable(true);
        studTable.setVisible(false);

        logTable.setDisable(false);
        logTable.setVisible(true);
        try {

            ResultSet output = new RequestDAO().requestData(RequestDAO.REQ_LOGS_USR);
            ObservableList<Logs> logList = FXCollections.observableArrayList();

            logTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
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

            logIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            logEquipColumn.setCellValueFactory(cellData -> {
                Equipment equipment = cellData.getValue().getEquipment();
                if (equipment != null) {
                    return new SimpleStringProperty(equipment.getEquipName());
                }
                return new SimpleStringProperty("");
            });
            logCatColumn.setCellValueFactory(cellData -> {
                Equipment equipment = cellData.getValue().getEquipment();
                if (equipment != null) {
                    return new SimpleStringProperty(equipment.getCategory());
                }
                return new SimpleStringProperty("");
            });
            logStudNameColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue().getStudent();
                if (student != null) {
                    return new SimpleStringProperty(student.getName());
                }
                return new SimpleStringProperty("");
            });
            logStudSurnameColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue().getStudent();
                if (student != null) {
                    return new SimpleStringProperty(student.getSurname());
                }
                return new SimpleStringProperty("");
            });
            studentClassColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue().getStudent();
                if (student != null) {
                    return new SimpleStringProperty(student.getGroup());
                }
                return new SimpleStringProperty("");
            });
            logEmailColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue().getStudent();
                if (student != null) {
                    return new SimpleStringProperty(student.getEmail());
                }
                return new SimpleStringProperty("");
            });
            logPhoneNumberColumn.setCellValueFactory(cellData -> {
                Student student = cellData.getValue().getStudent();
                if (student != null) {
                    return new SimpleStringProperty(student.getPhoneNr());
                }
                return new SimpleStringProperty("");
            });
            logLendDateColumn.setCellValueFactory(new PropertyValueFactory<>("lendDate"));
            logReturnedColumn.setCellValueFactory(new PropertyValueFactory<>("returned"));
            logReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

            logTable.setItems(logList);
            selectedTable = "logs";
            logsCache = logList;

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
                            e.printStackTrace(System.out);
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
    /// Seach bar
     */

    protected void searchTable(String oldValue, String newValue){
        switch (selectedTable) {
            case "equipment": {
                FilteredList<Equipment> filteredData = new FilteredList<>(equipmentCache, b -> true);

                filteredData.setPredicate(equipment -> {
                    if(newValue.isEmpty() || oldValue.isBlank() || newValue == null || oldValue.contains(newValue)) {
                        return true;
                    }

                    String keyword = newValue.toLowerCase();

                    if (String.valueOf(equipment.getId()).toLowerCase().contains(keyword)){
                        return true;
                    } else if(equipment.getCategory().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(equipment.getEquipName().toLowerCase().contains(keyword)) {
                        return true;
                    } else {
                        return false;
                    }
                });

                SortedList<Equipment> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(equipTable.comparatorProperty());
                equipTable.setItems(sortedData);
            }
            break;
            case "category": {
                FilteredList<Category> filteredData = new FilteredList<>(categroyCache, b -> true);

                filteredData.setPredicate(category -> {
                    if(newValue.isEmpty() || oldValue.isBlank() || newValue == null || oldValue.contains(newValue)) {
                        return true;
                    }

                    String keyword = newValue.toLowerCase();

                    if (String.valueOf(category.getId()).toLowerCase().contains(keyword)){
                        return true;
                    } else if(category.getCategory().toLowerCase().contains(keyword)) {
                        return true;
                    } else {
                        return false;
                    }
                });

                SortedList<Category> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(catTable.comparatorProperty());
                catTable.setItems(sortedData);
            }
            break;
            case "students": {
                FilteredList<Student> filteredData = new FilteredList<>(studentCache, b -> true);

                filteredData.setPredicate(student -> {
                    if(newValue.isEmpty() || oldValue.isBlank() || newValue == null || oldValue.contains(newValue)) {
                        return true;
                    }

                    String keyword = newValue.toLowerCase();

                    if (String.valueOf(student.getId()).toLowerCase().contains(keyword)){
                        return true;
                    } else if(student.getName().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(student.getSurname().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(student.getGroup().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(student.getEmail().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(student.getPhoneNr().toLowerCase().contains(keyword)) {
                        return true;
                    } else {
                        return false;
                    }
                });

                SortedList<Student> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(studTable.comparatorProperty());
                studTable.setItems(sortedData);
            }
            break;
            case "logs": {
                FilteredList<Logs> filteredData = new FilteredList<>(logsCache, b -> true);

                filteredData.setPredicate(logs -> {
                    if(newValue.isEmpty() || oldValue.isBlank() || newValue == null || oldValue.contains(newValue)) {
                        return true;
                    }

                    String keyword = newValue.toLowerCase();

                    if (String.valueOf(logs.getId()).toLowerCase().contains(keyword)){
                        return true;
                    } else if(logs.getEquipment().getEquipName().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(logs.getStudent().getName().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(logs.getStudent().getSurname().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(logs.getStudent().getGroup().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(logs.getStudent().getEmail().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(logs.getStudent().getPhoneNr().toLowerCase().contains(keyword)) {
                        return true;
                    } else if(String.valueOf(logs.getLendDate()).toLowerCase().contains(keyword)) {
                        return true;
                    } else if(String.valueOf(logs.isReturned()).toLowerCase().contains(keyword)) {
                        return true;
                    } else if(String.valueOf(logs.getReturnDate()).toLowerCase().contains(keyword)) {
                        return true;
                    } else {
                        return false;
                    }
                });

                SortedList<Logs> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(logTable.comparatorProperty());
                logTable.setItems(sortedData);
            }
            break;
        }
    }

    /*
    /// -----Interaction buttons functions
     */

    @FXML
    protected void returnedBtnClick() {
        try {
            Logs selectedLog = logTable.getSelectionModel().getSelectedItem();

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