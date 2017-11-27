package enrollGUI;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import player.HolsDerGeierSpieler;

import java.util.ArrayList;

public class NetworkPlayerList extends JList implements ListSelectionListener{

	private GameEnrollMediator med;
	private DefaultListModel model;
	private ArrayList<HolsDerGeierSpieler> player;
	private static final String parentPlayer = "BuzzardPlayer.class";
	
	
	public NetworkPlayerList(GameEnrollMediator med){
		this.med=med;
		this.med.registerNetworkPlayerList(this);
//		player = new ArrayList<HolsDerGeierSpieler>();
		addListSelectionListener(this);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model = new DefaultListModel();
		setModel(model);
		setFixedCellWidth(250);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e){
		if(getSelectedIndex()>=0){
			med.select();
			med.clearSelectionAvailablePlayerList();
		}
	}

	public String getElementAt(int index){
		return (String)model.getElementAt(index);
		
	}

	public HolsDerGeierSpieler getElement(int index){
		return player.get(index);
	}
	
	public void refreshData(ArrayList<HolsDerGeierSpieler> player){
	    model.removeAllElements();
	  //  this.player.clear();
	    //this.player.
	    this.player = player;
	    
	    
        for(HolsDerGeierSpieler p:player){
        	model.addElement(p.getName());
		}
	
		
		 /*
		
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
	*/	
	}
}
