package tableDisplay;

import java.awt.*;
import javax.swing.*;

public class DisplayStackPanel extends JPanel{
	/** Abstand zwischen den einzelnen aufeinanderliegenden Karten*/
	private static int CARD_GAP = 3;
	
	/** Abstand des Stapels vom oberen Rand*/
	private static int TOP_GAP=20;
	
	private int stackSize;
	private int currentStackSize;
	private int currentCardValue;
	private int picWidth = 110;
	private int picHeight = 150;
	private ImageIcon backOfCard;
	
	private ImageIcon currentTopCard;
	
	public DisplayStackPanel(int stackSize){
		this.stackSize=stackSize;
		backOfCard= new ImageIcon("src/cardGraphics/rot-muster.gif");
		scaleImageIcon(backOfCard);
		currentStackSize=stackSize;
	}
	
	/** Setzt den Wert der obersten Karte im Stapel auf Value - setzt zudem die currentTopCard 
	 * auf den gleichen Wert
	 * @param value Wert der obersten Karte des Stapels
	 */
	void setCurrentValue(int value){
		if(value==0){
			currentCardValue=0;
			clearCurrentTopCard();
		}
		else{
			this.currentCardValue=value;
			setCurrentTopCard(value);
		}
	}
	
	/** Sorgt dafuer, dass die Oberste Karte des Stapels mit dem Wert <<value>> dargestellt wird
	 * 
	 * @param value Wert, den die Dargestellte Karte haben soll.
	 */
	public void setCurrentTopCard(int value){
		System.out.println(value);
		System.out.println("src/cardGraphics/Geier"+
		String.valueOf((-1*value))+ ".gif");
		if(value>0){
			currentTopCard = new ImageIcon("src/cardGraphics/Maus"+
					                 String.valueOf(value) + ".gif");
			scaleImageIcon(currentTopCard);
			repaint();
		}
		else{
			currentTopCard = new ImageIcon("src/cardGraphics/Geier"+
					String.valueOf((-1*value))+ ".gif");
			scaleImageIcon(currentTopCard);
			repaint();
		}
	}
	
	/** Gibt das ImageIcon zurueck, das die oberste Karte des Stacks
	 *  repraesentiert
	 * @return ImageIcon des Stacks, das die oberste Karte repraesentiert
	 */
	public ImageIcon getCurrentTopCard(){
		return currentTopCard;
	}
	
	
	/** Wird über den BuzzardTable an den AnimationLayer weitergeleitet, sodass 
	 *  diese die Position der obersten Karte des Stacks kennt
	 * @return Position der obersten Karte des Stacks
	 */
	public Point getAnimationCoordinates(){
		int x = getLocation().x;
		Insets insets = getInsets();
		int y = getLocation().y+insets.bottom+TOP_GAP-CARD_GAP;
		return new Point(x,y);
	}

	
	void clearCurrentTopCard(){
		currentTopCard=null;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Insets insets = getInsets();
		for(int i=0;i<currentStackSize;i++){
			backOfCard.paintIcon(this,g, 0,  insets.bottom+(currentStackSize*CARD_GAP+TOP_GAP)-(i*CARD_GAP));
		}
		if(currentTopCard!=null){
			currentTopCard.paintIcon(this, g,0 , insets.bottom+TOP_GAP-CARD_GAP);
		}
	}
	
	@Override
	public Dimension getPreferredSize(){
		Insets insets = getInsets();
		return new Dimension(picWidth+insets.left+insets.right,picHeight + currentStackSize*CARD_GAP+insets.top+insets.bottom);
	}
	
	/** Skaliert ein ImageIcon auf die Dimension (picWidth, picHeight) */
	private void scaleImageIcon(ImageIcon icon){
		Image img = icon.getImage();
		icon.setImage(img.getScaledInstance(picWidth,picHeight,Image.SCALE_SMOOTH));
	}
	
//	public static void main(String[] args){
//		JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(300,300);
//		DisplayStackPanel panel = new DisplayStackPanel(15);
//		panel.setCurrentTopCard(5);
//		frame.getContentPane().add(panel);
//		frame.setVisible(true);
//		
//	}
}
