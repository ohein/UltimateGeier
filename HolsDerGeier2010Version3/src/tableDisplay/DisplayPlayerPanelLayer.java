package tableDisplay;

import javax.swing.*;
import player.*;

public class DisplayPlayerPanelLayer extends JLayeredPane{
	private int number;
	private HolsDerGeierSpieler player;
	private DisplayPlayerPanel displayPanel;
	private AnimationLabel animationLabel;
	
	public DisplayPlayerPanelLayer(int number, HolsDerGeierSpieler player){
		super();
		setLayout(null);
		this.number=number;
		this.player=player;
		displayPanel = new DisplayPlayerPanel(number,player);
		displayPanel.setBounds(0,0,300,136);
		
		this.setLayer(displayPanel,JLayeredPane.PALETTE_LAYER);
		this.add(displayPanel);
		
		animationLabel = new AnimationLabel();
		animationLabel.setBounds(0,0,300,136);
		this.setLayer(animationLabel, JLayeredPane.DRAG_LAYER);
		this.add(animationLabel);
		setVisible(true);
	}
	
	public void startReceiveAnimation(int value){
		animationLabel.startReceiveAnimation(value);
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
	
		frame.setSize(300,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DisplayPlayerPanelLayer pane = new DisplayPlayerPanelLayer(0,new IntelligentBuzzardPlayer("Detlef Eigen"));
		frame.getContentPane().add(pane);
		frame.setVisible(true);
	    pane.startReceiveAnimation(5);
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException ie){
			ie.printStackTrace();
		}
	    pane.startReceiveAnimation(7);

	}
}
