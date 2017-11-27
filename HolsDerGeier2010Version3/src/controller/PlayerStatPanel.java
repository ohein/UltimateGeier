package controller;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerStatPanel extends JPanel {
	private final JLabel playerName;
	private final JLabel[] buzzard;
	private final int numberOfBuzzards;
	private final JPanel buzzardPanel;
	private final int shwBuzzardCnt;
	/**Ein JLabel, auf dem die aktuelle Platzierung abgelegt wird.*/ 
	private final JLabel playerPos;
	/**Ein JPanel, auf welches die JLabel's playerName und playerPos gepackt werden.*/
	private final JPanel playerStatus;
	/**Ein String, welcher den Text "Platz - " und die aktuelle Position beinhaltet.*/
	private final String position;
	private int wins; // Zähler der Gewinne
	private int oldWidth;
	private int oldHeight;
	private int newWidth;
	private int newHeight;
	private double widthPct;
	private double heightPct;

	private ImageIcon tmpIcon;
	private int tmpWidth;
	private int tmpHeight;
	ImageIcon start = new ImageIcon("src/graphics/counter/0.gif");
	// Counter Hunderterstelle
	JLabel wincountA = new JLabel(start);
	// Counter Zehnerstelle
	JLabel wincountB = new JLabel(start);
	// Counter Einerstelle
	JLabel wincountC = new JLabel(start);

	/*
	 * War nur mal 'so ne Idee... aber das mit dem Skalieren führt bei dieser
	 * Implementierung zu immer unschärferen Bildern, weshalb die Idee für's
	 * Erste auf Eis gelegt wurde... aber vielleicht kommt man ja später noch
	 * Einmal auf das Skalieren der Grafik zurück....
	 */

	class StatComponentListener extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent e) {
			// System.out.println("StatComponentListener: componentResized()");
			// System.out.println(e.getComponent().getWidth()+" * " +
			// e.getComponent().getHeight());
			if (oldWidth != 0 && oldHeight != 0 && newHeight != 0
					&& newWidth != 0) {
				// System.out.println("=======");
				newWidth = e.getComponent().getWidth();
				newHeight = e.getComponent().getHeight();

				widthPct = (newWidth / (double) oldWidth);
				heightPct = newHeight / (double) oldHeight;
				System.out.println("oldWidth: " + oldWidth);
				System.out.println("newWidth: " + newWidth);
				System.out.println("widhtPct: " + widthPct);
				oldWidth = newWidth;
				oldHeight = newHeight;

				for (int i = 0; i < buzzard.length; i++) {
					tmpIcon = ((ImageIcon) buzzard[i].getIcon());
					tmpWidth = tmpIcon.getIconWidth();
					tmpHeight = tmpIcon.getIconHeight();

					System.out.println(tmpWidth + " * " + tmpHeight);
					tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(
							((int) (tmpWidth * widthPct)),
							((int) (tmpHeight * heightPct)),
							Image.SCALE_AREA_AVERAGING));
					buzzard[i].revalidate();
					buzzard[i].repaint();
				}
			} else {
				newWidth = 1;
				newHeight = 1;
				oldWidth = e.getComponent().getWidth();
				oldHeight = e.getComponent().getHeight();
			}
		}
	}
	/**Erzeugt ein Statusfenster, welches den Namen eines Spielers
	 * und einen GewinnzŠhler (Gewonnene Runden) beinhaltet.*/
	public PlayerStatPanel(String playerName, int numberOfBuzzards) {
		position="Erste Runde";
		this.setLayout(new GridLayout(2, 1));
		this.numberOfBuzzards = numberOfBuzzards;
		this.playerName = new JLabel(playerName);
		this.playerName.setLayout(new GridLayout(2, 1));
		this.playerName.setFont(new Font("SansSerif", Font.BOLD, 27));
		this.playerName.setHorizontalAlignment(JLabel.CENTER);
		
		//add(this.playerName);
		this.playerPos = new JLabel(position);
		this.playerPos.setFont(new Font("SansSerif", Font.PLAIN, 12));
		this.playerPos.setHorizontalAlignment(JLabel.CENTER);
	
		this.playerStatus = new JPanel();
		this.playerStatus.setLayout(new GridLayout(2, 1));
		this.playerStatus.add(this.playerName);

		this.playerStatus.add(this.playerPos);
		
		add(this.playerStatus);
		
		buzzardPanel = new JPanel();
		// buzzardPanel.setLayout(new GridLayout(1, 1, 0, 0));
		buzzard = new JLabel[numberOfBuzzards];
		buzzardPanel.add(wincountA);
		buzzardPanel.add(wincountB);
		buzzardPanel.add(wincountC);

		/*
		 * for (int i = 0; i < numberOfBuzzards; i++) { buzzard[i] = new
		 * JLabel(new ImageIcon("src/graphics/pokalGrau.gif"));
		 * buzzardPanel.add(buzzard[i]); }
		 */
		shwBuzzardCnt = 0;
		add(buzzardPanel);
		oldWidth = getWidth();
		oldHeight = getHeight();
		// addComponentListener(new StatComponentListener());
	}
	/**Erzeugt ein Statusfenster, welches den Namen eines Spielers beinhaltet, dessen Position
	 * und einen GewinnzŠhler (Gewonnene Runden)*/
	public PlayerStatPanel(String playerName, int numberOfBuzzards, int pos) {
		//Hier wird der Konstruktor Ÿberladen und nimmt zusŠtzlich den Integer Wert pos (die aktuelle Position)
		//entgegen
		position = "Platz - "+String.valueOf(pos);// Hier wird die Ÿbergebene Position zu dem String "Platz - " 
		//hinzugefŸgt, so dass ein String mit "Position - XYZ" entsteht

		this.setLayout(new GridLayout(2, 1));
		this.numberOfBuzzards = numberOfBuzzards;
		//Initialisierung des JLabel's playerName
		this.playerName = new JLabel(playerName);
		this.playerName.setFont(new Font("SansSerif", Font.BOLD, 27));
		this.playerName.setHorizontalAlignment(JLabel.CENTER);
		//Initialisierung des JLabel's playerPos
		this.playerPos = new JLabel(position);
		this.playerPos.setFont(new Font("SansSerif", Font.PLAIN, 20));
		this.playerPos.setHorizontalAlignment(JLabel.CENTER);
		//Initialisierung des JPanel's playerStatus
		this.playerStatus = new JPanel();
		this.playerStatus.setLayout(new GridLayout(2, 1));
		this.playerStatus.add(this.playerName);
		if (pos!=0) 	// in der ersten Runde wird die Position 0 Ÿbergeben, d.h. in der ersten Runde soll 
						// das JLabel mit der aktuellen Poition nicht angezeigt werden.
		this.playerStatus.add(this.playerPos);
		
		add(this.playerStatus);
		
		buzzardPanel = new JPanel();
		// buzzardPanel.setLayout(new GridLayout(1, 1, 0, 0));
		buzzard = new JLabel[numberOfBuzzards];
		buzzardPanel.add(wincountA);
		buzzardPanel.add(wincountB);
		buzzardPanel.add(wincountC);

		/*
		 * for (int i = 0; i < numberOfBuzzards; i++) { buzzard[i] = new
		 * JLabel(new ImageIcon("src/graphics/pokalGrau.gif"));
		 * buzzardPanel.add(buzzard[i]); }
		 */
		shwBuzzardCnt = 0;
		add(buzzardPanel);
		oldWidth = getWidth();
		oldHeight = getHeight();
		// addComponentListener(new StatComponentListener());
	}

	void showNextBuzzard() {
		if (shwBuzzardCnt < numberOfBuzzards) {
			wins++;
			int winA = wins / 100; // Berechung der Hunderterstellen
			int winBB = wins / 10; // Zwischenberechnung
			int winB = winBB % 10; // Berechnung der Zehnerstelle
			int winC = wins % 10; // Berechnung der Einerstelle

			String pfad = "src/graphics/counter/";
			ImageIcon a = new ImageIcon(pfad + winA + ".gif");
			ImageIcon b = new ImageIcon(pfad + winB + ".gif");
			ImageIcon c = new ImageIcon(pfad + winC + ".gif");

			wincountB.setIcon(b);
			wincountA.setIcon(a);
			wincountC.setIcon(c);

			// buzzard[shwBuzzardCnt++].setIcon(new ImageIcon(
			// "src/graphics/pokalGold.gif"));
		} else {
			throw new RuntimeException("Show Buzzard Overflow");
		}
	}
}
