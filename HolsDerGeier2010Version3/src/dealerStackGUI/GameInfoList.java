package dealerStackGUI;

import javax.swing.*;
import java.util.*;
import player.*;

public class GameInfoList extends JList {
	private DefaultListModel model;
	private DealerStackPanel panel;
	
	public GameInfoList(DealerStackPanel panel){
		this.panel= panel;
		model = new DefaultListModel();
		setModel(model);
		this.panel.registerGameInfoList(this);
		this.setFixedCellWidth(210);
	}
	
	void displayPlayers(ArrayList<HolsDerGeierSpieler> list){
		Iterator<HolsDerGeierSpieler> it = list.iterator();
		while(it.hasNext()){
			model.addElement(it.next().toString());
		}
	}
	
	public void clear(){
		model.removeAllElements();
	}

}
