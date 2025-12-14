package view;

import dao.DoctorDAO;
import model.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorView extends JFrame {
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField specialityField;
    private JTextField availableTimeField;
    private JButton addButton;
    private JButton updateButton;
    private JButton removeButton;
    private JTable docotrTable;

    private DoctorDAO dao = new DoctorDAO();

    public DoctorView() {
        setTitle("Doctor Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);

        // Initialize table
        initializeTable();

        // Set ID field non-editable
        idField.setEditable(false);

        // Action Listeners
        addButton.addActionListener(e -> addDoctor());
        updateButton.addActionListener(e -> updateDoctor());
        removeButton.addActionListener(e -> removeDoctor());

        docotrTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedDoctor();
            }
        });

        refreshTable();
        setNextId();
    }

    private void initializeTable() {
        String[] columns = {"ID", "Name", "Speciality", "Available Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        docotrTable.setModel(model);
    }

    private void setNextId() {
        try {
            int nextId = dao.getNextId();
            idField.setText(String.valueOf(nextId));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching next ID: " + e.getMessage());
        }
    }

    private void addDoctor() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String speciality = specialityField.getText().trim();
            String availableTime = availableTimeField.getText().trim();

            if (name.isEmpty() || speciality.isEmpty() || availableTime.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            Doctor d = new Doctor(id, name, speciality, availableTime);
            dao.addDoctor(d);

            JOptionPane.showMessageDialog(this, "Doctor added successfully!");
            clearFields();
            refreshTable();
            setNextId();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateDoctor() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String speciality = specialityField.getText().trim();
            String availableTime = availableTimeField.getText().trim();

            if (name.isEmpty() || speciality.isEmpty() || availableTime.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            Doctor d = new Doctor(id, name, speciality, availableTime);
            dao.updateDoctor(d);

            JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
            clearFields();
            refreshTable();
            setNextId();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void removeDoctor() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this doctor?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dao.deleteDoctor(id);
                JOptionPane.showMessageDialog(this, "Doctor removed successfully!");
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
        DefaultTableModel model = (DefaultTableModel) docotrTable.getModel();
        model.setRowCount(0);

        try {
            List<Doctor> doctors = dao.getAllDoctors();
            for (Doctor d : doctors) {
                model.addRow(new Object[]{
                        d.getId(),
                        d.getName(),
                        d.getSpeciality(),
                        d.getAvailable_time()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + e.getMessage());
        }
    }

    private void loadSelectedDoctor() {
        int selectedRow = docotrTable.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(docotrTable.getValueAt(selectedRow, 0).toString());
            nameField.setText(docotrTable.getValueAt(selectedRow, 1).toString());
            specialityField.setText(docotrTable.getValueAt(selectedRow, 2).toString());
            availableTimeField.setText(docotrTable.getValueAt(selectedRow, 3).toString());
        }
    }

    private void clearFields() {
        nameField.setText("");
        specialityField.setText("");
        availableTimeField.setText("");
        docotrTable.clearSelection();
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

        // Speciality
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Speciality:"), gbc);
        gbc.gridx = 1;
        specialityField = new JTextField(15);
        inputPanel.add(specialityField, gbc);

        // Available Time
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Available Time:"), gbc);
        gbc.gridx = 1;
        availableTimeField = new JTextField(15);
        inputPanel.add(availableTimeField, gbc);

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
        docotrTable = new JTable();
        mainPanel.add(new JScrollPane(docotrTable), BorderLayout.CENTER);
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
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        idField = new JTextField();
        panel1.add(idField, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addButton = new JButton();
        addButton.setText("Add");
        panel1.add(addButton, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateButton = new JButton();
        updateButton.setText("Update");
        panel1.add(updateButton, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeButton = new JButton();
        removeButton.setText("Remove");
        panel1.add(removeButton, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        docotrTable = new JTable();
        panel1.add(docotrTable, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        nameField = new JTextField();
        panel1.add(nameField, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        specialityField = new JTextField();
        panel1.add(specialityField, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        availableTimeField = new JTextField();
        panel1.add(availableTimeField, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }
}
