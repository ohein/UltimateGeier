package tests;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import storeAndLoad.MultiLoader;

public class DirectoryTest extends JFrame {
	private JPanel selectionPanel, listPanel, buttonPanel,namePanel;
	private JButton okButton, cancelButton;
	private JList list, enrPlayers;
	private JTextField nameField;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	
	
	public DirectoryTest(){
		Container cp = getContentPane();
		gbl = new GridBagLayout();
		selectionPanel = new JPanel();
		selectionPanel.setLayout(gbl);
		listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(1,1));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		
		gbc = new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridheight=3;
		gbc.gridwidth=5;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.weightx=100;
		gbc.weighty=70;
		gbl.setConstraints(listPanel, gbc);
		selectionPanel.add(listPanel);
		
		gbc = new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=4;
		gbc.gridheight=2;
		gbc.gridwidth=5;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.weightx=100;
		gbc.weighty=20;
		gbl.setConstraints(buttonPanel, gbc);
		selectionPanel.add(buttonPanel);
		
		namePanel = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=7;
		gbc.gridheight=1;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.gridwidth=5;
		gbc.weightx=100;
		gbc.weighty=10;
		gbl.setConstraints(namePanel,gbc);
		selectionPanel.add(namePanel);
		
		
		
		File file = new File("src/cls");
		list = new JList(file.list());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPanel.add(new JScrollPane(list));
		okButton = new JButton("Load");
		cancelButton = new JButton("Cancel");
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		nameField = new JTextField("Name der Strategie-Instanz");
		namePanel.setLayout(new GridLayout());
		namePanel.add(nameField);
		
		
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				MultiLoader.newInstance("src/cls",list.getSelectedValue().toString(),nameField.getText());
			}
		});
		
		cp.add(selectionPanel, BorderLayout.CENTER);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				e.getWindow().setVisible(false);
				e.getWindow().dispose();
				System.exit(0);
			}
		});
		pack();
		setLocation(100,100);
		setVisible(true);
		
	}
	
	public static void testDirectories(){
		File file = new File("src/cls");
		String[] list = file.list();
		for(int i=0;i<list.length;i++){
			System.out.println(list[i]);
		}
	}
	
	public static void main(String[] args){
		new DirectoryTest();
	}
}
