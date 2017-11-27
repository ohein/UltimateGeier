package dealerStackGUI;

import java.util.Iterator;
import java.util.Queue;

import javax.swing.event.*;
import javax.swing.*;

import controller.BuzzardSeriesInfo;

public class RegisteredGamesList extends JList implements ListSelectionListener {
	private DefaultListModel model;
	private DealerStackPanel panel;
	
	public RegisteredGamesList(DealerStackPanel panel){
		this.panel=panel;
		this.panel.registerRegisteredGamesList(this);
		model = new DefaultListModel();
		addListSelectionListener(this);
		setFixedCellWidth(240);
		setModel(model);
	}
	
	public void clear(){
		model.removeAllElements();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event){
		panel.regListValueChanged();
	}
	
	void displayGames(Queue<BuzzardSeriesInfo> queue){
		BuzzardSeriesInfo info;
		Iterator<BuzzardSeriesInfo> it = queue.iterator();
		while(it.hasNext()){
			info=it.next();
			model.addElement(info.toString());
		}
	}
}
