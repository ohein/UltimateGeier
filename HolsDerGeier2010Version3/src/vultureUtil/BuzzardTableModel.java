package vultureUtil;

import javax.swing.table.*;

public class BuzzardTableModel extends AbstractTableModel {
	private BuzzardGameModel data;
	
	public BuzzardTableModel(BuzzardGameModel data){
		this.data=data;
	}
	
	@Override
	public Class getColumnClass(int columnIndex){
		return String.class;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return (rowIndex<data.getRowCount()&&columnIndex<data.getColumnCount());
	}
	
	@Override
	public int getRowCount(){
		return data.getRowCount();
	}
	
	@Override
	public int getColumnCount(){
		return data.getColumnCount();
	}
	
	@Override
	public Object getValueAt(int row, int column){
		return data.getValueAt(row,column);
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int colIndex){
		data.setValueAt((String)aValue, rowIndex, colIndex);
	}
}
