package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import storeAndLoad.*;

public class InputDialogTest extends JFrame {
	JFileChooser chooser = new JFileChooser();
	JButton button = new JButton ("FileChooser");

	
	public InputDialogTest(){
		Container cp = getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(button);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int state = chooser.showOpenDialog(InputDialogTest.this);
				File file = chooser.getSelectedFile();
				
				
				if(file!=null && state==JFileChooser.APPROVE_OPTION){
					// System.out.println(path);
					// System.out.println(rest);
					try{
						SALManager.sal(file);
						// LoadClassMultipleTimes.newInstance(path, rest2);
					}catch(Exception en){
						System.out.println("Exception Occured");
						en.printStackTrace();
					}

				}
			}
		});
		setBounds(300,400,100,100);
		setVisible(true);
	}
	
	public static void main(String[] args){
		new InputDialogTest();
	}
}
