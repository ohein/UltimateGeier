package player;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.*;
import java.util.concurrent.locks.*;

import javax.swing.*;

import java.util.*;

public class Human extends HolsDerGeierSpieler {
	
	private int currentReturnValue;
	private PlayerHandDialog dialog;
	
	private ArrayList<Integer> unusedCards;
	
	private ReentrantLock lock;
	private Condition playCard;
	private boolean itsMyTurn;
	
	public Human(String s){
		super(s);
		itsMyTurn = false;
		lock = new ReentrantLock();
		playCard = lock.newCondition();
		dialog = new PlayerHandDialog(null,"Please Select a Card", false);

		System.out.println("Human Constructor Invoked");
	}
	
	@Override
	public void reset(){
		dialog.setVisible(true);
		dialog.showAllCards();
		unusedCards.clear();
		for(int i=1;i<=15;i++){
			unusedCards.add(i);
		}
	}
	
	@Override
	public int gibKarte(int karte){
		lock.lock();
		itsMyTurn = true;
		try{
			playCard.await();
		}catch(InterruptedException ie){
			ie.printStackTrace();
		}finally{
			lock.unlock();
			itsMyTurn = false;
			System.out.println("Unlocked");
		}
		return currentReturnValue;
	}
	
	class PlayerHandDialog extends JDialog{
		
		private ArrayList<JLabel> cards;
		private final int HGAP = 30;
		private final int VGAP = 30;
		private boolean dispose;
		private Container c;

		
		public PlayerHandDialog(JFrame owner, String title, boolean modal){
			super(owner,title,modal);
			unusedCards = new ArrayList<Integer>();
			dispose = false;
			cards = new ArrayList<JLabel>();
			c = getContentPane();
			c.setLayout(new GridLayout(3, 5, HGAP, VGAP));
			for(int i=1;i<=15;i++){
				ImageIcon imageIcon = new ImageIcon("src/cardGraphics/geld" + i + ".gif");
				scaleImageIcon(imageIcon);
				JLabel tmpLabel = new JLabel(imageIcon);
				c.add(tmpLabel);
				cards.add(tmpLabel);
			}
			addMouseListener(new MouseAdapter(){
				@Override
				public void mousePressed(MouseEvent event){
					if(itsMyTurn){
						Point p = event.getPoint();
						System.out.println(p);
						// SwingUtilities.convertPointToScreen(p,c);
						System.out.println("Punkt: " + p);
						for(int i=1; i<=15;i++){
							System.out.println(cards.get((i-1)).getLocation() + " " + cards.get((i-1)).getSize());
							JLabel currentCard = cards.get((i-1));
							int currentCardX = currentCard.getLocation().x;
							int currentCardY = currentCard.getLocation().y;
							int currentCardXL = currentCardX + currentCard.getSize().width;
							int currentCardYL = currentCardY + currentCard.getSize().height;
							//
							if(p.x >= currentCardX && p.x<=currentCardXL && p.y >= currentCardY && p.y <= currentCardYL){
								System.out.println("Match - Punkt: " + p + "Karte: " + currentCard.getLocation());
								currentReturnValue = i;
								currentCard.setVisible(false);
								lock.lock();
								playCard.signal();
								lock.unlock();
							}
						}
					}
				}
			});
			
			setSize(500,500);
		}
		/** Skaliert ein ImageIcon auf die Dimension (picWidth, picHeight) */
		private void scaleImageIcon(ImageIcon icon){
			Image img = icon.getImage();
			icon.setImage(img.getScaledInstance(80,100,Image.SCALE_SMOOTH));
		}
		
		public void showAllCards(){
			for(JLabel l : cards){
				l.setVisible(true);
			}
		}
	}
}
