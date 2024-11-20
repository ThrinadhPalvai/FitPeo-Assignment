package Hospital_Management_System;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Add_New_Patient extends JFrame implements ActionListener {

    private JTextField idField, nameField, ageField, genderField, phoneField, addressField, medicalHistoryField;
    private JButton addButton, clearButton,viewButton;

    public Add_New_Patient() {
        setTitle("Add New Patient");
        setSize(1600, 900);  // Adjust to fit panels and image
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Only dispose this window
        setLocationRelativeTo(null);
        setLayout(null);

        
        JPanel panel = new JPanel(); panel.setLayout(null);
        panel.setBounds(5, 5, 1585, 870); // Fill almost the entire frame
        panel.setBackground(new Color(109, 164, 170));
        add(panel);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Icons/patient.png"));
        Image image = i1.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(image);
        JLabel label = new JLabel(i2);
        label.setBounds(1000, 0, 250, 250);
        panel.add(label);

        // Create labels and text fields on the main panel
        addLabelAndField(panel, "Patient ID:", 20, 20, idField = new JTextField());
        addLabelAndField(panel, "Patient Name:", 20, 70, nameField = new JTextField());
        addLabelAndField(panel, "Patient Age:", 20, 120, ageField = new JTextField());
        addLabelAndField(panel, "Patient Gender:", 20, 170, genderField = new JTextField());
        addLabelAndField(panel, "Patient Phone:", 20, 220, phoneField = new JTextField());
        addLabelAndField(panel, "Patient Address:", 20, 270, addressField = new JTextField());
        addLabelAndField(panel, "Medical History:", 20, 320, medicalHistoryField = new JTextField());

        // Create buttons on the main panel
        addButton = new JButton("Add Patient");
        addButton.setBounds(20, 370, 120, 30);
        addButton.addActionListener(this);
        panel.add(addButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(160, 370, 120, 30);
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton);
        
        viewButton = new JButton("View Patients");
        viewButton.setBounds(300, 370, 150, 30);
        viewButton.addActionListener(this);
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
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderField.setText("");
        phoneField.setText("");
        addressField.setText("");
        medicalHistoryField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String id = idField.getText();
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = genderField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String medicalHistory = medicalHistoryField.getText();

            try {
                // Validate inputs
                if (id.isEmpty() || name.isEmpty() || ageField.getText().isEmpty() || gender.isEmpty() || phone.isEmpty() || address.isEmpty() || medicalHistory.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields.");
                    return;
                }

                // Create a Patient object
                Patient patient = new Patient(id, name, age, gender, phone, address, medicalHistory);
                // Register the patient
                PatientService patientService = new PatientService();
                patientService.registerPatient(patient);

                JOptionPane.showMessageDialog(this, "Patient registered successfully!");
                clearFields();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid age format. Please enter a number.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error registering patient. Please try again.");
            }
        } else if (e.getSource() == viewButton) {
            viewPatients();
    }
    }
    
    private void viewPatients() {
        JFrame viewFrame = new JFrame("View Patients");
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
            String query = "SELECT * FROM Patients";
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Id: ").append(rs.getString("id")).append("\n");
                sb.append("Name: ").append(rs.getString("name")).append("\n");
                sb.append("Age: ").append(rs.getString("age")).append("\n");
                sb.append("Gender: ").append(rs.getString("gender")).append("\n");
                sb.append("Phone: ").append(rs.getString("phone")).append("\n");
                sb.append("Address: ").append(rs.getString("address")).append("\n");
                sb.append("MedicalHistory: ").append(rs.getString("medicalHistory")).append("\n");
                sb.append("\n-------------------\n");
            }
            textArea.setText(sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            textArea.setText("Error retrieving Patients.");
        }

        viewFrame.setVisible(true);
    }


    private static class Patient {
        private String id;
        private String name;
        private int age;
        private String gender;
        private String phone;
        private String address;
        private String medicalHistory;

        public Patient(String id, String name, int age, String gender, String phone, String address, String medicalHistory) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
            this.address = address;
            this.medicalHistory = medicalHistory;
        }

        @Override
        public String toString() {
            return "Patient [ID=" + id + ", Name=" + name + ", Age=" + age + ", Gender=" + gender +
                   ", Phone=" + phone + ", Address=" + address + ", Medical History=" + medicalHistory + "]";
        }
    }

    private static class PatientService {
        public void registerPatient(Patient patient) {
            Database database = new Database();
            // SQL query to insert patient details into the Patients table
            String query = "INSERT INTO Patients (id, name, age, gender, phone, address, medicalHistory) VALUES ('" +
                           patient.id + "', '" + patient.name + "', " + patient.age + ", '" + patient.gender + "', '" +
                           patient.phone + "', '" + patient.address + "', '" + patient.medicalHistory + "')";
            // Execute the SQL query
            database.executeUpdate(query);
            System.out.println("Patient registered: " + patient);
        }
    }

    public static void main(String[] args) {
        new Add_New_Patient();
    }
}

