package tests;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;

public class FileLoad extends JFrame{
	JFileChooser chooser;
	JButton button;
	
	public FileLoad(){
		chooser = new JFileChooser();
		button = new JButton("FileChooser");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		Container cp = getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(button);
		
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int state = chooser.showOpenDialog(FileLoad.this);
				File file = chooser.getSelectedFile();
				
				if(file !=null && state == JFileChooser.APPROVE_OPTION){
					vultureUtil.PropertyManager.storeKeyVal("playerPath", file.getAbsolutePath());
				}
			}
		});
		setBounds(300,400,100,100);
		setVisible(true);
	}
	
	public static void main(String[] args){
		new FileLoad();
	}
}
