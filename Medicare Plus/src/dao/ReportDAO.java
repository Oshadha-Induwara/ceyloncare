package dao;

import db.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReportDAO {

    public Map<String, Integer> getMonthlyAppointmentVolume(String month) throws SQLException {
        Map<String, Integer> report = new HashMap<>();
        String sql = "SELECT status, COUNT(*) as count FROM appointment WHERE date LIKE ? GROUP BY status";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, month + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                report.put(rs.getString("status"), rs.getInt("count"));
            }
        }
        return report;
    }

    public Map<String, Integer> getDoctorPerformance(String month) throws SQLException {
        Map<String, Integer> report = new HashMap<>();
        String sql = """
                SELECT d.name, COUNT(a.id) as count
                FROM appointment a
                JOIN doctor d ON a.doctor_id = d.id
                WHERE a.date LIKE ? AND a.status='Completed'
                GROUP BY d.name
                """;

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, month + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                report.put(rs.getString("name"), rs.getInt("count"));
            }
        }
        return report;
    }

    public int getPatientVisits(String month) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT patient_id) as count FROM appointment WHERE date LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, month + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }
}
