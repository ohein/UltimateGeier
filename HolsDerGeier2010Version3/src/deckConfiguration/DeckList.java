package deckConfiguration;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class DeckList extends JList {
	
	private DeckDialog deckDialog;
	
	private DefaultListModel model;
	
	public DeckList(DeckDialog deckDialog){
		this.deckDialog = deckDialog;
		deckDialog.registerDeckList(this);
		
		try{
			FileInputStream fis = new FileInputStream("src/data/deck.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			DefaultListModel dlm = (DefaultListModel) ois.readObject();
			System.out.print("Größe des Modells: ");
			System.out.println(dlm.getSize());
//			DefaultListModel dlm2 = new DefaultListModel();
//			setModel(dlm2);
//			for(int i = 0; i < dlm.getSize(); i++){
//				getDefaultListModel().addElement(dlm.getElementAt(i));
//			}
			model=dlm;
			setModel(dlm);
			revalidate();
		}catch(Exception e){
			System.err.println("Should not be here");
			setModel(new DefaultListModel());
		}
		
	}
	
	DefaultListModel getDefaultListModel(){
		return model;
	}

}
