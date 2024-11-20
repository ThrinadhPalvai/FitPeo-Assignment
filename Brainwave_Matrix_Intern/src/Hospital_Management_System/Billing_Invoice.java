package Hospital_Management_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Billing_Invoice extends JFrame implements ActionListener {

    private JTextField patientIdField, billingDateField, amountField, paymentMethodField;
    private JTextArea descriptionField;
    private JButton saveButton, clearButton,viewButton;

    public Billing_Invoice() {
        setTitle("Billing and Invoice");
        setSize(1600, 900);  // Adjust to fit content
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Only dispose this window
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(5, 5, 1585, 870);  // Fill almost the entire frame
        panel.setBackground(new Color(109, 164, 170));
        add(panel);

        // Create labels and text fields on the panel
        addLabelAndField(panel, "Patient ID:", 20, 20, patientIdField = new JTextField());
        addLabelAndField(panel, "Billing Date (YYYY-MM-DD):", 20, 70, billingDateField = new JTextField(LocalDate.now().toString())); // Set today's date
        addLabelAndField(panel, "Amount:", 20, 120, amountField = new JTextField());
        addLabelAndField(panel, "Payment Method:", 20, 170, paymentMethodField = new JTextField());

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(20, 220, 100, 30);
        panel.add(descriptionLabel);
        descriptionField = new JTextArea();
        descriptionField.setBounds(170, 220, 200, 100);
        panel.add(descriptionField);

        // Create buttons on the panel
        saveButton = new JButton("Save Invoice");
        saveButton.setBounds(20, 350, 120, 30);
        saveButton.addActionListener(this);
        panel.add(saveButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(160, 350, 120, 30);
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton);
        
        viewButton = new JButton("View Billing Invoices");
        viewButton.setBounds(300, 350, 150, 30);
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
        billingDateField.setText("");
        amountField.setText("");
        paymentMethodField.setText("");
        descriptionField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String patientId = patientIdField.getText();
            String billingDate = billingDateField.getText();
            String amount = amountField.getText();
            String paymentMethod = paymentMethodField.getText();
            String description = descriptionField.getText();

            try {
                // Validate date format
                if (!billingDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
                    return;
                }

                // Validate amount format
                if (!amount.matches("\\d+(\\.\\d{2})?")) {
                    JOptionPane.showMessageDialog(this, "Invalid amount format. Please enter a valid amount.");
                    return;
                }

                // Create a BillingInvoice object
                BillingInvoice invoice = new BillingInvoice(patientId, billingDate, amount, paymentMethod, description);
                // Save the invoice
                BillingInvoiceService invoiceService = new BillingInvoiceService();
                invoiceService.saveInvoice(invoice);

                JOptionPane.showMessageDialog(this, "Invoice saved successfully!");
                clearFields();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving invoice. Please try again.");
            }
        } else if (e.getSource() == viewButton) {
            viewBillingInvoices();
        }
    }
    
    private void viewBillingInvoices() {
        JFrame viewFrame = new JFrame("View Billing Invoices");
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
            String query = "SELECT * FROM BillingInvoices";
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("PatientID: ").append(rs.getString("patientId")).append("\n");
                sb.append("BillingDate: ").append(rs.getString("billingDate")).append("\n");
                sb.append("Amount: ").append(rs.getString("amount")).append("\n");
                sb.append("PaymentMethod: ").append(rs.getString("paymentMethod")).append("\n");
                sb.append("Description: ").append(rs.getString("description")).append("\n");
                sb.append("\n-------------------\n");
            }
            textArea.setText(sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            textArea.setText("Error retrieving BillingInvoices.");
        }

        viewFrame.setVisible(true);
    }


    private static class BillingInvoice {
        private String patientId;
        private String billingDate;
        private String amount;
        private String paymentMethod;
        private String description;

        public BillingInvoice(String patientId, String billingDate, String amount, String paymentMethod, String description) {
            this.patientId = patientId;
            this.billingDate = billingDate;
            this.amount = amount;
            this.paymentMethod = paymentMethod;
            this.description = description;
        }

        @Override
        public String toString() {
            return "BillingInvoice [Patient ID=" + patientId + ", Billing Date=" + billingDate + ", Amount=" + amount +
                   ", Payment Method=" + paymentMethod + ", Description=" + description + "]";
        }
    }

    private static class BillingInvoiceService {
        public void saveInvoice(BillingInvoice invoice) {
            Database database = new Database();
            // SQL query to insert invoice details into the BillingInvoices table
            String query = "INSERT INTO BillingInvoices (patientId, billingDate, amount, paymentMethod, description) VALUES ('" +
                           invoice.patientId + "', '" + invoice.billingDate + "', '" + invoice.amount + "', '" + invoice.paymentMethod + "', '" +
                           invoice.description + "')";
            // Execute the SQL query
            database.executeUpdate(query);
            System.out.println("Invoice saved: " + invoice);
        }
    }

    public static void main(String[] args) {
        new Billing_Invoice();
    }
}
