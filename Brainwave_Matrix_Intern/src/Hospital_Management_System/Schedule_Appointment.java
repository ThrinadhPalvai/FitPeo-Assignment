package Hospital_Management_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Schedule_Appointment extends JFrame implements ActionListener {

    private JTextField patientIdField, doctorField, dateField, timeField, reasonField;
    private JButton scheduleButton, clearButton,viewButton;

    public Schedule_Appointment() {
        setTitle("Schedule Appointment");
        setSize(1900, 900);  // Adjust to fit content
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Only dispose this window
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(5, 5, 1585, 870);  // Fill almost the entire frame
        panel.setBackground(new Color(109, 164, 170));
        add(panel);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Icons/Appointment.jpg"));
        Image image = i1.getImage().getScaledInstance(941, 431, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(image);
        JLabel label = new JLabel(i2);
        label.setBounds(500, 50, 941, 431);
        panel.add(label);

        // Create labels and text fields on the panel
        addLabelAndField(panel, "Patient ID:", 20, 20, patientIdField = new JTextField());
        addLabelAndField(panel, "Doctor Name:", 20, 70, doctorField = new JTextField());
        addLabelAndField(panel, "Date (YYYY-MM-DD):", 20, 120, dateField = new JTextField(LocalDate.now().toString()));
        addLabelAndField(panel, "Time (HH:MM):", 20, 170, timeField = new JTextField());
        addLabelAndField(panel, "Reason:", 20, 220, reasonField = new JTextField());

        // Create buttons on the panel
        scheduleButton = new JButton("Schedule Appointment");
        scheduleButton.setBounds(20, 270, 170, 30);
        scheduleButton.addActionListener(this);
        panel.add(scheduleButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(200, 270, 120, 30);
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton);
        
        
        viewButton = new JButton("View Appointments");
        viewButton.setBounds(330, 270, 150, 30);
        viewButton.addActionListener(this);
        panel.add(viewButton);

        setVisible(true);
    }

    private void addLabelAndField(JPanel panel, String labelText, int x, int y, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, 120, 30);
        panel.add(label);
        textField.setBounds(x + 150, y, 200, 30);
        panel.add(textField);
    }

    private void clearFields() {
        patientIdField.setText("");
        doctorField.setText("");
        dateField.setText("");
        timeField.setText("");
        reasonField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == scheduleButton) {
            String patientId = patientIdField.getText();
            String doctor = doctorField.getText();
            String date = dateField.getText();
            String time = timeField.getText();
            String reason = reasonField.getText();

            try {
                // Validate date format
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
                    return;
                }

                // Validate time format
                if (!time.matches("\\d{2}:\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Invalid time format. Please use HH:MM.");
                    return;
                }

                // Create an Appointment object
                Appointment appointment = new Appointment(patientId, doctor, date, time, reason);
                // Schedule the appointment
                AppointmentService appointmentService = new AppointmentService();
                appointmentService.scheduleAppointment(appointment);

                JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
                clearFields();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error scheduling appointment. Please try again.");
            }
        } else if (e.getSource() == viewButton) {
            viewAppointments();
        }
    }
    
    private void viewAppointments() {
        JFrame viewFrame = new JFrame("View Appointments");
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
            String query = "SELECT * FROM Appointments";
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("PatientID: ").append(rs.getString("patientId")).append("\n");
                sb.append("Doctor: ").append(rs.getString("doctor")).append("\n");
                sb.append("date: ").append(rs.getString("date")).append("\n");
                sb.append("Time: ").append(rs.getString("time")).append("\n");
                sb.append("Date: ").append(rs.getString("date")).append("\n");
                sb.append("Reason: ").append(rs.getString("reason")).append("\n");
                sb.append("\n-------------------\n");
            }
            textArea.setText(sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            textArea.setText("Error retrieving supplies.");
        }

        viewFrame.setVisible(true);
    }


    private static class Appointment {
        private String patientId;
        private String doctor;
        private String date;
        private String time;
        private String reason;

        public Appointment(String patientId, String doctor, String date, String time, String reason) {
            this.patientId = patientId;
            this.doctor = doctor;
            this.date = date;
            this.time = time;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return "Appointment [Patient ID=" + patientId + ", Doctor=" + doctor + 
                   ", Date=" + date + ", Time=" + time + ", Reason=" + reason + "]";
        }
    }

    private static class AppointmentService {
        public void scheduleAppointment(Appointment appointment) {
            Database database = new Database();
            // SQL query to insert appointment details into the Appointments table
            String query = "INSERT INTO Appointments (patientId, doctor, date, time, reason) VALUES ('" +
                           appointment.patientId + "', '" + appointment.doctor + "', '" +
                           appointment.date + "', '" + appointment.time + ":00', '" + appointment.reason + "')";
            // Execute the SQL query
            database.executeUpdate(query);
            System.out.println("Appointment scheduled: " + appointment);
        }
    }

    public static void main(String[] args) {
        new Schedule_Appointment();
    }
}



