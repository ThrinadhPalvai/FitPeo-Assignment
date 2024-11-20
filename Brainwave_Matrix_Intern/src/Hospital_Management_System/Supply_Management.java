package Hospital_Management_System;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Supply_Management extends JFrame implements ActionListener {

    private JTextField supplyIdField, supplyNameField, supplierField, quantityField, dateField;
    private JTextArea notesField;
    private JButton saveButton, clearButton, viewButton;

    public Supply_Management() {
        setTitle("Supply Management");
        setSize(1600, 900);  // Adjust to fit content
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Only dispose this window
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(5, 5, 1585, 870);  // Fill almost the entire frame
        panel.setBackground(new Color(109, 164, 170));
        add(panel);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Icons/supply.png"));
        Image image = i1.getImage().getScaledInstance(824,596, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(image);
        JLabel label = new JLabel(i2);
        label.setBounds(500, 50, 824, 596);
        panel.add(label);

        // Create labels and text fields on the panel
        addLabelAndField(panel, "Supply ID:", 20, 20, supplyIdField = new JTextField());
        addLabelAndField(panel, "Supply Name:", 20, 70, supplyNameField = new JTextField());
        addLabelAndField(panel, "Supplier:", 20, 120, supplierField = new JTextField());
        addLabelAndField(panel, "Quantity:", 20, 170, quantityField = new JTextField());
        addLabelAndField(panel, "Date (YYYY-MM-DD):", 20, 220, dateField = new JTextField(LocalDate.now().toString()));

        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setBounds(20, 270, 100, 30);
        panel.add(notesLabel);
        notesField = new JTextArea();
        notesField.setBounds(140, 270, 200, 100);
        panel.add(notesField);

        // Create buttons on the panel
        saveButton = new JButton("Save Supply");
        saveButton.setBounds(20, 400, 120, 30);
        saveButton.addActionListener(this);
        panel.add(saveButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(160, 400, 120, 30);
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton);

        viewButton = new JButton("View Supplies");
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
        supplyIdField.setText("");
        supplyNameField.setText("");
        supplierField.setText("");
        quantityField.setText("");
        dateField.setText("");
        notesField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String supplyId = supplyIdField.getText();
            String supplyName = supplyNameField.getText();
            String supplier = supplierField.getText();
            String quantity = quantityField.getText();
            String date = dateField.getText();
            String notes = notesField.getText();

            try {
                // Validate date format
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
                    return;
                }

                // Create a Supply object
                Supply supply = new Supply(supplyId, supplyName, supplier, quantity, date, notes);
                // Save the supply
                SupplyService supplyService = new SupplyService();
                supplyService.saveSupply(supply);

                JOptionPane.showMessageDialog(this, "Supply saved successfully!");
                clearFields();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving supply. Please try again.");
            }
        } else if (e.getSource() == viewButton) {
            viewSupplies();
        }
    }

    private void viewSupplies() {
        JFrame viewFrame = new JFrame("View Supplies");
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
            String query = "SELECT * FROM Supplies";
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Supply ID: ").append(rs.getString("supplyId")).append("\n");
                sb.append("Supply Name: ").append(rs.getString("supplyName")).append("\n");
                sb.append("Supplier: ").append(rs.getString("supplier")).append("\n");
                sb.append("Quantity: ").append(rs.getString("quantity")).append("\n");
                sb.append("Date: ").append(rs.getString("date")).append("\n");
                sb.append("Notes: ").append(rs.getString("notes")).append("\n");
                sb.append("\n-------------------\n");
            }
            textArea.setText(sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            textArea.setText("Error retrieving supplies.");
        }

        viewFrame.setVisible(true);
    }

    private static class Supply {
        private String supplyId;
        private String supplyName;
        private String supplier;
        private String quantity;
        private String date;
        private String notes;

        public Supply(String supplyId, String supplyName, String supplier, String quantity, String date, String notes) {
            this.supplyId = supplyId;
            this.supplyName = supplyName;
            this.supplier = supplier;
            this.quantity = quantity;
            this.date = date;
            this.notes = notes;
        }

        @Override
        public String toString() {
            return "Supply [Supply ID=" + supplyId + ", Supply Name=" + supplyName + ", Supplier=" + supplier +
                   ", Quantity=" + quantity + ", Date=" + date + ", Notes=" + notes + "]";
        }
    }

    private static class SupplyService {
        public void saveSupply(Supply supply) {
            Database database = new Database();
            // SQL query to insert supply details into the Supplies table
            String query = "INSERT INTO Supplies (supplyId, supplyName, supplier, quantity, date, notes) VALUES ('" +
            		supply.supplyId + "', '" + supply.supplyName + "', '" + supply.supplier + "', '" + supply.quantity + "', '" +
            		supply.date + "', '" + supply.notes + "')";
     // Execute the SQL query
     database.executeUpdate(query);
     System.out.println("supplies saved: " + supply);
 }
}

public static void main(String[] args) {
 new Supply_Management();
}
}
