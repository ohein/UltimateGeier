package enrollGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class SetPathButton extends JButton implements ActionListener {
	private JFileChooser chooser;
	private GameEnrollMediator med;
	private String path;
	private Component owner;
	
	public SetPathButton(GameEnrollMediator med, Component owner){
		super("SetPath");
		this.med=med;
		//this.owner=owner;
		this.med.registerSetPathButton(this);
		chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		int state = chooser.showOpenDialog(SetPathButton.this.owner);
		File file = chooser.getSelectedFile();
		if(file!=null && state==JFileChooser.APPROVE_OPTION){
			path=file.getAbsolutePath();
			med.setPath();
		}
	}
	
	String getPath(){
		return path;
	}

}
