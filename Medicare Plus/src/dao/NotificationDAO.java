package dao;

import db.DatabaseConnection;
import model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public static void createTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS notification (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER,
                message TEXT,
                date TEXT,
                sent INTEGER DEFAULT 0,
                FOREIGN KEY(patient_id) REFERENCES patient(id)
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addNotification(Notification n) throws SQLException {
        String sql = "INSERT INTO notification (patient_id, message, date, sent) VALUES (?,?,?,?)";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, n.getPatientId());
            ps.setString(2, n.getMessage());
            ps.setString(3, n.getDate());
            ps.setInt(4, n.isSent() ? 1 : 0);
            ps.executeUpdate();
        }
    }

    public List<Notification> getNotificationsByPatient(int patientId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE patient_id=? ORDER BY date DESC";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("message"),
                        rs.getString("date"),
                        rs.getInt("sent") == 1));
            }
        }
        return list;
    }

    public void markAsSent(int id) throws SQLException {
        String sql = "UPDATE notification SET sent=1 WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
