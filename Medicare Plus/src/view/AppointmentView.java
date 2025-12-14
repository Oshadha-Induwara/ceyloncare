package view;

import dao.AppointmentDAO;
import dao.DoctorDAO;
import dao.NotificationDAO;
import dao.PatientDAO;
import model.Appointment;
import model.Doctor;
import model.Notification;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AppointmentView extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> patientcombo;
    private JComboBox<String> doctorcombo;
    private JTextField datefield;
    private JButton bookbutton;
    private JTextField appointmenttfield;
    private JComboBox<String> statuscombo;
    private JButton updatebutton;
    private JTable appointmenttable;
    private JButton refreshbutton;

    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    public AppointmentView() {
        setTitle("Appointment Management");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);


        statuscombo.addItem("Scheduled");
        statuscombo.addItem("Completed");
        statuscombo.addItem("Canceled");
        statuscombo.addItem("Delayed");

        initializeTable();


        loadPatients();
        loadDoctors();
        refreshTable();

        // Action Listeners
        bookbutton.addActionListener(e -> bookAppointment());
        updatebutton.addActionListener(e -> updateStatus());
        refreshbutton.addActionListener(e -> refreshTable());

        appointmenttable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && appointmenttable.getSelectedRow() >= 0) {
                int selectedRow = appointmenttable.getSelectedRow();
                appointmenttfield.setText(appointmenttable.getValueAt(selectedRow, 0).toString());
            }
        });
    }

    private void initializeTable() {
        String[] columns = {"ID", "Patient ID", "Doctor ID", "Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        appointmenttable.setModel(model);
    }

    private void loadPatients() {
        patientcombo.removeAllItems();
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            if (patients.isEmpty()) {
                patientcombo.addItem("No patients available");
            } else {
                for (Patient p : patients) {
                    patientcombo.addItem(p.getId() + " - " + p.getName());
                }
            }
        } catch (Exception e) {
            patientcombo.addItem("Error loading patients");
            JOptionPane.showMessageDialog(this, "Error loading patients: " + e.getMessage());
        }
    }

    private void loadDoctors() {
        doctorcombo.removeAllItems();
        try {
            List<Doctor> doctors = doctorDAO.getAllDoctors();
            if (doctors.isEmpty()) {
                doctorcombo.addItem("No doctors available");
            } else {
                for (Doctor d : doctors) {
                    doctorcombo.addItem(d.getId() + " - " + d.getName() + " (" + d.getAvailable_time() + ")");
                }
            }
        } catch (Exception e) {
            doctorcombo.addItem("Error loading doctors");
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + e.getMessage());
        }
    }

    private void bookAppointment() {
        try {
            String patientStr = (String) patientcombo.getSelectedItem();
            String doctorStr = (String) doctorcombo.getSelectedItem();

            if (patientStr == null || doctorStr == null ||
                    patientStr.equals("No patients available") ||
                    doctorStr.equals("No doctors available") ||
                    patientStr.equals("Error loading patients") ||
                    doctorStr.equals("Error loading doctors")) {
                JOptionPane.showMessageDialog(this, "Please ensure patients and doctors are added first");
                return;
            }

            int patientId = Integer.parseInt(patientStr.split(" - ")[0]);
            int doctorId = Integer.parseInt(doctorStr.split(" - ")[0]);
            String date = datefield.getText().trim();

            if (date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a date (YYYY-MM-DD)");
                return;
            }

            int newId = appointmentDAO.getNextId();
            Appointment appointment = new Appointment(newId, patientId, doctorId, date, "Scheduled");
            appointmentDAO.addAppointment(appointment);

            // Send notification
            String message = "Appointment scheduled for " + date + " with Doctor ID: " + doctorId;
            Notification notification = new Notification(0, patientId, message, LocalDate.now().toString(), false);
            notificationDAO.addNotification(notification);

            JOptionPane.showMessageDialog(this, "Appointment booked successfully!\nNotification sent to patient.");
            datefield.setText("");
            refreshTable();
            loadPatients();
            loadDoctors();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateStatus() {
        try {
            String idText = appointmenttfield.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an appointment ID or select from table");
                return;
            }

            int id = Integer.parseInt(idText);
            String newStatus = (String) statuscombo.getSelectedItem();

            appointmentDAO.updateAppointmentStatus(id, newStatus);

            // Get appointment details and send notification
            List<Appointment> appointments = appointmentDAO.getAllAppointments();
            for (Appointment a : appointments) {
                if (a.getId() == id) {
                    String message = "Appointment #" + id + " status updated to: " + newStatus;
                    Notification notification = new Notification(0, a.getPatient_id(), message,
                            LocalDate.now().toString(), false);
                    notificationDAO.addNotification(notification);
                    break;
                }
            }

            JOptionPane.showMessageDialog(this, "Status updated successfully!\nNotification sent to patient.");
            appointmenttfield.setText("");
            refreshTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid appointment ID");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) appointmenttable.getModel();
        model.setRowCount(0);

        try {
            List<Appointment> appointments = appointmentDAO.getAllAppointments();
            for (Appointment a : appointments) {
                model.addRow(new Object[]{
                        a.getId(),
                        a.getPatient_id(),
                        a.getDoctor_id(),
                        a.getDate(),
                        a.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + e.getMessage());
        }
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Left Column (Booking)
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        patientcombo = new JComboBox<>();
        inputPanel.add(patientcombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        doctorcombo = new JComboBox<>();
        inputPanel.add(doctorcombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        datefield = new JTextField(15);
        inputPanel.add(datefield, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        bookbutton = new JButton("Book Appointment");
        inputPanel.add(bookbutton, gbc);

        // Right Column (Update Status)
        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Appointment ID:"), gbc);
        gbc.gridx = 3;
        appointmenttfield = new JTextField(10);
        inputPanel.add(appointmenttfield, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3;
        statuscombo = new JComboBox<>();
        inputPanel.add(statuscombo, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        updatebutton = new JButton("Update Status");
        inputPanel.add(updatebutton, gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        refreshbutton = new JButton("Refresh Table");
        inputPanel.add(refreshbutton, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Table
        appointmenttable = new JTable();
        mainPanel.add(new JScrollPane(appointmenttable), BorderLayout.CENTER);
    }

    {
        initUI();
    }

}