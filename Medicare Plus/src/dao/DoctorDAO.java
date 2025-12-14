package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import model.Doctor;

public class DoctorDAO {
    public static void createTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS doctor (
                id INTEGER PRIMARY KEY,
                name TEXT,
                speciality TEXT,
                available_time TEXT
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addDoctor(Doctor d) throws SQLException {
        String sql = "INSERT INTO doctor (id,name,speciality,available_time) VALUES (?,?,?,?)";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, d.getId());
            ps.setString(2, d.getName());
            ps.setString(3, d.getSpeciality());
            ps.setString(4, d.getAvailable_time());
            ps.executeUpdate();
        }
    }

    public void updateDoctor(Doctor d) throws SQLException {
        String sql = "UPDATE doctor SET name=?, speciality=?, available_time=? WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, d.getName());
            ps.setString(2, d.getSpeciality());
            ps.setString(3, d.getAvailable_time());
            ps.setInt(4, d.getId());
            ps.executeUpdate();
        }
    }

    public void deleteDoctor(int id) throws SQLException {
        String sql = "DELETE FROM doctor WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctor";

        try (Connection con = DatabaseConnection.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("speciality"),
                        rs.getString("available_time")));
            }
        }
        return list;
    }

    public int getNextId() throws SQLException {
        String sql = "SELECT MAX(id) as max_id FROM doctor";
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
