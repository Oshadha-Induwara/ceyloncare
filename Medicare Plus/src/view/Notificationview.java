package view;

import dao.NotificationDAO;
import dao.PatientDAO;
import model.Notification;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Notificationview extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> patientCombo;
    private JButton viewButton;
    private JTable notificationTable;

    private NotificationDAO notificationDAO = new NotificationDAO();
    private PatientDAO patientDAO = new PatientDAO();

    public Notificationview() {
        setTitle("Patient Notifications");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel); // Add this line!
        setLocationRelativeTo(null);

        // Initialize table
        initializeTable();

        loadPatients();

        // Action Listener
        viewButton.addActionListener(e -> viewNotifications());
    }

    private void initializeTable() {
        String[] columns = {"ID", "Message", "Date", "Sent"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        notificationTable.setModel(model);
    }

    private void loadPatients() {
        patientCombo.removeAllItems();
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            if (patients.isEmpty()) {
                patientCombo.addItem("No patients available");
            } else {
                for (Patient p : patients) {
                    patientCombo.addItem(p.getId() + " - " + p.getName());
                }
            }
        } catch (Exception e) {
            patientCombo.addItem("Error loading patients");
            JOptionPane.showMessageDialog(this, "Error loading patients: " + e.getMessage());
        }
    }

    private void viewNotifications() {
        try {
            String patientStr = (String) patientCombo.getSelectedItem();

            if (patientStr == null || patientStr.equals("No patients available")) {
                JOptionPane.showMessageDialog(this, "Please add patients first");
                return;
            }

            int patientId = Integer.parseInt(patientStr.split(" - ")[0]);

            DefaultTableModel model = (DefaultTableModel) notificationTable.getModel();
            model.setRowCount(0);

            List<Notification> notifications = notificationDAO.getNotificationsByPatient(patientId);

            if (notifications.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No notifications found for this patient");
            }

            for (Notification n : notifications) {
                model.addRow(new Object[]{
                        n.getId(),
                        n.getMessage(),
                        n.getDate(),
                        n.isSent() ? "Yes" : "No"
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Patient:"));
        patientCombo = new JComboBox<>();
        inputPanel.add(patientCombo);
        viewButton = new JButton("View Notifications");
        inputPanel.add(viewButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Table
        notificationTable = new JTable();
        mainPanel.add(new JScrollPane(notificationTable), BorderLayout.CENTER);
    }

    {
        initUI();
    }

}
