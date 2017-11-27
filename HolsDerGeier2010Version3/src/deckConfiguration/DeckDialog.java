package deckConfiguration;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class DeckDialog extends JDialog {

	private DeckList deckList;
	private JTextField cardField;
	private JLabel cardLabel;
	private CardButton cardButton;
	private DeleteButton deleteButton;
	private OKButton okButton;
	private CancelButton cancelButton;

	public DeckDialog(Frame owner, String title){
		super(owner, title, true);
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		GridBagConstraints gbc;
		
		new DeckList(this);
		JScrollPane scrollPane = new JScrollPane(deckList);
		gbc = makegbc(0,0,2,1);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx=100;
		gbc.weighty=100;
		gbl.setConstraints(scrollPane, gbc);
		getContentPane().add(scrollPane);
		
		cardLabel = new JLabel("Neue Karte");
		gbc = makegbc(0, 1, 1, 1);
		gbl.setConstraints(cardLabel, gbc);
		getContentPane().add(cardLabel);
		
		new CardField(this);
		gbc = makegbc(1,1,1,1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(cardField, gbc);
		getContentPane().add(cardField);
		
		new CardButton(this);
		gbc = makegbc(0,2,1,1);
		gbl.setConstraints(cardButton,gbc);
		getContentPane().add(cardButton);
		
		new DeleteButton(this);
		gbc = makegbc(1,2,1,1);
		gbl.setConstraints(deleteButton, gbc);
		getContentPane().add(deleteButton);

		new OKButton(this);
		gbc = makegbc(0,3,1,1);
		gbc.weightx = 50;
		gbc.weighty = 50;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(okButton,gbc);
		getContentPane().add(okButton);
		
		
		new CancelButton(this);
		gbc = makegbc(1,3,1,1);
		gbc.weightx = 50;
		gbc.weighty = 50;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(cancelButton, gbc);
		getContentPane().add(cancelButton);
		
		setSize(400,300);
		
	}
	
	private GridBagConstraints makegbc(int x, int y, int width, int height){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		return gbc;
	}
	
	
	void cardButtonPressed(){
		String card = cardField.getText();
		if(card.equals("")){
			JOptionPane.showMessageDialog(this, "Bitte Wert der Karte Eingeben", 
					                       "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		try{
			Integer cardValue = Integer.parseInt(card);
			DefaultListModel list = deckList.getDefaultListModel();
			list.addElement(card);
			invalidate();
			validate();
			repaint();
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Bitte gültigen Kartenwert (ganze Zahl) verwenden.", 
					"Fehler", JOptionPane.ERROR_MESSAGE); 
		}
		

	}
	
	void deleteButtonPressed(){
		int i = deckList.getSelectedIndex();
		if(i==-1){
			JOptionPane.showMessageDialog(this, "Bitte Karte zum Löschen selektieren.", 
					"Fehler", JOptionPane.ERROR_MESSAGE);
		}else{
			deckList.getDefaultListModel().removeElementAt(i);
		}
		
	}
	
	void okButtonPressed(){
		try{
			FileOutputStream fos = new FileOutputStream("src/data/deck.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(deckList.getDefaultListModel());
		}catch(Exception e){
			e.printStackTrace();
		}
		setVisible(false);
	}
	
	void cancelButtonPressed(){
		setVisible(false);
	}
	
	void registerDeckList(DeckList deckList){
		this.deckList = deckList;
	}
	
	void registerCardField(CardField cardField){
		this.cardField = cardField;
	}
	
	void registerCardButton(CardButton cardButton){
		this.cardButton = cardButton;
	}
	
	void registerDeleteButton(DeleteButton deleteButton){
		this.deleteButton = deleteButton;
	}
	
	void registerOKButton(OKButton okButton){
		this.okButton = okButton;
	}
	
	void registerCancelButton(CancelButton cancelButton){
		this.cancelButton = cancelButton;
	}
	
	
}
