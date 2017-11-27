package enrollGUI;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.*;

public class AvailablePlayerList extends JList implements ListSelectionListener{
	private GameEnrollMediator med;
	private DefaultListModel model;
	private static final String parentPlayer = "BuzzardPlayer.class";
	
	public AvailablePlayerList(GameEnrollMediator med){
		this.med=med;
		this.med.registerAvailablePlayerList(this);
		addListSelectionListener(this);
		
		// Gruppe 3 Punkt 11 Mehrfachselektion
		//setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		model = new DefaultListModel();
		setModel(model);
		setFixedCellWidth(250);
		refreshData();
		
	}
	
	void refreshData(){
		model.removeAllElements();
		File file = new File("src/standardPlayers/");
		String[] files = file.list();
		for(int i=0;i<files.length;i++){
			if(!files[i].equals(parentPlayer)){
				model.addElement(files[i]);
			}
		}
		String externalPlayerFiles = vultureUtil.PropertyManager.getValue("playerPath");
		System.out.println(externalPlayerFiles);
		if(externalPlayerFiles!=null){
			file = new File(externalPlayerFiles);
			files = file.list();
			String test;
			System.out.println("Start Test");
			for(int i=0;i<files.length;i++){
				if(files[i].lastIndexOf(".")>0){
					test = files[i].substring(files[i].lastIndexOf(".")+1);
					System.out.println(test);
					if(test.equals("class")){
						model.addElement(files[i]);
					}
				}
			}
		}
	}
	
	
	public String getElementAt(int index){
		return (String)model.getElementAt(index);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e){
		if(getSelectedIndex()>=0){
			med.select();
			med.clearSelectionNetworkPlayerList();
		}
	}
	
}
