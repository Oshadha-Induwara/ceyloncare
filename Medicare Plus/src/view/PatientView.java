package view;

import dao.PatientDAO;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientView extends JFrame {
    private JPanel mainPanel; // Add this line!
    private JTextField idField;
    private JTextField nameField;
    private JTextField contactField;
    private JTextArea mediHistoryArea;
    private JButton addButton;
    private JButton updateButton;
    private JButton removeButton;
    private JTable patientTable;

    private PatientDAO dao = new PatientDAO();

    public PatientView() {
        setTitle("Patient Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel); // Add this line!
        setLocationRelativeTo(null);

        // Initialize table
        initializeTable();

        // Set ID field non-editable
        idField.setEditable(false);

        // Action Listeners
        addButton.addActionListener(e -> addPatient());
        updateButton.addActionListener(e -> updatePatient());
        removeButton.addActionListener(e -> removePatient());

        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedPatient();
            }
        });

        refreshTable();
        setNextId();
    }

    private void initializeTable() {
        String[] columns = {"ID", "Name", "Contact", "Medical History"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        patientTable.setModel(model);
    }

    private void setNextId() {
        try {
            int nextId = dao.getNextId();
            idField.setText(String.valueOf(nextId));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching next ID: " + e.getMessage());
        }
    }

    private void addPatient() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String mediHistory = mediHistoryArea.getText().trim();

            if (name.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill Name and Contact fields");
                return;
            }

            Patient p = new Patient(id, name, contact, mediHistory);
            dao.addPatient(p);

            JOptionPane.showMessageDialog(this, "Patient added successfully!");
            clearFields();
            refreshTable();
            setNextId();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updatePatient() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String mediHistory = mediHistoryArea.getText().trim();

            if (name.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill Name and Contact fields");
                return;
            }

            Patient p = new Patient(id, name, contact, mediHistory);
            dao.updatePatient(p);

            JOptionPane.showMessageDialog(this, "Patient updated successfully!");
            clearFields();
            refreshTable();
            setNextId();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void removePatient() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this patient?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dao.deletePatient(id);
                JOptionPane.showMessageDialog(this, "Patient removed successfully!");
                clearFields();
                refreshTable();
                setNextId();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) patientTable.getModel();
        model.setRowCount(0);

        try {
            List<Patient> patients = dao.getAllPatients();
            for (Patient p : patients) {
                model.addRow(new Object[]{
                        p.getId(),
                        p.getName(),
                        p.getContact(),
                        p.getMedi_history()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading patients: " + e.getMessage());
        }
    }

    private void loadSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(patientTable.getValueAt(selectedRow, 0).toString());
            nameField.setText(patientTable.getValueAt(selectedRow, 1).toString());
            contactField.setText(patientTable.getValueAt(selectedRow, 2).toString());
            mediHistoryArea.setText(patientTable.getValueAt(selectedRow, 3).toString());
        }
    }

    private void clearFields() {
        nameField.setText("");
        contactField.setText("");
        mediHistoryArea.setText("");
        patientTable.clearSelection();
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

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        inputPanel.add(idField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        inputPanel.add(nameField, gbc);

        // Contact
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        contactField = new JTextField(15);
        inputPanel.add(contactField, gbc);

        // History
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("History:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        mediHistoryArea = new JTextArea(3, 15);
        inputPanel.add(new JScrollPane(mediHistoryArea), gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        removeButton = new JButton("Remove");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        inputPanel.add(buttonPanel, gbc);

        mainPanel.add(inputPanel, BorderLayout.WEST);

        // Table
        patientTable = new JTable();
        mainPanel.add(new JScrollPane(patientTable), BorderLayout.CENTER);
    }

    {
        initUI();
    }

}
