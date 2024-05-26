package com.general.daologic;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class RequestDAO {
    // Connection object
    ConnectDAO connectDAO = new ConnectDAO();
    Connection connection = connectDAO.getConnection();

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
    public static final String REQ_EQUIPMENT_USR = "SELECT equipment.id, equipment.equipname, category.catname FROM equipment LEFT JOIN category ON equipment.categoryid = category.id ORDER BY equipment.id";
    public static final String REQ_LOGS_USR = "SELECT logs.id AS log, equipment.id AS equipment, equipname, categoryid AS category, catname, students.id AS student, name, surname, class, email, phonenumber, lenddate, returned " +
            "FROM logs LEFT JOIN equipment ON logs.equipment = equipment.id " +
            "LEFT JOIN category ON equipment.categoryid = category.id " +
            "LEFT JOIN students ON logs.student = students.id ORDER BY logs.id";

    /*
    /// -----Edit calls
     */

    public static final String RETURN_EQUIPMENT = "UPDATE logs SET returned = true WHERE id = ";

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
    public static final String UPDATE_LOGS = "UPDATE logs SET equipment = ?, student = ?, lenddate = ? WHERE id = ?";

    /*
    /// -----Connection to database
     */

    // TODO: Improve the request. Make it return either an array or data, or formated table columns
    public ResultSet requestData(String requestLine) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(requestLine);
    }

    public int executeUpdateStatement(String requestLine) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(requestLine);
    }

    public int updateCategory(String catname, int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CATEGORY)) {
            preparedStatement.setString(1, catname);
            preparedStatement.setInt(2, id);

            return preparedStatement.executeUpdate();
        }
    }

    public int updateEquipment(String equipname, int categoryid, int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EQUIPMENT)) {
            preparedStatement.setString(1, equipname);
            preparedStatement.setInt(2, categoryid);
            preparedStatement.setInt(3, id);

            return preparedStatement.executeUpdate();
        }
    }

    public int updateStudents(String name, String surname, String group, String email, String phonenumber, int id) throws SQLException {
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

    public int updateLogs(int equipment, int student, Date date, int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LOGS)) {
            preparedStatement.setInt(1, equipment);
            preparedStatement.setInt(2, student);
            preparedStatement.setDate(3, date);
            preparedStatement.setInt(4, id);

            return preparedStatement.executeUpdate();
        }
    }

    /*
    /// -----Data request
     */

    public Map<Integer, String> getCategories() throws SQLException {
        Map<Integer, String> categories = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet output = statement.executeQuery("SELECT id, catname FROM category");

        while (output.next()){
            categories.put(output.getInt("id"), output.getString("catname"));
        }

        return categories;
    }

    public Map<Integer, String> getEquipment() throws SQLException {
        Map<Integer, String> categories = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet output = statement.executeQuery("SELECT id, equipname FROM equipment");

        while (output.next()){
            categories.put(output.getInt("id"), output.getString("equipname"));
        }

        return categories;
    }

    public Map<Integer, String> getStudents() throws SQLException {
        Map<Integer, String> categories = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet output = statement.executeQuery("SELECT id, name, surname FROM students");

        while (output.next()){
            categories.put(output.getInt("id"), output.getString("name") + " " + output.getString("surname"));
        }

        return categories;
    }

    /*
    /// -----Inserttion function
     */

    public int insertCategory(String categoryName) throws SQLException {
        final String INSERT_CATEGORY = "INSERT INTO category(catname) VALUES ('" + categoryName + "')";

        Statement statement = connection.createStatement();
        return statement.executeUpdate(INSERT_CATEGORY);
    }

    public int insertEquipment(String equipmentName, int categoryID) throws SQLException {
        final String INSERT_EQUIPMENT = "INSERT INTO equipment(equipname, categoryid) VALUES ('" + equipmentName + "', " + categoryID + ")";

        Statement statement = connection.createStatement();
        return statement.executeUpdate(INSERT_EQUIPMENT);
    }

    public int insertStudent(String name, String surname, String group, String email, String phoneNr) throws SQLException {
        final String INSERT_STUDENT = "INSERT INTO students(name, surname, class, email, phonenumber) VALUES ('" + name + "', '" + surname + "', '" + group + "', '" + email + "', '" + phoneNr + "')";

        Statement statement = connection.createStatement();
        return statement.executeUpdate(INSERT_STUDENT);
    }

    public int insertLog(int equipmentID, int studentID, Date lenddate) throws SQLException {
        final String INSERT_LOG = "INSERT INTO logs(equipment, student, lenddate, returned) VALUES (" + equipmentID + ", " + studentID + ", '" + lenddate + "', false)";

        Statement statement = connection.createStatement();
        return statement.executeUpdate(INSERT_LOG);
    }
}
