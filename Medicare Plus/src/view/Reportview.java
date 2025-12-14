package view;

import dao.ReportDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Reportview extends JFrame {
    private JPanel mainPanel;
    private JTextField monthField;
    private JButton generateButton;
    private JTextArea reportArea;

    private ReportDAO reportDAO = new ReportDAO();

    public Reportview() {
        setTitle("Monthly Reports");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel); // Add this line!
        setLocationRelativeTo(null);

        // Set report area properties
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Action Listener
        generateButton.addActionListener(e -> generateReport());
    }

    private void generateReport() {
        try {
            String month = monthField.getText().trim();

            if (month.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a month (YYYY-MM)");
                return;
            }

            StringBuilder report = new StringBuilder();
            report.append("========================================\n");
            report.append("     MONTHLY REPORT FOR ").append(month).append("\n");
            report.append("========================================\n\n");

            // Appointment Volume
            report.append("1. APPOINTMENT VOLUMES:\n");
            report.append("   ----------------------------\n");
            Map<String, Integer> volumes = reportDAO.getMonthlyAppointmentVolume(month);
            if (volumes.isEmpty()) {
                report.append("   No appointments found.\n");
            } else {
                for (Map.Entry<String, Integer> entry : volumes.entrySet()) {
                    report.append(String.format("   %-15s: %d\n", entry.getKey(), entry.getValue()));
                }
            }
            report.append("\n");

            // Doctor Performance
            report.append("2. DOCTOR PERFORMANCE:\n");
            report.append("   ----------------------------\n");
            Map<String, Integer> performance = reportDAO.getDoctorPerformance(month);
            if (performance.isEmpty()) {
                report.append("   No completed appointments found.\n");
            } else {
                for (Map.Entry<String, Integer> entry : performance.entrySet()) {
                    report.append(String.format("   %-20s: %d completed\n", entry.getKey(), entry.getValue()));
                }
            }
            report.append("\n");

            // Patient Visits
            report.append("3. PATIENT VISITS:\n");
            report.append("   ----------------------------\n");
            int visits = reportDAO.getPatientVisits(month);
            report.append(String.format("   Unique Patients: %d\n", visits));

            report.append("\n========================================\n");

            reportArea.setText(report.toString());

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
        inputPanel.add(new JLabel("Month (YYYY-MM):"));
        monthField = new JTextField(10);
        inputPanel.add(monthField);
        generateButton = new JButton("Generate Report");
        inputPanel.add(generateButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Report Area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        mainPanel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
    }

    {
        initUI();
    }

}
