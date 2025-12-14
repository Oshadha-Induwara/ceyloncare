package view;

import db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;

public class Mainmenu extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLable;
    private JButton patientButton;
    private JButton doctorButton;
    private JButton appointmentButton;
    private JButton reportButton;
    private JButton notificationButton;

    public Mainmenu() {
        setTitle("Medicare Plus - Main Menu");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize UI
        initUI();

        // Action Listeners
        patientButton.addActionListener(e -> {
            new PatientView().setVisible(true);
        });

        doctorButton.addActionListener(e -> {
            new DoctorView().setVisible(true);
        });

        appointmentButton.addActionListener(e -> {
            new AppointmentView().setVisible(true);
        });

        reportButton.addActionListener(e -> {
            new Reportview().setVisible(true);
        });

        notificationButton.addActionListener(e -> {
            new Notificationview().setVisible(true);
        });
    }

    private void initUI() {
        mainPanel = new JPanel(new GridBagLayout());
        setContentPane(mainPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        titleLable = new JLabel("Medicare Plus System", SwingConstants.CENTER);
        titleLable.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLable, gbc);

        patientButton = new JButton("Patient Management");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(patientButton, gbc);

        doctorButton = new JButton("Doctor Management");
        gbc.gridx = 1;
        mainPanel.add(doctorButton, gbc);

        appointmentButton = new JButton("Book Appointment");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(appointmentButton, gbc);

        reportButton = new JButton("Reports");
        gbc.gridx = 1;
        mainPanel.add(reportButton, gbc);

        notificationButton = new JButton("Notifications");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(notificationButton, gbc);
    }

    public static void main(String[] args) {
        // Initialize Database Tables
        DatabaseConnection.initializeTables();

        // Run Application
        SwingUtilities.invokeLater(() -> {
            new Mainmenu().setVisible(true);
        });
    }


}