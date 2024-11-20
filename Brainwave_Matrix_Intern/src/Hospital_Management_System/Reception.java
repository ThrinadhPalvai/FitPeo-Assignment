package Hospital_Management_System;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Reception extends JFrame {
	
	Reception(){
		
		JPanel panel=new JPanel();
		panel.setLayout(null);
		panel.setBounds(5,160,1525,670);
		panel.setBackground(new Color(109,164,170));
		add(panel);
		
		JPanel panel1=new JPanel();
		panel1.setLayout(null);
		panel1.setBounds(5,5,1525,150);
		panel1.setBackground(new Color(109,164,170));
		add(panel1);
		
		
		ImageIcon i1= new ImageIcon(ClassLoader.getSystemResource("Icons/doctor.png"));
		Image image=i1.getImage().getScaledInstance(250,250,Image.SCALE_DEFAULT);
		ImageIcon i2= new ImageIcon(image);
		JLabel label=new JLabel(i2);
		label.setBounds(1150,0,250,250);
		panel1.add(label);
		
		ImageIcon i11= new ImageIcon(ClassLoader.getSystemResource("Icons/ambulance.png"));
		Image image1=i11.getImage().getScaledInstance(300,100,Image.SCALE_DEFAULT);
		ImageIcon i22= new ImageIcon(image1);
		JLabel label1=new JLabel(i22);
		label1.setBounds(850,50,300,100);
		panel1.add(label1);
		
		
		JButton btn1=new JButton("Add New Patient");
		btn1.setBounds(30,15,200,30);
		btn1.setBackground(new Color(246,215,118));
		panel1.add(btn1);
		btn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new Add_New_Patient();
			}
			
		});
		
		
		
		JButton btn2=new JButton("Appointment Schedule");
		btn2.setBounds(30,58,200,30);
		btn2.setBackground(new Color(246,215,118));
		panel1.add(btn2);
		btn2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new Schedule_Appointment();
			}
			
		});
		
		
		JButton btn3=new JButton("Electronic Health Records");
		btn3.setBounds(30,100,200,30);
		btn3.setBackground(new Color(246,215,118));
		panel1.add(btn3);
		btn3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new Electronic_Health_Records();
			}
			
		});
		
		
		JButton btn4=new JButton("Billing & Invoice");
		btn4.setBounds(270,15,200,30);
		btn4.setBackground(new Color(246,215,118));
		panel1.add(btn4);
		btn4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new Billing_Invoice();
			}
			
		});
		
		
		JButton btn5=new JButton("Inventory Management");
		btn5.setBounds(270,58,200,30);
		btn5.setBackground(new Color(246,215,118));
		panel1.add(btn5);
		btn5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new Inventory_Management();
			}
			
		});
		
		
		JButton btn6=new JButton("Staff Management");
		btn6.setBounds(270,100,200,30);
		btn6.setBackground(new Color(246,215,118));
		panel1.add(btn6);
		btn6.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new Staff_Management();
			}
			
		});
		
		
		JButton btn7=new JButton("Supply Management");
		btn7.setBounds(510,15,200,30);
		btn7.setBackground(new Color(246,215,118));
		panel1.add(btn7);
		btn7.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new Supply_Management();
			}
			
		});
		
		
		JButton btn8=new JButton("Logout");
		btn8.setBounds(510,58,200,30);
		btn8.setBackground(new Color(246,215,118));
		panel1.add(btn8);
		btn8.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new Logout();
			}
			
		});
		
		
		
		setSize(1950,1090);
		getContentPane().setBackground(Color.white);
		setLayout(null);
		setVisible(true);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Reception();
	}

}
