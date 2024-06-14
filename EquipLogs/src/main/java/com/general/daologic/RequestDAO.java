package com.general.daologic;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class RequestDAO {
    // Connection object
    private static final ConnectDAO connectDAO = new ConnectDAO();
    private static final Connection connection = connectDAO.getConnection();

    public RequestDAO() {
    }

    /*
    /// -----Database calls
     */

    /*
    /// -----Requests
     */

    // General calls
    public static final String REQ_EQUIPMENT_ORDERED = "SELECT * FROM equipment ORDER BY id";
    public static final String REQ_CATEGORY_ORDERED = "SELECT * FROM category ORDER BY id";
    public static final String REQ_STUDENTS_ORDERED = "SELECT * FROM students ORDER BY id";
    public static final String REQ_LOGS_ORDERED = "SELECT * FROM logs ORDER BY id";
    public static final String REQ_EQUIPMENT = "SELECT * FROM equipment";
    public static final String REQ_CATEGORY = "SELECT * FROM category";
    public static final String REQ_STUDENTS = "SELECT * FROM students";
    public static final String REQ_LOGS = "SELECT * FROM logs";

    // Detailed calls
    public static final String REQ_EQUIPMENT_USR = "SELECT equipment.id, equipment.equipname, equipment.categoryid ,category.catname FROM equipment LEFT JOIN category ON equipment.categoryid = category.id ORDER BY equipment.id";
    public static final String REQ_LOGS_USR = "SELECT logs.id AS log, equipment.id AS equipment, equipname, categoryid, catname, students.id AS student, name, surname, class, email, phonenumber, lenddate, returned, returndate, notes " +
            "FROM logs LEFT JOIN equipment ON logs.equipment = equipment.id " +
            "LEFT JOIN category ON equipment.categoryid = category.id " +
            "LEFT JOIN students ON logs.student = students.id ORDER BY logs.id";

    /*
    /// -----Edit calls
     */

    public static final String RETURN_EQUIPMENT = "UPDATE logs SET returned = true WHERE id = ";
    public static final String RETURN_DATE = "UPDATE logs SET returndate = ";

    /*
    /// -----Deletion calls
     */

    public static final String DELETE_CATEGORY = "DELETE FROM category WHERE id = ";
    public static final String DELETE_EQUIPMENT = "DELETE FROM equipment WHERE id = ";
    public static final String DELETE_STUDENT = "DELETE FROM students WHERE id = ";
    public static final String DELETE_LOGS = "DELETE FROM logs WHERE id = ";

    /*
    /// -----Update calls
     */

    public static final String UPDATE_CATEGORY = "UPDATE category SET catname = ? WHERE id = ?";
    public static final String UPDATE_EQUIPMENT = "UPDATE equipment SET equipname = ?, categoryid = ? WHERE id = ?";
    public static final String UPDATE_STUDENT = "UPDATE students SET name = ?, surname = ?, class = ?, email = ?, phonenumber = ? WHERE id = ?";
    public static final String UPDATE_LOGS = "UPDATE logs SET equipment = ?, student = ?, lenddate = ?, notes = ? WHERE id = ?";

    /*
    /// -----Connection to database
     */

    public static Optional<ResultSet> requestData(String requestLine) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(requestLine);

            if (resultSet.next()) {
                return Optional.of(resultSet);
            } else {
                resultSet.close();
                statement.close();
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            return Optional.empty();
        }
    }

    public static int executeUpdateStatement(String requestLine) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(requestLine);
    }

    public static int updateCategory(String catname, int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CATEGORY)) {
            preparedStatement.setString(1, catname);
            preparedStatement.setInt(2, id);

            return preparedStatement.executeUpdate();
        }
    }

    public static int updateEquipment(String equipname, int categoryid, int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EQUIPMENT)) {
            preparedStatement.setString(1, equipname);
            preparedStatement.setInt(2, categoryid);
            preparedStatement.setInt(3, id);

            return preparedStatement.executeUpdate();
        }
    }

    public static int updateStudents(String name, String surname, String group, String email, String phonenumber, int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STUDENT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, group);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, phonenumber);
            preparedStatement.setInt(6, id);

            return preparedStatement.executeUpdate();
        }
    }

    public static int updateLogs(int equipment, int student, Date date, String note, int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LOGS)) {
            preparedStatement.setInt(1, equipment);
            preparedStatement.setInt(2, student);
            preparedStatement.setDate(3, date);
            if (note.isEmpty()) {
                preparedStatement.setString(4, null);
            } else {
                preparedStatement.setString(4, note);
            }
            preparedStatement.setInt(5, id);

            return preparedStatement.executeUpdate();
        }
    }

    /*
    /// -----Data request
     */

    public static Map<Integer, String> getCategories() throws SQLException {
        Map<Integer, String> categories = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet output = statement.executeQuery("SELECT id, catname FROM category ORDER BY id");

        while (output.next()){
            categories.put(output.getInt("id"), output.getString("catname"));
        }

        return categories;
    }

    public static Map<Integer, String> getClasses() throws SQLException {
        Map<Integer, String> classes = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet output = statement.executeQuery("SELECT id, class FROM students ORDER BY class");

        while (output.next()){
            classes.put(output.getInt("id"), output.getString("class"));
        }

        Map<Integer, String> uniqueClasses = new HashMap<>();

        Set<String> uniqueValues = new HashSet<>();

        for (Map.Entry<Integer, String> entry : classes.entrySet()) {
            if (!uniqueValues.contains(entry.getValue())) {
                uniqueClasses.put(entry.getKey(), entry.getValue());
                uniqueValues.add(entry.getValue());
            }
        }

        return uniqueClasses;
    }

    public static Map<Integer, String> getEquipment() throws SQLException {
        Map<Integer, String> equipment = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet output = statement.executeQuery("SELECT id, equipname FROM equipment");

        while (output.next()){
            equipment.put(output.getInt("id"), output.getString("equipname"));
        }

        return equipment;
    }

    public static Map<Integer, String> getEquipmentByCategory(int id) throws SQLException {
        Map<Integer, String> equipment = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet output = statement.executeQuery("SELECT id, equipname FROM equipment WHERE categoryid = " + id);

        while (output.next()){
            equipment.put(output.getInt("id"), output.getString("equipname"));
        }

        return equipment;
    }

    public static Map<Integer, String> getStudents() throws SQLException {
        Map<Integer, String> logs = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet output = statement.executeQuery("SELECT id, name, surname FROM students");

        while (output.next()){
            logs.put(output.getInt("id"), output.getString("name") + " " + output.getString("surname"));
        }

        return logs;
    }

    /*
    /// -----Inserttion function
     */

    public static int insertCategory(String categoryName) throws SQLException {
        final String INSERT_CATEGORY = "INSERT INTO category(catname) VALUES ('" + categoryName + "')";

        Statement statement = connection.createStatement();
        return statement.executeUpdate(INSERT_CATEGORY);
    }

    public static int insertEquipment(String equipmentName, int categoryID) throws SQLException {
        final String INSERT_EQUIPMENT = "INSERT INTO equipment(equipname, categoryid) VALUES ('" + equipmentName + "', " + categoryID + ")";

        Statement statement = connection.createStatement();
        return statement.executeUpdate(INSERT_EQUIPMENT);
    }

    public static int insertStudent(String name, String surname, String group, String email, String phoneNr) throws SQLException {
        final String INSERT_STUDENT = "INSERT INTO students(name, surname, class, email, phonenumber) VALUES ('" + name + "', '" + surname + "', '" + group + "', '" + email + "', '" + phoneNr + "')";

        Statement statement = connection.createStatement();
        return statement.executeUpdate(INSERT_STUDENT);
    }

    public static int insertLog(int equipmentID, int studentID, Date lenddate) throws SQLException {
        final String INSERT_LOG = "INSERT INTO logs(equipment, student, lenddate, returned) VALUES (" + equipmentID + ", " + studentID + ", '" + lenddate + "', false)";

        Statement statement = connection.createStatement();
        return statement.executeUpdate(INSERT_LOG);
    }
}
