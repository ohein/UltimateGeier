package tableDisplay;

import java.awt.*;
import javax.swing.*;

import player.*;

public class DisplayPlayerPanel2 extends JPanel{
	// Derzeitige Dimension: 300 x 136
	
	
	private int number;
	private int currentCardValue;
	
	private JPanel cardPanel;
	private JPanel infoPanel;
	
	private JLabel pictureLabel;
	private JLabel nameLabel;
	private JLabel classLabel;
	private JLabel pointsLabel;
	private Color labelColor = new Color(0,90,100);
	
	private JLabel winLabel;
	
	
	
	private int picHeight = 120;
	private int picWidth = 95;
	
	private Font font = new Font("SansSerif", Font.BOLD, 15);
	private Color fontColor = Color.WHITE;
	
	private Font[] anFonts;
	
	public void seatPlayer(HolsDerGeierSpieler player){
		nameLabel.setText(player.getName());
		String playerClassName = player.getClass().toString();
		if(playerClassName.lastIndexOf(".")>0){
			classLabel.setText(playerClassName.substring(playerClassName.lastIndexOf(".")+1));
		}
		else{
			classLabel = new JLabel(playerClassName);
		}
		pointsLabel.setText("0");
		winLabel.setText("");
	}
	
	public void removePlayer(){
		nameLabel.setText("");
		classLabel.setText("");
		pointsLabel.setText("");
		winLabel.setText("");
		currentCardValue=0;
		pictureLabel.setIcon(null);
	}
	
	
	public DisplayPlayerPanel2(int number, HolsDerGeierSpieler player){
		this(number);
		seatPlayer(player);
	}
	
	public DisplayPlayerPanel2(int number){
		initAnFonts();
		setLayout(null);
		this.number=number;
		
		nameLabel = new JLabel();
		prepareLabel(nameLabel);
		
		classLabel = new JLabel();
		prepareLabel(classLabel);
		
		pointsLabel = new JLabel();
		prepareLabel(pointsLabel);
		pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		winLabel = new JLabel();
		prepareLabel(winLabel);
		
		pictureLabel = new JLabel();
		
		
		cardPanel = new JPanel();
		cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
		cardPanel.add(pictureLabel);
		Dimension d = new Dimension(120,136);
		cardPanel.setPreferredSize(d);
		cardPanel.setMinimumSize(d);
		cardPanel.setMaximumSize(d);
		cardPanel.setBounds(0,0,120,136);
		add(cardPanel);
		
		infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
		infoPanel.setLayout(new GridLayout(4,1,0,12));
		Dimension di = new Dimension(180,136);
		infoPanel.setMinimumSize(di);
		infoPanel.setMaximumSize(di);
		infoPanel.setPreferredSize(di);
		infoPanel.add(nameLabel);
		infoPanel.add(classLabel);
		infoPanel.add(pointsLabel);
		infoPanel.add(winLabel);
		infoPanel.setBounds(119,0,180,136);
		add(infoPanel);
		Dimension dtop = new Dimension(300,137);
		setPreferredSize(dtop);
		setMaximumSize(dtop);
		setMinimumSize(dtop);
	}
	

	/** Setzt den dargestellten Punktestand auf den Wert points
	 * @param points Wert, der auf dem Label dargestellt wird
	 */
	public void setPoints(int points){
		pointsLabel.setText(String.valueOf(points));
	}
	
	/** Gibt die Anzahl der Punkte des Spielers zurueck. Hierzu wird der String, welcher auf dem 
	 *  pointsLabel (Klasse JLabel) zu finden ist, in einen int konvertiert und zurueckgegeben.
	 * @return Anzahl der Punkte des Spielers
	 */
	public int getPoints(){
		return Integer.parseInt(pointsLabel.getText());
	}
	
	/** Setzt den Wert der Aktuellen Karte auf value, sorgt jedoch dafuer, dass die Karte nur mit ihrer Rueckansicht dargestellt wird */
	public void showCardHidden(int value){
		showCardBack(value);
	}
	
	/** Setzt den Wert der aktuellen Karte auf Value und sorgt dafuer, dass die Karte mit ihrer Vorderansicht dargestellt wird*/
	public void showCardFront(int value){
		currentCardValue=value;
		showCurrentCard();
	}
	
	/** Setzt den Wert der Aktuellen Karte auf value, sorgt jedoch dafuer, dass die Karte nur mit ihrer Rueckansicht dargestellt wird */
	public void showCardBack(int value){
		currentCardValue=value;
		ImageIcon icon = new ImageIcon("src/cardGraphics/blau-muster.gif");
		scaleImageIcon(icon);
		pictureLabel.setIcon(icon);
		pictureLabel.revalidate();
		pictureLabel.repaint();
	}
	
	/** Entfernt die dargestellte Karte*/
	public void clearCard(){
		currentCardValue=0;
		ImageIcon icon = null;
		pictureLabel.setIcon(icon);
		pictureLabel.revalidate();
		pictureLabel.repaint();
	}
	
	/** Sorgt dafuer, dass eine Karte mit dem Wert currentValue im Programm mit ihrer Vorderansich dargestellt wird*/
	public void showCurrentCard(){
		ImageIcon icon = new ImageIcon("src/cardGraphics/geld"+String.valueOf(currentCardValue)+".gif");
		scaleImageIcon(icon);
		pictureLabel.setIcon(icon);
		pictureLabel.revalidate();
		pictureLabel.repaint();
	}

	
	/** Diese Methode bereitet das Label vor, in dem es selbiges die Eigenschaft opaque=true verleight, die entsprechende Labelfarbe zuweist,
	 *  die Schrift sowie die Schriftfarbe einstellt und die Dimension des Labels setzt
	 * @param Das zu praeparierende Label.
	 */
	private void prepareLabel(JLabel label){
		label.setOpaque(true);
		label.setBackground(labelColor);
		label.setFont(font);
		label.setForeground(fontColor);
		Dimension d = new Dimension(180,24);
		label.setMaximumSize(d);
		label.setMinimumSize(d);
		label.setPreferredSize(d);
	}
	
	/** Skaliert ein ImageIcon auf die Dimension (picWidth, picHeight) */
	private void scaleImageIcon(ImageIcon icon){
		Image img = icon.getImage();
		icon.setImage(img.getScaledInstance(picWidth,picHeight,Image.SCALE_SMOOTH));
	}
	
	/** Convenience-Methode, die ein GridBagConstraints-Objekt erzeugt*/
	private GridBagConstraints createGBC(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty){
		GridBagConstraints res = new GridBagConstraints();
		res.gridx=gridx;
		res.gridy=gridy;
		res.gridwidth=gridwidth;
		res.gridheight=gridheight;
		res.weightx=weightx;
		res.weighty=weighty;
		return res;
	}
	
	/** Erzeugt das Array, welches die fuer die Punktanimation benoetigten Schriftarten enthaelt*/
	private void initAnFonts(){
		anFonts = new Font[10];
		for(int i=0;i<10;i++){
			anFonts[i] = new Font("SansSerif", Font.BOLD, (20+(i*2)));
		}
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,200);
		DisplayPlayerPanel2 panel = new DisplayPlayerPanel2(0,new IntelligentBuzzardPlayer("Detlef Eigen"));
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		panel.showCardBack(3);

		try{
			Thread.sleep(1500);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		panel.showCurrentCard();
		try{
			Thread.sleep(1500);
		}
		catch(InterruptedException ie){
			ie.printStackTrace();
		}
		
	}
	
}
