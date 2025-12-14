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

    {

        $$$setupUI$$$();
    }

    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        titleLable = new JLabel();
        titleLable.setText("Label");
        panel1.add(titleLable, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        patientButton = new JButton();
        patientButton.setText("Patient");
        panel1.add(patientButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        doctorButton = new JButton();
        doctorButton.setText("Doctor");
        panel1.add(doctorButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appointmentButton = new JButton();
        appointmentButton.setText("Book Appointment");
        panel1.add(appointmentButton, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reportButton = new JButton();
        reportButton.setText("Report");
        panel1.add(reportButton, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        notificationButton = new JButton();
        notificationButton.setText("Notification");
        panel1.add(notificationButton, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
}