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

public class Electronic_Health_Records extends JFrame implements ActionListener {

    private JTextField patientIdField, diagnosisField, treatmentField, doctorField, dateField;
    private JTextArea notesField;
    private JButton saveButton, clearButton,viewButton;

    public Electronic_Health_Records() {
        setTitle("Electronic Health Records");
        setSize(1600, 900);  // Adjust to fit content
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Only dispose this window
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(5, 5, 1585, 870);  // Fill almost the entire frame
        panel.setBackground(new Color(109, 164, 170));
        add(panel);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Icons/ehr.png"));
        Image image = i1.getImage().getScaledInstance(612, 428, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(image);
        JLabel label = new JLabel(i2);
        label.setBounds(700,50, 612, 428);
        panel.add(label);

        // Create labels and text fields on the panel
        addLabelAndField(panel, "Patient ID:", 20, 20, patientIdField = new JTextField());
        addLabelAndField(panel, "Diagnosis:", 20, 70, diagnosisField = new JTextField());
        addLabelAndField(panel, "Treatment:", 20, 120, treatmentField = new JTextField());
        addLabelAndField(panel, "Doctor:", 20, 170, doctorField = new JTextField());
        addLabelAndField(panel, "Date (YYYY-MM-DD):", 20, 220, dateField = new JTextField(LocalDate.now().toString()));

        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setBounds(20, 270, 100, 30);
        panel.add(notesLabel);
        notesField = new JTextArea();
        notesField.setBounds(140, 270, 200, 100);
        panel.add(notesField);

        // Create buttons on the panel
        saveButton = new JButton("Save Record");
        saveButton.setBounds(20, 400, 120, 30);
        saveButton.addActionListener(this);
        panel.add(saveButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(160, 400, 120, 30);
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton);
        
        viewButton = new JButton("View EHR");
        viewButton.setBounds(300, 400, 150, 30);
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
        patientIdField.setText("");
        diagnosisField.setText("");
        treatmentField.setText("");
        doctorField.setText("");
        dateField.setText("");
        notesField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String patientId = patientIdField.getText();
            String diagnosis = diagnosisField.getText();
            String treatment = treatmentField.getText();
            String doctor = doctorField.getText();
            String date = dateField.getText();
            String notes = notesField.getText();

            try {
                // Validate date format
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
                    return;
                }

                // Create an EHR object
                EHR ehr = new EHR(patientId, diagnosis, treatment, doctor, date, notes);
                // Save the EHR
                EHRService ehrService = new EHRService();
                ehrService.saveEHR(ehr);

                JOptionPane.showMessageDialog(this, "Health record saved successfully!");
                clearFields();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving health record. Please try again.");
            }
        }  else if (e.getSource() == viewButton) {
            viewEHR();
    }
    }
        
        private void viewEHR() {
            JFrame viewFrame = new JFrame("View EHR");
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
                String query = "SELECT * FROM EHR";
                ResultSet rs = stmt.executeQuery(query);

                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("Patient ID: ").append(rs.getString("patientId")).append("\n");
                    sb.append("Diagnosis: ").append(rs.getString("diagnosis")).append("\n");
                    sb.append("Treatment: ").append(rs.getString("treatment")).append("\n");
                    sb.append("Doctor: ").append(rs.getString("doctor")).append("\n");
                    sb.append("Date: ").append(rs.getString("date")).append("\n");
                    sb.append("Notes: ").append(rs.getString("notes")).append("\n");
                    sb.append("\n-------------------\n");
                }
                textArea.setText(sb.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
                textArea.setText("Error retrieving Inventory.");
            }

            viewFrame.setVisible(true);
        }

    private static class EHR {
        private String patientId;
        private String diagnosis;
        private String treatment;
        private String doctor;
        private String date;
        private String notes;

        public EHR(String patientId, String diagnosis, String treatment, String doctor, String date, String notes) {
            this.patientId = patientId;
            this.diagnosis = diagnosis;
            this.treatment = treatment;
            this.doctor = doctor;
            this.date = date;
            this.notes = notes;
        }

        @Override
        public String toString() {
            return "EHR [Patient ID=" + patientId + ", Diagnosis=" + diagnosis + ", Treatment=" + treatment +
                   ", Doctor=" + doctor + ", Date=" + date + ", Notes=" + notes + "]";
        }
    }

    private static class EHRService {
        public void saveEHR(EHR ehr) {
            Database database = new Database();
            // SQL query to insert EHR details into the EHR table
            String query = "INSERT INTO EHR (patientId, diagnosis, treatment, doctor, date, notes) VALUES ('" +
                           ehr.patientId + "', '" + ehr.diagnosis + "', '" + ehr.treatment + "', '" + ehr.doctor + "', '" +
                           ehr.date + "', '" + ehr.notes + "')";
            // Execute the SQL query
            database.executeUpdate(query);
            System.out.println("EHR saved: " + ehr);
        }
    }

    public static void main(String[] args) {
        new Electronic_Health_Records();
    }
}
