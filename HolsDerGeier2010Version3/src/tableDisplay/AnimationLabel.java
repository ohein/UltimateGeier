package tableDisplay;

import java.awt.*;
import javax.swing.*;

public class AnimationLabel extends JLabel{
	
	private boolean animating;
	private int cnt;
	private String animationValue;
	private Font[] fonts;
	private Color animColor;
	
	public AnimationLabel(){
		setOpaque(false);
		initializeFonts();
		Dimension size = new Dimension(300,136);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(animating){
			Dimension d = getSize();
			setForeground(Color.GREEN);
			setFont(fonts[cnt]);
			FontMetrics fm = g.getFontMetrics();
			g.drawString(animationValue, ((d.width-fm.stringWidth(animationValue))/2), d.height-(d.height/8)-(cnt*4));
		}
	}
	
	public void startReceiveAnimation(int value){
		this.animationValue="+"+String.valueOf(value);
		animating=true;
		cnt=0;
		for(int i=0;i<19;i++){
			revalidate();
			repaint();
			cnt++;
			try{
				Thread.sleep(40);
			}
			catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
		animating=false;
		revalidate();
		repaint();
	}
	
	private void initializeFonts(){
		fonts = new Font[20];
		for(int i=0;i<20;i++){
			fonts[i] = new Font("SansSerif", Font.BOLD, 15+(i*5));
		}
		
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setSize(200,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AnimationLabel animLabel = new AnimationLabel();
		frame.add(animLabel);
		frame.setVisible(true);
		animLabel.startReceiveAnimation(5);
	}
	
	
}
