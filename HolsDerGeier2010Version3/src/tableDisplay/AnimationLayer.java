package tableDisplay;

import java.awt.*;
import javax.swing.*;

public class AnimationLayer extends JLabel{
	
	/** Parameter fuer die Einstellung, wann die Gewinnkarte bei der
	 *  ScoreAnimation verschwindet.
	 */
	private static int SCORE_GAP = 30;
	
	/** Gibt an, ob die erste Phase der Punkteverteilung animiert werden soll*/
	private boolean animatingScore1;
	/** Gibt an, ob eine Punkteverteilung animiert werden soll (true) oder nicht (false).*/
	private boolean animatingScore2;
	/** Gibt an, ob ein Unentschieden animiert werden soll (true) oder nicht (false).*/
	private boolean animatingDraw;
	
	/** ImageIcon zur Animation der Punktevergabe*/
	private ImageIcon scoreImageIcon;
	private int xScoreCard, yScoreCard;
	
	
	/** ImageIcon zur Animation des Unentschiedens*/
	private ImageIcon drawImageIcon;
	
	private Font[] fonts;
	private int xPos;
	private int yPos;
	private String drawValue;
	private DisplayStackPanel stack;
	private DisplayPlayerPanel2[] pPanels;
	private DisplayPlayerPanel2 sample;
	private int samplePreferredWidth;
	private int samplePreferredHeight;
	private int i;
	
	public AnimationLayer(DisplayPlayerPanel2[] pPanels, Dimension d, DisplayStackPanel stack){
		this.pPanels=pPanels;
		
		
		
		
		this.stack=stack;
		sample = pPanels[0];
		System.out.println("AnimationLayer: Constructor starting");
		initializeFonts();
		this.sample=sample;
		samplePreferredWidth=sample.getPreferredSize().width;
		samplePreferredHeight=sample.getPreferredSize().height;
		setSize(d);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		drawImageIcon = new ImageIcon("src/graphics/drawIcon.gif");
	}
	
	private void initializeFonts(){
		fonts = new Font[30];
		for(int i=0;i<30;i++){
			fonts[i] = new Font("SansSerif", Font.BOLD, 15+(i*2));
		}
	}
	
	protected void startDrawAnimation(){
		animatingDraw=true;
		repaint();
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException ie){
			ie.printStackTrace();
		}
		animatingDraw=false;
		repaint();
	}
	
	protected void goToSleep(int millis){
		try{
			Thread.sleep(millis);
		}
		catch(InterruptedException ie){
			ie.printStackTrace();
		}
	}
	
	protected void startAnimation1(DisplayPlayerPanel2 panel){
		animatingScore1 = true;
		System.out.println("startAnimation1() invoked!");
		
		/* Die Startpunkte Holen, bei denen die Karte derzeit noch zu sehen ist*/
		xScoreCard = stack.getAnimationCoordinates().x;
		yScoreCard = stack.getAnimationCoordinates().y;
		
		scoreImageIcon = stack.getCurrentTopCard();
		
		/* Die Karte des Stacks übernehmen*/
		stack.clearCurrentTopCard();
		
		if(panel.equals(pPanels[0])){
			while(xScoreCard > pPanels[0].getLocation().x + (pPanels[0].getSize().width/3) &&
					       yScoreCard>pPanels[0].getLocation().y + (pPanels[0].getSize().height/3)){
				repaint();
				xScoreCard-=4;
				yScoreCard-=2;
				goToSleep(10);
			}
		}
		else if (panel.equals(pPanels[1])){
			while(xScoreCard > pPanels[1].getLocation().x + (pPanels[0].getSize().width/2)){
				repaint();
				xScoreCard-=5;
				goToSleep(15);
			}
		}
		else if (panel.equals(pPanels[2])){
			while(xScoreCard > pPanels[2].getLocation().x - (pPanels[0].getSize().width/2) &&
				       yScoreCard < pPanels[2].getLocation().y ){
				repaint();
				xScoreCard--;
				yScoreCard++;
				goToSleep(5);
			}
		}
		else if (panel.equals(pPanels[3])){
			while( yScoreCard > pPanels[3].getLocation().y + (pPanels[0].getSize().height/2)){
				repaint();
				yScoreCard--;
				goToSleep(5);
			
			}
		}
		else if (panel.equals(pPanels[4])){
			while(yScoreCard < pPanels[4].getLocation().y){
				repaint();
				yScoreCard++;
				goToSleep(5);
				
			
			}
		}
		else if (panel.equals(pPanels[5])){
			while(xScoreCard < pPanels[5].getLocation().x + (pPanels[0].getSize().width/2) &&
				       yScoreCard>pPanels[5].getLocation().y - (pPanels[0].getSize().height/2)){
				repaint();
				xScoreCard+=2;
				yScoreCard--;
				goToSleep(5);
			}
		}
		else if (panel.equals(pPanels[6])){
			while(xScoreCard < pPanels[6].getLocation().x + (pPanels[0].getSize().width/5)){
				repaint();
				xScoreCard+=3;
				goToSleep(10);
			}
		}
		else if (panel.equals(pPanels[7])){
			while(xScoreCard < pPanels[7].getLocation().x &&
				       yScoreCard<pPanels[7].getLocation().y ){
				repaint();
				xScoreCard++;
				yScoreCard++;
				goToSleep(5);
			}
		}
		animatingScore1=false;
	}
	
	protected void startAnimation(int drawValue, Point p){
		System.out.println("startAnimation invoked()");
		this.xPos=p.x;
		this.yPos=p.y;
		this.drawValue=String.valueOf(drawValue);
		animatingScore2 = true;
		if(drawValue>0){
			this.drawValue="+"+this.drawValue;
			setForeground(Color.GREEN);
		}
		else{
			setForeground(Color.RED);
		}
		
		for(i=0;i<29;i++){
			revalidate();
			repaint();
			try{
				Thread.sleep(30);
			}
			catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
		revalidate();
		repaint();
		animatingScore2=false;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(animatingScore2){
			System.out.println("Action");
			g.setFont(fonts[i]);
			FontMetrics fm = g.getFontMetrics();
			g.drawString(drawValue,
					xPos+(samplePreferredWidth-fm.stringWidth(drawValue))/2,
					yPos+samplePreferredHeight-i*3);
		}else if(animatingDraw){
			int drawImgIcnWidth = drawImageIcon.getIconWidth();
			System.out.println("D R A W I N G");
			drawImageIcon.paintIcon(this, g, getLocation().x+ (getWidth()-drawImgIcnWidth)/2, this.getLocation().y +250);
		}else if(animatingScore1){
			scoreImageIcon.paintIcon(this,g,xScoreCard,yScoreCard);
		}
	}
}
