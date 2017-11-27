package enrollGUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import player.*;


public class EnrolledPlayerList extends JList implements ListSelectionListener {
	private GameEnrollMediator med;
	private DefaultListModel model;
	
	public EnrolledPlayerList(GameEnrollMediator med){
		this.med=med;
		this.med.registerEnrolledPlayerList(this);
		addListSelectionListener(this);
		model = new DefaultListModel();
		setModel(model);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setFixedCellWidth(250);
	}
	
	public HolsDerGeierSpieler getElementAt(int index){
		return (HolsDerGeierSpieler)model.getElementAt(index);
	}
	
	public void clearList(){
		model.removeAllElements();
	}
	
	public int getNumberOfElements(){
		return model.size();
	}
	
	public void removeElementAt(int index){
		model.removeElementAt(index);
	}
	
	public void removeElement(Object element){
		model.removeElement(element);
	}
	
	public void addPlayer(HolsDerGeierSpieler player){
		model.addElement(player);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e){
		System.out.println(e.getSource().toString());
	}
}
