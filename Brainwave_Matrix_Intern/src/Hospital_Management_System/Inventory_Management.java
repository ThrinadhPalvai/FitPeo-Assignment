package Hospital_Management_System;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Inventory_Management extends JFrame implements ActionListener {

    private JTextField itemIdField, itemNameField, quantityField, priceField, supplierField, dateField;
    private JTextArea descriptionField;
    private JButton saveButton, clearButton,viewButton;

    public Inventory_Management() {
        setTitle("Inventory Management");
        setSize(1600, 900);  // Adjust to fit content
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Only dispose this window
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(5, 5, 1585, 870);  // Fill almost the entire frame
        panel.setBackground(new Color(109, 164, 170));
        add(panel);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Icons/Inv.png"));
        Image image = i1.getImage().getScaledInstance(400, 400, Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(image);
        JLabel label = new JLabel(i2);
        label.setBounds(850, 50, 400, 400);
        panel.add(label);

        // Create labels and text fields on the panel
        addLabelAndField(panel, "Item ID:", 20, 20, itemIdField = new JTextField());
        addLabelAndField(panel, "Item Name:", 20, 70, itemNameField = new JTextField());
        addLabelAndField(panel, "Quantity:", 20, 120, quantityField = new JTextField());
        addLabelAndField(panel, "Price:", 20, 170, priceField = new JTextField());
        addLabelAndField(panel, "Supplier:", 20, 220, supplierField = new JTextField());
        addLabelAndField(panel, "Date (YYYY-MM-DD):", 20, 270, dateField = new JTextField(LocalDate.now().toString()));

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(20, 320, 100, 30);
        panel.add(descriptionLabel);
        descriptionField = new JTextArea();
        descriptionField.setBounds(140, 320, 200, 100);
        panel.add(descriptionField);

        // Create buttons on the panel
        saveButton = new JButton("Save Item");
        saveButton.setBounds(20, 450, 120, 30);
        saveButton.addActionListener(this);
        panel.add(saveButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(160, 450, 120, 30);
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton);
        
        viewButton = new JButton("View Inventory");
        viewButton.setBounds(300, 450, 150, 30);
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
        itemIdField.setText("");
        itemNameField.setText("");
        quantityField.setText("");
        priceField.setText("");
        supplierField.setText("");
        dateField.setText("");
        descriptionField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String itemId = itemIdField.getText();
            String itemName = itemNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            String price = priceField.getText();
            String supplier = supplierField.getText();
            String date = dateField.getText();
            String description = descriptionField.getText();

            try {
                // Validate date format
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
                    return;
                }

                // Validate quantity format
                if (!quantityField.getText().matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity format. Please enter a valid number.");
                    return;
                }

                // Validate price format
                if (!price.matches("\\d+(\\.\\d{2})?")) {
                    JOptionPane.showMessageDialog(this, "Invalid price format. Please enter a valid amount.");
                    return;
                }

                // Create an InventoryItem object
                InventoryItem item = new InventoryItem(itemId, itemName, quantity, price, supplier, date, description);
                // Save the item
                InventoryService inventoryService = new InventoryService();
                inventoryService.saveItem(item);

                JOptionPane.showMessageDialog(this, "Item saved successfully!");
                clearFields();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving item. Please try again.");
            }
        } else if (e.getSource() == viewButton) {
            viewInventory();
    }
    }
    
    private void viewInventory() {
        JFrame viewFrame = new JFrame("View Inventory");
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
            String query = "SELECT * FROM Inventory";
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ItemId: ").append(rs.getString("itemId")).append("\n");
                sb.append("ItemName: ").append(rs.getString("itemName")).append("\n");
                sb.append("Quantity: ").append(rs.getString("quantity")).append("\n");
                sb.append("Price: ").append(rs.getString("price")).append("\n");
                sb.append("Supplier: ").append(rs.getString("supplier")).append("\n");
                sb.append("Date: ").append(rs.getString("date")).append("\n");
                sb.append("Description: ").append(rs.getString("description")).append("\n");
                sb.append("\n-------------------\n");
            }
            textArea.setText(sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            textArea.setText("Error retrieving Inventory.");
        }

        viewFrame.setVisible(true);
    }


    private static class InventoryItem {
        private String itemId;
        private String itemName;
        private int quantity;
        private String price;
        private String supplier;
        private String date;
        private String description;

        public InventoryItem(String itemId, String itemName, int quantity, String price, String supplier, String date, String description) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.price = price;
            this.supplier = supplier;
            this.date = date;
            this.description = description;
        }

        @Override
        public String toString() {
            return "InventoryItem [Item ID=" + itemId + ", Item Name=" + itemName + ", Quantity=" + quantity +
                   ", Price=" + price + ", Supplier=" + supplier + ", Date=" + date + ", Description=" + description + "]";
        }
    }

    private static class InventoryService {
        public void saveItem(InventoryItem item) {
            Database database = new Database();
            // SQL query to insert item details into the Inventory table
            String query = "INSERT INTO Inventory (itemId, itemName, quantity, price, supplier, date, description) VALUES ('" +
                           item.itemId + "', '" + item.itemName + "', '" + item.quantity + "', '" + item.price + "', '" + item.supplier +"', '" + item.date + "', '" +item.description +"')";
            
            database.executeUpdate(query);
            System.out.println("InventoryItem saved: " + item);
        }
    }

    public static void main(String[] args) {
        new Inventory_Management();
    }
}
