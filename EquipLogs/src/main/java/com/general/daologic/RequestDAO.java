package com.general.daologic;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class RequestDAO {
    // Connection object
    ConnectDAO connectDAO = new ConnectDAO();
    Connection connection = connectDAO.getConnection();

    /*
    /// -----Database calls
     */

    /*
    /// -----Requests
     */

    // General calls
    public static final String REQ_EQUIPMENT = "SELECT * FROM equipment ORDER BY id";
    public static final String REQ_CATEGORY = "SELECT * FROM category ORDER BY id";
    public static final String REQ_STUDENTS = "SELECT * FROM students ORDER BY id";
    public static final String REQ_LOGS = "SELECT * FROM logs ORDER BY id";

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

    public RequestDAO() throws IOException {
    }

    /*
    /// -----Connection to database
     */
    // TODO: Improve the request. Make it return either an array or data, or formated table columns
    public ResultSet requestData(String requestLine) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(requestLine);
    }

    public int updateCall(String requestLine) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(requestLine);
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
