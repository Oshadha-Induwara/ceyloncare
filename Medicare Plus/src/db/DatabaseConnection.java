package db;

import dao.AppointmentDAO;
import dao.DoctorDAO;
import dao.NotificationDAO;
import dao.PatientDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:medicare.db";

    // Establishing the connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Initialize all tables
    public static void initializeTables() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                PatientDAO.createTable(conn);
                DoctorDAO.createTable(conn);
                AppointmentDAO.createTable(conn);
                NotificationDAO.createTable(conn);
                System.out.println("All tables initialized successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
