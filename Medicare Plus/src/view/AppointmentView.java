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

        // Initialize status combo
        statuscombo.addItem("Scheduled");
        statuscombo.addItem("Completed");
        statuscombo.addItem("Canceled");
        statuscombo.addItem("Delayed");

        // Initialize table
        initializeTable();

        // Load data
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        patientcombo = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        patientcombo.setModel(defaultComboBoxModel1);
        panel1.add(patientcombo, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        doctorcombo = new JComboBox();
        panel1.add(doctorcombo, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        datefield = new JTextField();
        panel1.add(datefield, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        bookbutton = new JButton();
        bookbutton.setText("Book Appointment");
        panel1.add(bookbutton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appointmenttfield = new JTextField();
        panel1.add(appointmenttfield, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        statuscombo = new JComboBox();
        panel1.add(statuscombo, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updatebutton = new JButton();
        updatebutton.setText("Update Status");
        panel1.add(updatebutton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appointmenttable = new JTable();
        panel1.add(appointmenttable, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        refreshbutton = new JButton();
        refreshbutton.setText("Refresh Table");
        panel1.add(refreshbutton, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
}