package tableDisplay;

import java.awt.*;
import javax.swing.*;

import player.*;
@Deprecated
public class DisplayPlayerPanel extends JPanel{
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
	
	private ImageIcon pictureIcon;
	
	private GridBagConstraints gbc;
	private GridBagLayout gbl;
	
	private int picHeight = 120;
	private int picWidth = 95;
	
	private Font font = new Font("SansSerif", Font.BOLD, 15);
	private Color fontColor = Color.WHITE;
	
	private Font[] anFonts;
	
	public DisplayPlayerPanel(int number, HolsDerGeierSpieler player){
		gbl=new GridBagLayout();
		initAnFonts();
		setLayout(gbl);
		this.number=number;
		nameLabel = new JLabel(player.getName());
		prepareLabel(nameLabel);
		String playerClassName = player.getClass().toString();
		if(playerClassName.lastIndexOf(".")>0){
			classLabel = new JLabel(playerClassName.substring(playerClassName.lastIndexOf(".")+1));
		}
		else{
			classLabel = new JLabel(playerClassName);
		}
		prepareLabel(classLabel);
		pointsLabel = new JLabel("0");
		prepareLabel(pointsLabel);
		pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		winLabel = new JLabel();
		prepareLabel(winLabel);
		
		pictureLabel = new JLabel();
		pictureIcon = new ImageIcon();
		
		cardPanel = new JPanel();
		cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
		cardPanel.add(pictureLabel);
		Dimension d = new Dimension(120,136);
		cardPanel.setPreferredSize(d);
		cardPanel.setMinimumSize(d);
		cardPanel.setMaximumSize(d);
		gbc = createGBC(0,0,1,1,30,100);
		gbc.fill=GridBagConstraints.NONE;
		gbc.anchor=GridBagConstraints.NORTHEAST;
		gbl.setConstraints(cardPanel,gbc);
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
		gbc=createGBC(1,0,1,1,70,100);
		gbc.fill=GridBagConstraints.NONE;
		gbc.anchor=GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(0,0,0,0);
		gbl.setConstraints(infoPanel,gbc);
		add(infoPanel);
		
	}
	

	
	public void setPoints(int points){
		pointsLabel.setText(String.valueOf(points));
	}
	
	public void showCardHidden(int value){
		currentCardValue=value;
		ImageIcon icon = new ImageIcon("src/cardGraphics/blau-muster.gif");
		scaleImageIcon(icon);
		pictureLabel.setIcon(icon);
		pictureLabel.revalidate();
		pictureLabel.repaint();
	}
	
	public void showCardFront(int value){
		currentCardValue=value;
		showCurrentCard();
	}
	
	public void showCardBack(int value){
		currentCardValue=value;
		ImageIcon icon = new ImageIcon("src/cardGraphics/blau-muster.gif");
		scaleImageIcon(icon);
		pictureLabel.setIcon(icon);
		pictureLabel.revalidate();
		pictureLabel.repaint();
	}
	
	public void clearCard(){
		currentCardValue=0;
		ImageIcon icon = null;
		pictureLabel.setIcon(icon);
		pictureLabel.revalidate();
		pictureLabel.repaint();
	}
	
	public void showCurrentCard(){
		ImageIcon icon = new ImageIcon("src/cardGraphics/geld"+String.valueOf(currentCardValue)+".gif");
		scaleImageIcon(icon);
		pictureLabel.setIcon(icon);
		pictureLabel.revalidate();
		pictureLabel.repaint();
	}

	
	
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
	
	private void scaleImageIcon(ImageIcon icon){
		Image img = icon.getImage();
		icon.setImage(img.getScaledInstance(picWidth,picHeight,Image.SCALE_SMOOTH));
	}
	
	
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
		DisplayPlayerPanel panel = new DisplayPlayerPanel(0,new IntelligentBuzzardPlayer("Detlef Eigen"));
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
