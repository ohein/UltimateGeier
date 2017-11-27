package controller;

import javax.swing.*;
import java.awt.*;

public class SimpleGUI extends JFrame {
	private PlayerStatPanel panel;
	
	public SimpleGUI(){
		Container cp = getContentPane();
		panel = new PlayerStatPanel("Benny",3);
		cp.add(panel);
		setSize(300,400);
		setVisible(true);
	}
	
	public static void main(String[] args){
		System.out.println(System.getProperty("file.separator"));
		SimpleGUI gui =new SimpleGUI();
		for(int i=0;i<3;i++){
			try{
				Thread.sleep(1000);
				gui.panel.showNextBuzzard();
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
	}
}
