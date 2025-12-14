package dao;

import db.DatabaseConnection;
import model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    public static void createTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS appointment (
                id INTEGER PRIMARY KEY,
                patient_id INTEGER,
                doctor_id INTEGER,
                date TEXT,
                status TEXT,
                    FOREIGN KEY(patient_id) REFERENCES patient(id),
                    FOREIGN KEY(doctor_id) REFERENCES doctor(id)
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addAppointment(Appointment a) throws SQLException {
        String sql = "INSERT INTO appointment (id,patient_id,doctor_id,date,status) VALUES (?,?,?,?,?)";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getId());
            ps.setInt(2, a.getPatient_id());
            ps.setInt(3, a.getDoctor_id());
            ps.setString(4, a.getDate());
            ps.setString(5, a.getStatus());
            ps.executeUpdate();
        }
    }

    public void updateAppointmentStatus(int id, String status) throws SQLException {
        String sql = "UPDATE appointment SET status=? WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void deleteAppointment(int id) throws SQLException {
        String sql = "DELETE FROM appointment WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointment";

        try (Connection con = DatabaseConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("date"),
                        rs.getString("status")));
            }
        }
        return list;
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) throws SQLException {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE patient_id=?";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("date"),
                        rs.getString("status")));
            }
        }
        return list;
    }

    public int getNextId() throws SQLException {
        String sql = "SELECT MAX(id) as max_id FROM appointment";
        try (Connection con = DatabaseConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        }
        return 1;
    }
}
