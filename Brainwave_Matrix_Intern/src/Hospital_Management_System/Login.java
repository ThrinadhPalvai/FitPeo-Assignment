package Hospital_Management_System;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame implements ActionListener {
	
	JTextField textField;
	JPasswordField jPasswordField;
	JButton b1,b2;
	
	Login(){
		
		JLabel nameLabel= new JLabel("Username");
		nameLabel.setBounds(40,20,100,30);
		nameLabel.setFont(new Font("Tahoma",Font.BOLD,16));
		nameLabel.setForeground(Color.BLACK);
		add(nameLabel);
		
		JLabel password= new JLabel("Password");
		password.setBounds(40,70,100,30);
		password.setFont(new Font("Tahoma",Font.BOLD,16));
		password.setForeground(Color.BLACK);
		add(password);
		
		textField= new JTextField();
		textField.setBounds(125,20,150,30);
		textField.setFont(new Font("Tahoma",Font.PLAIN,15));
		textField.setBackground(new Color(255,179,0));
		add(textField);
		
		jPasswordField= new JPasswordField();
		jPasswordField.setBounds(125,70,150,30);
		jPasswordField.setFont(new Font("Tahoma",Font.PLAIN,15));
		jPasswordField.setBackground(new Color(255,179,0));
		add(jPasswordField);
		
		ImageIcon imageIcon= new ImageIcon(ClassLoader.getSystemResource("Icons/login.png"));
		Image i1=imageIcon.getImage().getScaledInstance(450,450,Image.SCALE_DEFAULT);
		ImageIcon imageIcon1= new ImageIcon(i1);
		JLabel label=new JLabel(imageIcon1);
		label.setBounds(350,-30,400,300);
		add(label);
		
		b1=new JButton("Login");
		b1.setBounds(40, 140, 120, 30);
		b1.setFont(new Font("serif",Font.BOLD,15));
		b1.setBackground(Color.black);
		b1.setForeground(Color.white);
		b1.addActionListener(this);
		add(b1);
		
		b2=new JButton("Cancel");
		b2.setBounds(180, 140, 120, 30);
		b2.setFont(new Font("serif",Font.BOLD,15));
		b2.setBackground(Color.black);
		b2.setForeground(Color.white);
		b2.addActionListener(this);
		add(b2);
		
		
		getContentPane().setBackground(new Color(109,164,170));
		setSize(750,300);
		setLocation(375,270);
		setLayout(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==b1) {
			try {
				Database d=new Database();
				String user=textField.getText();
				String pass=jPasswordField.getText();
				
				String q="Select * from login where ID='"+user+"'and PW='"+pass+"'";
				ResultSet resultSet=d.statement.executeQuery(q);
				
				if(resultSet.next()) {
					
					new Reception();
					setVisible(false);
				}else {
					JOptionPane.showMessageDialog(null,"Invalid");
				}
				
			}catch(Exception E){
				E.printStackTrace();
			}
			
		}else {
			System.exit(10);
			
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Login();

	}

	

}
