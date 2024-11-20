package Hospital_Management_System;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Logout extends JFrame implements ActionListener {
    
    JButton logoutButton, cancelButton;
    
    public Logout() {
        
        JLabel label = new JLabel("Are you sure you want to logout?");
        label.setBounds(40, 20, 300, 30);
        label.setFont(new Font("Tahoma", Font.BOLD, 16));
        label.setForeground(Color.BLACK);
        add(label);
       
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(40, 140, 120, 30);
        logoutButton.setFont(new Font("serif", Font.BOLD, 15));
        logoutButton.setBackground(Color.black);
        logoutButton.setForeground(Color.white);
        logoutButton.addActionListener(this);
        add(logoutButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(180, 140, 120, 30);
        cancelButton.setFont(new Font("serif", Font.BOLD, 15));
        cancelButton.setBackground(Color.black);
        cancelButton.setForeground(Color.white);
        cancelButton.addActionListener(this);
        add(cancelButton);
        
        getContentPane().setBackground(new Color(109, 164, 170));
        setSize(750, 300);
        setLocation(375, 270);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            System.exit(0);
            setVisible(false);
        } else if (e.getSource() == cancelButton) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new Logout();
    }
}
