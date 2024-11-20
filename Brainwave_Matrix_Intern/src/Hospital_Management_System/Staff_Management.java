package Hospital_Management_System;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Staff_Management extends JFrame implements ActionListener {

    private JTextField staffIdField, staffNameField, designationField, departmentField, joiningDateField;
    private JTextArea addressField;
    private JButton saveButton, clearButton, viewButton;

    public Staff_Management() {
        setTitle("Staff Management");
        setSize(1600, 900);  // Adjust to fit content
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Only dispose this window
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(5, 5, 1585, 870);  // Fill almost the entire frame
        panel.setBackground(new Color(109, 164, 170));
        add(panel);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Icons/staff.png"));
        Image image = i1.getImage().getScaledInstance(681, 382, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(image);
        JLabel label = new JLabel(i2);
        label.setBounds(600, 50, 681, 382);
        panel.add(label);

        // Create labels and text fields on the panel
        addLabelAndField(panel, "Staff ID:", 20, 20, staffIdField = new JTextField());
        addLabelAndField(panel, "Staff Name:", 20, 70, staffNameField = new JTextField());
        addLabelAndField(panel, "Designation:", 20, 120, designationField = new JTextField());
        addLabelAndField(panel, "Department:", 20, 170, departmentField = new JTextField());
        addLabelAndField(panel, "Joining Date (YYYY-MM-DD):", 20, 220, joiningDateField =new JTextField(LocalDate.now().toString()));

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(20, 270, 100, 30);
        panel.add(addressLabel);
        addressField = new JTextArea();
        addressField.setBounds(140, 270, 200, 100);
        panel.add(addressField);

        // Create buttons on the panel
        saveButton = new JButton("Save Staff");
        saveButton.setBounds(20, 400, 120, 30);
        saveButton.addActionListener(this);
        panel.add(saveButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(160, 400, 120, 30);
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton);
        
        viewButton = new JButton("View Staff");
        viewButton.setBounds(300, 400, 120, 30);
        viewButton.addActionListener(e -> viewStaffDetails());
        panel.add(viewButton);

        setVisible(true);
    }

    private void addLabelAndField(JPanel panel, String labelText, int x, int y, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, 100, 30);
        panel.add(label);
        textField.setBounds(x + 120, y, 200, 30);
        panel.add(textField);
    }

    private void clearFields() {
        staffIdField.setText("");
        staffNameField.setText("");
        designationField.setText("");
        departmentField.setText("");
        joiningDateField.setText("");
        addressField.setText("");
    }

    private void viewStaffDetails() {
    	JFrame viewFrame = new JFrame("View Staff");
    	viewFrame.setSize(800, 600);
    	viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	viewFrame.setLocationRelativeTo(null);
    	viewFrame.setLayout(new BorderLayout());
    	JTextArea textArea = new JTextArea();
    	textArea.setEditable(false);
    	JScrollPane scrollPane = new JScrollPane(textArea);
    	viewFrame.add(scrollPane, BorderLayout.CENTER);
    	try {
    		Database database = new Database();
    		Connection conn = database.connection;
    		Statement stmt = conn.createStatement();
    		String query = "SELECT * FROM Staffs";
    		ResultSet rs = stmt.executeQuery(query);
    		StringBuilder sb = new StringBuilder();
    		while (rs.next()) {
    			sb.append("Staff ID: ").append(rs.getString("staffId")).append("\n");
    			sb.append("Staff Name: ").append(rs.getString("staffName")).append("\n");
    			sb.append("Designation: ").append(rs.getString("designation")).append("\n");
    			sb.append("Department: ").append(rs.getString("department")).append("\n");
    			sb.append("Joining Date: ").append(rs.getString("joiningDate")).append("\n");
    			sb.append("Address: ").append(rs.getString("address")).append("\n");
    			sb.append("\n-------------------\n"); } textArea.setText(sb.toString());
    			}
    	catch (Exception ex) { ex.printStackTrace();
    	textArea.setText("Error retrieving staff details.");
    	}
    	viewFrame.setVisible(true);
    	}
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String staffId = staffIdField.getText();
            String staffName = staffNameField.getText();
            String designation = designationField.getText();
            String department = departmentField.getText();
            String joiningDate = joiningDateField.getText();
            String address = addressField.getText();

            try {
                // Validate date format
                if (!joiningDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
                    return;
                }

                // Create a Staff object
                Staff staff = new Staff(staffId, staffName, designation, department, joiningDate, address);
                // Save the staff details
                StaffService staffService = new StaffService();
                staffService.saveStaff(staff);

                JOptionPane.showMessageDialog(this, "Staff saved successfully!");
                clearFields();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving staff. Please try again.");
            }
        }
    }

    private static class Staff {
        private String staffId;
        private String staffName;
        private String designation;
        private String department;
        private String joiningDate;
        private String address;

        public Staff(String staffId, String staffName, String designation, String department, String joiningDate, String address) {
            this.staffId = staffId;
            this.staffName = staffName;
            this.designation = designation;
            this.department = department;
            this.joiningDate = joiningDate;
            this.address = address;
        }

        @Override
        public String toString() {
            return "Staff [Staff ID=" + staffId + ", Staff Name=" + staffName + ", Designation=" + designation +
                   ", Department=" + department + ", Joining Date=" + joiningDate + ", Address=" + address + "]";
        }
    }

    private static class StaffService {
        public void saveStaff(Staff staff) {
            Database database = new Database();
            // SQL query to insert staff details into the Staffs table
            String query = "INSERT INTO Staffs (staffId, staffName, designation, department, joiningDate, address) VALUES ('" +
                           staff.staffId + "', '" + staff.staffName + "', '" + staff.designation + "', '" + staff.department + "', '" +
                           staff.joiningDate + "', '" + staff.address + "')";
            // Execute the SQL query
            database.executeUpdate(query);
            System.out.println("Staff saved: " + staff);
        }
    }

    public static void main(String[] args) {
        new Staff_Management();
    }
}
